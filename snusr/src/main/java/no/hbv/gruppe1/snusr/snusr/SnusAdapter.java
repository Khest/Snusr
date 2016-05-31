package no.hbv.gruppe1.snusr.snusr;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
        ImageView imgBook = (ImageView) view.findViewById(R.id.imgBook);
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

        imgBook.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.drawable.button_bookmark, 50, 50));

        // Convert byte[] to Bitmap:
        //ImageHandler imageHandler = new ImageHandler();
        byte[] stream = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_snus_img));
        if(stream != null)
            img.setImageBitmap(convertByteToBitmap(stream));
        else
            img.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.drawable.noimagefound, 150, 150));
            //img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.noimagefound));

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap convertByteToBitmap(byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Bitmap mbitmap = bitmap.createScaledBitmap(bitmap, 250, 250, true);
        return mbitmap;
    }


}
