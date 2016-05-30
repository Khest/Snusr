package no.hbv.gruppe1.snusr.snusr.dummydata;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;
import no.hbv.gruppe1.snusr.snusr.dataclasses.DatabaseInteractor;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Globals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Knut Johan Hesten 2016-05-30.
 */
public class PutDummyDataExtra {
    private Context context;

    public PutDummyDataExtra(Context context) {
        this.context = context;
    }

    public void putDymmyData(String id, String filename) {
        DatabaseInteractor di = null;
        AssetManager am = context.getAssets();
        try (InputStream is = am.open(filename)) {
            Bitmap bm = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            if (b == null) {
                Log.e(Globals.TAG, " No image data received. Check if file exists");
                return;
            }
            di = new DatabaseInteractor(this.context);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.FeedEntry.col_snus_img, b);
            di.update(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
                    cv, DatabaseHelper.FeedEntry.col_snus_id + "=" + id, null);
            Log.i(Globals.TAG, "Inserted dummy data into " + DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS + " with ID " + id);
        } catch (IOException ioex) {
            Log.e(Globals.TAG, " Unable to load file : " + ioex.getMessage());
        } catch (Exception ex) {
            Log.e(Globals.TAG, " A fatal error occured when trying to update dummy data " + ex.getMessage());
        } finally {
            if (di != null) {
                di.close();
            }
        }
    }
}