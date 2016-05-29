package no.hbv.gruppe1.snusr.snusr;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.provider.ContactsContract;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import no.hbv.gruppe1.snusr.snusr.dataclasses.Snus;

/**
 * @author Håkon Stensheim
 */
public class SnusAdapter extends CursorAdapter {

    private LayoutInflater inflater;

    public SnusAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.from(context).inflate(R.layout.snus_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Set the contentvalues for the view:
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
                // Update the rating in MYLIST in DB:

            }
        });
        // Convert byte[] to Bitmap:
        //ImageHandler imageHandler = new ImageHandler();
//        byte[] stream = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_snus_img));
//        if(stream != null)
//            img.setImageBitmap(imageHandler.convertByteToBitmap(stream));
//        else
//            img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.noimagefound));

    }

}
