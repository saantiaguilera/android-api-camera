package com.santiago.camera.camera.utils;

import android.graphics.Bitmap;

/**
 * Created by santiago on 13/03/16.
 */
public interface CameraPictureCallback {

    void onPictureTaken(Bitmap picture);
    void onPictureVisibilityChanged(int visibility);

}
