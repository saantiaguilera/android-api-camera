package com.santiago.camera.mocks.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;

import com.santiago.camera.R;
import com.santiago.camera.camera.controller.BaseCameraController;
import com.santiago.camera.camera.view.BaseCameraView;
import com.santiago.camera.configs.flashlight.CameraFlashlightConfiguration;
import com.santiago.camera.configs.focus.CameraFocusConfiguration;
import com.santiago.camera.mocks.event.MockFlashChangedEvent;
import com.santiago.camera.mocks.event.MockOnCameraStartEvent;
import com.santiago.camera.mocks.event.MockOnPictureTakenEvent;
import com.santiago.camera.mocks.event.MockShootChangedEvent;
import com.santiago.camera.mocks.event.MockSwitchCameraChangedEvent;
import com.santiago.event.anotation.EventMethod;

import java.util.List;

/**
 * Created by santiago on 10/03/16.
 */
public class MockCameraController extends BaseCameraController<BaseCameraView> {

    private CameraFlashlightConfiguration flashConfiguration;
    private CameraFocusConfiguration focusConfiguration;

    public MockCameraController(Context context) {
        super(context);
    }

    public MockCameraController(Context context, BaseCameraView baseCameraView) {
        super(context, baseCameraView);

        flashConfiguration = new CameraFlashlightConfiguration(context);
        flashConfiguration.setFlashlight(Camera.Parameters.FLASH_MODE_OFF);

        focusConfiguration = new CameraFocusConfiguration(context);

        getCameraManager().addConfiguration(flashConfiguration);
        getCameraManager().addConfiguration(focusConfiguration);
    }

    @Override
    protected void onViewAttached(BaseCameraView baseCameraView) {
        super.onViewAttached(baseCameraView);
        baseCameraView.setBackgroundColor(getContext().getResources().getColor(R.color.mock_black));
    }

    @EventMethod(MockFlashChangedEvent.class)
    private void onFlashChange(MockFlashChangedEvent event) {
        flashConfiguration.setFlashlight(event.getFlashMode() == MockFlashController.FLASH_ON ? Camera.Parameters.FLASH_MODE_ON : Camera.Parameters.FLASH_MODE_OFF);
        startCamera();
    }

    @EventMethod(MockShootChangedEvent.class)
    private void onShootOrientationChange(MockShootChangedEvent event) {
        if(event.getCameraMode() == MockShootController.CAMERA_ENABLED)
            startCamera();
        else takePicture();
    }

    @EventMethod(MockSwitchCameraChangedEvent.class)
    private void onSwitchCamera(MockSwitchCameraChangedEvent event) {
        getCameraManager().getCameraTypeManager().setCamera(event.getCameraType());
        startCamera();
    }

    @Override
    public void startCamera() {
        super.startCamera();
        broadcastEvent(new MockOnCameraStartEvent());
    }

    @Override
    protected void onPictureGenerated(Bitmap bitmap) {
        broadcastEvent(new MockOnPictureTakenEvent());
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
