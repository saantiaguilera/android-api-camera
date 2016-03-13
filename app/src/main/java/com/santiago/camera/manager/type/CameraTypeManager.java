package com.santiago.camera.manager.type;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Class in charge of handling which camera is currently being used
 *
 * Created by santiago on 10/03/16.
 */
public class CameraTypeManager {

    private Context context;

    private CameraType currentCamera;

    public CameraTypeManager(Context context) {
        this.context = context;
        this.currentCamera = getBackCamera();
    }

    public void setCamera(CameraType camera) {
        if (currentCamera == CameraType.FRONT)
            currentCamera = getBackCamera();
        else currentCamera = getFrontCamera();
    }

    public CameraType getCurrentCamera() { return currentCamera; }

    private CameraType getFrontCamera() {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT))
            return CameraType.FRONT;

        return getBackCamera();
    }

    private CameraType getBackCamera() {
        return CameraType.BACK;
    }

    public boolean hasFrontCamera() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

    public boolean hasCamera() { return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY); }

}
