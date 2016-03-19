package com.santiago.camera.mocks.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.support.annotation.NonNull;

import com.santiago.camera.R;
import com.santiago.camera.camera.controller.BaseCameraController;
import com.santiago.camera.camera.utils.picture.PictureCropper;
import com.santiago.camera.camera.view.SquaredCameraView;
import com.santiago.camera.configs.flashlight.CameraFlashlightConfiguration;
import com.santiago.camera.configs.focus.CameraFocusConfiguration;
import com.santiago.camera.mocks.event.MockFlashChangedEvent;
import com.santiago.camera.mocks.event.MockOnCameraStartEvent;
import com.santiago.camera.mocks.event.MockOnPictureTakenEvent;
import com.santiago.camera.mocks.event.MockShootChangedEvent;
import com.santiago.camera.mocks.event.MockSwitchCameraChangedEvent;
import com.santiago.event.anotation.EventMethod;

/**
 * Created by santiago on 19/03/16.
 */
public class MockSquaredCameraController extends BaseCameraController<SquaredCameraView> {

    private CameraFlashlightConfiguration flashConfiguration;
    private CameraFocusConfiguration focusConfiguration;

    public MockSquaredCameraController(Context context) {
        super(context);
    }

    public MockSquaredCameraController(Context context, SquaredCameraView squaredCameraView) {
        super(context, squaredCameraView);

        flashConfiguration = new CameraFlashlightConfiguration(context);
        flashConfiguration.setFlashlight(Camera.Parameters.FLASH_MODE_OFF);

        focusConfiguration = new CameraFocusConfiguration(context);

        getCameraManager().addConfiguration(flashConfiguration);
        getCameraManager().addConfiguration(focusConfiguration);
    }

    @Override
    protected void onViewAttached(SquaredCameraView squaredCameraView) {
        super.onViewAttached(squaredCameraView);
        squaredCameraView.setBackgroundColor(getContext().getResources().getColor(R.color.mock_black));
    }

    @Override
    public void startCamera() {
        super.startCamera();
        broadcastEvent(new MockOnCameraStartEvent());
    }

    @Override
    protected void onPictureGenerated(Bitmap bitmap) {
        broadcastEvent(new MockOnPictureTakenEvent(bitmap));
    }

    @NonNull
    @Override
    protected PictureCropper.CROP_GRAVITY getCropGravity() {
        return PictureCropper.CROP_GRAVITY.TOP;
    }

    @NonNull
    @Override
    protected PictureCropper.CROP_MODE getCropMode() {
        return PictureCropper.CROP_MODE.SQUARED;
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

}
