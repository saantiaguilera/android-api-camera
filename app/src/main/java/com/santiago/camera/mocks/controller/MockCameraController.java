package com.santiago.camera.mocks.controller;

import android.content.Context;
import android.hardware.Camera;

import com.santiago.camera.camera.controller.BaseCameraController;
import com.santiago.camera.camera.view.BaseCameraView;
import com.santiago.camera.configs.flashlight.CameraFlashlightConfiguration;
import com.santiago.camera.configs.focus.CameraFocusConfiguration;
import com.santiago.camera.manager.type.CameraType;
import com.santiago.event.listeners.EventNotifierListener;

import java.util.List;

/**
 * Created by santiago on 10/03/16.
 */
public class MockCameraController extends BaseCameraController<BaseCameraView> {

    private EventNotifierListener eventNotifierListener;

    private CameraFlashlightConfiguration flashConfiguration;
    private CameraFocusConfiguration focusConfiguration;

    public MockCameraController(Context context) {
        super(context);
    }

    public MockCameraController(Context context, BaseCameraView baseCameraView) {
        super(context, baseCameraView);

        flashConfiguration = new CameraFlashlightConfiguration(context);
        focusConfiguration = new CameraFocusConfiguration(context);

        getCameraManager().addConfiguration(flashConfiguration);
        getCameraManager().addConfiguration(focusConfiguration);
    }

    @Override
    protected EventNotifierListener getEventNotifierListener() {
        if(eventNotifierListener == null) {
            eventNotifierListener = new EventNotifierListener() {
                @Override
                public void mockFlashChange(int flashMode) {
                    flashConfiguration.setFlashlight(flashMode==MockFlashController.FLASH_ON ? Camera.Parameters.FLASH_MODE_ON : Camera.Parameters.FLASH_MODE_OFF);
                }

                @Override
                public void mockShootChange(int cameraMode) {
                    if(cameraMode == MockShootController.CAMERA_ENABLED) {
                        startCamera();
                    } else {
                        takePicture();
                    }
                }

                @Override
                public void mockSwitchCameraChange(CameraType cameraType) {
                    getCameraManager().getCameraTypeManager().setCamera(cameraType);
                    startCamera();
                }
            };
        }

        return eventNotifierListener;
    }

    @Override
    public Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double) height / width;

        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

        if (parameters.getSupportedPreviewSizes() == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;

            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;

            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;

            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }

}
