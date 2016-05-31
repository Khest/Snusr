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

    public static final int MYLIST_BOOKMARKED               = 1;
    public static final int MYLIST_REMOVED_FROM_BOOKMARKS   = 0;
    public static final int MYLIST_ALL                      = -1;

    public static final int MYLIST_RATED                    = 12;
    public static final int MYLIST_ADDED                    = 13;


    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final int DEVICE_NAME_IDENTIFIER = 6;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

}
