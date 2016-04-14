package no.hbv.gruppe1.snusr.snusr.interfaces;

/**
 * Created by Dakh on 2016-04-10.
 */
public interface ImageHandlerInterface {

    Object handleInputStream(Object input);
    Object cropImage(Object input);
    Object resizeImage(Object input);
}
