package com.santiago.camera.configs;

import android.content.Context;
import android.hardware.Camera;

import com.santiago.camera.configs.orientation.CameraOrientationConfiguration;
import com.santiago.camera.configs.type.CameraType;
import com.santiago.camera.configs.type.CameraTypeConfiguration;

/**
 * Created by santiago on 10/03/16.
 */
public class CameraManager {

    private CameraTypeConfiguration cameraTypeConfiguration;
    private CameraOrientationConfiguration cameraOrientationConfiguration;
    private CameraFocusConfiguration cameraFocusConfiguration;

    public CameraManager(Context context) {
        cameraTypeConfiguration = new CameraTypeConfiguration(context);
        cameraOrientationConfiguration = new CameraOrientationConfiguration(context);
        cameraFocusConfiguration = new CameraFocusConfiguration(context);
    }

    public CameraOrientationConfiguration getCameraOrientationConfiguration() {
        return cameraOrientationConfiguration;
    }

    public CameraTypeConfiguration getCameraTypeConfiguration() {
        return cameraTypeConfiguration;
    }

    public CameraFocusConfiguration getCameraFocusConfiguration() {
        return cameraFocusConfiguration;
    }

    public Camera createNewCamera() {
        //Get the current type of camera (Front/Back). If the user has override the method for implementing a custom one, use theirs as long as it exists. If not ours
        CameraType currentCamera = getCameraTypeConfiguration().getCurrentCamera();

        //Create it facing the desired place
        Camera camera = Camera.open(currentCamera.getCameraType());

        //Since the display orientation behaves like shit in android, we have a config that interacts with it and makes it work as intended
        camera.setDisplayOrientation(getCameraOrientationConfiguration().determineDisplayOrientation(currentCamera));

        //Lets set a focus mode
        camera.setParameters(getCameraFocusConfiguration().applyFocus(camera.getParameters()));

        return camera;
    }

}
