package no.hbv.gruppe1.snusr.snusr.interfaces;

import android.graphics.Bitmap;

/**
 * Created by Dakh on 2016-04-10.
 */
public interface ImageHandlerInterface {

    Object handleInputStream(Object input);
    Object cropImage(Object input);
    Object resizeImage(Object input);
    byte[] convertBitmapToByte(Bitmap bitmap);
}
