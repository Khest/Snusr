package no.hbv.gruppe1.snusr.snusr.interfaces;

import android.graphics.Bitmap;

/**
 * Created by Knut Johan Hesten on 2016-04-10.
 * Interface stub for imagespace functions and methods. Implementation TBD
 */
public interface ImageHandlerInterface {

    Object handleInputStream(Object input);
    Object cropImage(Object input);
    Object resizeImage(Object input);
    byte[] convertBitmapToByte(Bitmap bitmap);
    Bitmap convertByteToBitmap(Byte[] img);
}
