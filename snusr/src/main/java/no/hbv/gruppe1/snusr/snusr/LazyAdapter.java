package no.hbv.gruppe1.snusr.snusr;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Long Huynh on 30.05.2016.
 */
public class LazyAdapter extends BaseAdapter {
    Activity activity;
    LayoutInflater inflater;
    Cursor cursor;

    public LazyAdapter(Activity a,  Cursor c, int f) {
        cursor = c;
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null) {
            view = inflater.inflate(R.layout.snus_list_item, parent, false);

            TextView txtSnusName = (TextView) view.findViewById(R.id.txtSnusName);
            TextView txtRating = (TextView) view.findViewById(R.id.txtRating);
            ImageView img = (ImageView) view.findViewById(R.id.imgSnusThumbnail);

            // Finds the ratingbar and sets the stars to yellow.
            RatingBar rating = (RatingBar) view.findViewById(R.id.ratingSnus);
            //Drawable stars = rating.getProgressDrawable();
            //DrawableCompat.setTint(stars, Color.YELLOW);

            String snusname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_line_name))
                    + " " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_snus_name));
            txtSnusName.setText(snusname);

            txtRating.setText(String.valueOf(rating.getRating()));

            rating.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_snus_totalrank)));
            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                }
                // Update the rating in MYLIST in DB:
                // Convert byte[] to Bitmap:
                //ImageHandler imageHandler = new ImageHandler();
            });
        byte[] stream = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_snus_img));
        if(stream != null)

            img.setImageBitmap(convertByteToBitmap(stream));
        else
            img.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.noimagefound));
           // img.setImageResource(imagePaths[position]);
        }

        return view;

        }

    public static Bitmap convertByteToBitmap(byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
        return bitmap;
    }


}
