package com.santiago.camera.manager.orientation;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;

import com.santiago.camera.manager.type.CameraType;

/**
 * Created by santiago on 10/03/16.
 */
public class CameraOrientationManager {

    private Context context;

    private int mDisplayOrientation;
    private int mLayoutOrientation;

    public CameraOrientationManager(Context context) {
        this.context = context;
    }

    public int determineDisplayOrientation(CameraType cameraOrientation) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraOrientation.getCameraType(), cameraInfo);

        int rotation = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: {
                degrees = 0;
                break;
            }
            case Surface.ROTATION_90: {
                degrees = 90;
                break;
            }
            case Surface.ROTATION_180: {
                degrees = 180;
                break;
            }
            case Surface.ROTATION_270: {
                degrees = 270;
                break;
            }
        }

        int displayOrientation;

        // Camera direction
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        } //see http://developer.android.com/reference/android/hardware/Camera.html#setDisplayOrientation(int)

        mDisplayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        mLayoutOrientation = degrees;

        return displayOrientation;
    }

}
