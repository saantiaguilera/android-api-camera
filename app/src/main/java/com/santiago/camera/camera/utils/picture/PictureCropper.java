package com.santiago.camera.camera.utils.picture;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by santiago on 19/03/16.
 */
public class PictureCropper {

    public enum CROP_MODE { NONE, SQUARED }

    //If w>h TOP==LEFT && BOTTOM==RIGHT
    public enum CROP_GRAVITY { TOP, CENTER, BOTTOM, LEFT, RIGHT }

    public static Bitmap crop(@NonNull Bitmap source, @NonNull CROP_MODE mode) {
        return crop(source, mode, CROP_GRAVITY.TOP);
    }

    public static Bitmap crop(@NonNull Bitmap source, @NonNull CROP_MODE mode, @NonNull CROP_GRAVITY gravity) {

        switch(mode) {
            case SQUARED:
                switch (gravity) {
                    case CENTER:
                        if (source.getWidth() >= source.getHeight())
                            return Bitmap.createBitmap(source, source.getWidth()/2 - source.getHeight()/2, 0, source.getHeight(), source.getHeight());
                        else return Bitmap.createBitmap(source, 0, source.getHeight()/2 - source.getWidth()/2, source.getWidth(), source.getWidth());

                    case LEFT:
                    case TOP:
                        if (source.getWidth() >= source.getHeight())
                            return Bitmap.createBitmap(source, 0, 0, source.getHeight(), source.getHeight());
                        else return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getWidth());

                    case BOTTOM:
                    case RIGHT:
                        if (source.getWidth() >= source.getHeight())
                            return Bitmap.createBitmap(source, source.getWidth() - source.getHeight(), 0, source.getHeight(), source.getHeight());
                        else return Bitmap.createBitmap(source, 0, source.getHeight() - source.getWidth(), source.getWidth(), source.getWidth());
                }

            case NONE:
            default:
                return source;
        }
    }

}
