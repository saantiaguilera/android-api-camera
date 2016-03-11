package com.santiago.camera.configs.type;

import android.content.Context;
import android.content.pm.PackageManager;

import com.santiago.camera.configs.type.CameraType;

/**
 * Class in charge of handling which camera is currently being used
 *
 * Created by santiago on 10/03/16.
 */
public class CameraTypeConfiguration {

    private Context context;

    private CameraType currentCamera;

    public CameraTypeConfiguration(Context context) {
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
