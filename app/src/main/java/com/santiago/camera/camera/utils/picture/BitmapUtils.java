package com.santiago.camera.camera.utils.picture;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by santiago on 13/03/16.
 */
public class BitmapUtils {

    public static Bitmap ChangeBitmapFromExif(Bitmap source, ExifReader.CameraExifData exifData) {
        Matrix matrix = new Matrix();

        //Apply rotation
        matrix.postRotate(exifData.getRotation());

        //Apply a flip depending which was
        switch(exifData.getFlip().getIndex()) {
            case ExifReader.Flip.HORIZONTAL_INDEX:
                matrix.preScale(-1, 1);
                break;

            case ExifReader.Flip.VERTICAL_INDEX:
                matrix.preScale(1, -1);
        }

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
