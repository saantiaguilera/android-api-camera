package com.santiago.camera.camera.utils.picture;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by santiago on 19/03/16.
 */
public class PictureCropper {

    public static final int ASPECT_RATIO_UNDEFINED = -1;

    //If w>h TOP==LEFT && BOTTOM==RIGHT
    public enum CROP_GRAVITY { TOP, CENTER, BOTTOM, LEFT, RIGHT }

    public static Bitmap crop(@NonNull Bitmap source, double aspectRatio) {
        return crop(source, aspectRatio, CROP_GRAVITY.TOP);
    }

    public static Bitmap crop(@NonNull Bitmap source, double aspectRatio, @NonNull CROP_GRAVITY gravity) {
        if(aspectRatio == ASPECT_RATIO_UNDEFINED)
            return source;

        switch (gravity) {
            case CENTER:
                if (source.getWidth() >= source.getHeight())
                    return Bitmap.createBitmap(source, (int) ((source.getWidth() - source.getHeight()) / (2 * aspectRatio)), 0, (int) (source.getHeight() * aspectRatio), (int) (source.getHeight() * aspectRatio));
                else return Bitmap.createBitmap(source, 0, (int) ((source.getHeight() - source.getWidth()) / (2 * aspectRatio)), (int) (source.getWidth() * aspectRatio), (int) (source.getWidth() * aspectRatio));

            case LEFT:
            case TOP:
                if (source.getWidth() >= source.getHeight())
                    return Bitmap.createBitmap(source, 0, 0, (int) (source.getHeight() * aspectRatio), (int) (source.getHeight() * aspectRatio));
                else return Bitmap.createBitmap(source, 0, 0, (int) (source.getWidth() * aspectRatio), (int) (source.getWidth() * aspectRatio));

            case BOTTOM:
            case RIGHT:
                if (source.getWidth() >= source.getHeight())
                    return Bitmap.createBitmap(source, (int) ((source.getWidth() - source.getHeight()) / (2 * aspectRatio)), 0, (int) (source.getHeight() * aspectRatio), (int) (source.getHeight() * aspectRatio));
                else return Bitmap.createBitmap(source, 0, (int) ((source.getHeight() - source.getWidth()) / (2 * aspectRatio)), (int) (source.getWidth() * aspectRatio), (int) (source.getWidth() * aspectRatio));
        }

        return source;
    }

}
