package com.santiago.camera.camera.utils.picture;

/**
 * Created by santi on 29/06/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class CameraPictureUtilities {

    public static Bitmap mirrorImage(Bitmap resultBitmap){
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);

        Bitmap returnBitmap =  Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, false);

        resultBitmap.recycle();

        return returnBitmap;
    }

    public static int getRotation(int mDisplayOrientation, int normalOrientation, int mLayoutOrientation, boolean isFrontCamera) {
        int rotation = (
                mDisplayOrientation
                        + normalOrientation
                        + mLayoutOrientation
        ) % 360;

        if(isFrontCamera){
            switch(rotation) {
                case 0:
                    rotation = 180;
                    break;

                case 180:
                    rotation = 0;
            }

            rotation = (rotation+180) % 360;
        }

        return rotation;
    }

    public static Bitmap rotatePicture(Context context, int rotation, Bitmap bitmap) {
        if (rotation != 0) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            bitmap = Bitmap.createBitmap(
                    oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false
            );

            oldBitmap.recycle();
        }

        return bitmap;
    }

}