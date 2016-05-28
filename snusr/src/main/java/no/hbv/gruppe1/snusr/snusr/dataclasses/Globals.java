package no.hbv.gruppe1.snusr.snusr.dataclasses;

import android.graphics.Bitmap;

import java.util.UUID;

/**
 * Created by Knut Johan Hesten 2016-05-28.
 *
 * Globals and static variables that should be exposed to the entire application
 */
public class Globals {
    public static final String TAG = "SnusrDebug";

    public static final String APP_NAME = "Snusr";
    public static final UUID MY_UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final int IMAGE_QUALITY = 100;
    public static final Bitmap.CompressFormat COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;

    public static final int MYLIST_FAVOURITES = 1;
    public static final int MYLIST_BOOKMARKS  = 0;
    public static final int MYLIST_ALL        = -1;

}
