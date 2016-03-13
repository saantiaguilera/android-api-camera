package com.santiago.camera.configs;

import android.content.Context;
import android.hardware.Camera;

/**
 * Created by santiago on 13/03/16.
 */
public class CameraFocusConfiguration {

    private Context context;

    private String currentFocusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;

    public CameraFocusConfiguration(Context context) {
        this.context = context;
    }

    public void setFocus(String focusMode) {
        currentFocusMode = focusMode;
    }

    public Camera.Parameters applyFocus(Camera.Parameters params) {
        if(params==null)
            return null;

        if (params.getSupportedFocusModes().contains(currentFocusMode))
            params.setFocusMode(currentFocusMode);

        return params;
    }

}
