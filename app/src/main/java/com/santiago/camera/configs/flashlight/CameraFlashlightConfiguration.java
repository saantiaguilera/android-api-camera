package com.santiago.camera.configs.flashlight;

import android.content.Context;
import android.hardware.Camera;

/**
 * Created by santiago on 13/03/16.
 */
public class CameraFlashlightConfiguration {

    private Context context;

    private String currentMode = Camera.Parameters.FLASH_MODE_AUTO;

    public CameraFlashlightConfiguration(Context context) {
        this.context = context;
    }

    public void setFlashlight(String mode) {
        currentMode = mode;
    }

    public Camera.Parameters applyFlashlight(Camera.Parameters params) {
        if(params==null)
            return null;

        if (params.getSupportedFlashModes().contains(currentMode))
            params.setFlashMode(currentMode);

        return params;
    }

}
