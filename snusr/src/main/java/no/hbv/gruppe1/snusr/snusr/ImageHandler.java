package no.hbv.gruppe1.snusr.snusr;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import no.hbv.gruppe1.snusr.snusr.interfaces.ImageHandlerInterface;

/**
 * Created by hakonst on 14.04.16.
 */
public class ImageHandler implements ImageHandlerInterface {
    @Override
    public Object handleInputStream(Object input) {
        return null;
    }

    @Override
    public Object cropImage(Object input) {
        return null;
    }

    @Override
    public Object resizeImage(Object input) {
        return null;
    }

    @Override
    public byte[] convertBitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return bos.toByteArray();
    }
}
