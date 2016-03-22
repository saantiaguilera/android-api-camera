package com.santiago.camera.mocks.controller.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.view.View;

import com.santiago.camera.camera.controller.AspectRatioCameraController;
import com.santiago.camera.configs.flashlight.CameraFlashlightConfiguration;
import com.santiago.camera.configs.focus.CameraFocusConfiguration;
import com.santiago.camera.manager.CameraManager;
import com.santiago.camera.mocks.controller.configs.MockFlashController;
import com.santiago.camera.mocks.controller.configs.MockShootController;
import com.santiago.camera.mocks.event.MockFlashChangedEvent;
import com.santiago.camera.mocks.event.MockOnCameraStartEvent;
import com.santiago.camera.mocks.event.MockOnPictureTakenEvent;
import com.santiago.camera.mocks.event.MockShootChangedEvent;
import com.santiago.camera.mocks.event.MockSwitchCameraChangedEvent;
import com.santiago.event.EventManager;
import com.santiago.event.anotation.EventMethod;

/**
 * Created by santiago on 19/03/16.
 */
public class MockGenericCameraController implements AspectRatioCameraController.CameraListener, AspectRatioCameraController.PictureGeneratedListener {

    private EventManager eventManager;

    private AspectRatioCameraController cameraController;

    private CameraFlashlightConfiguration flashConfiguration;
    private CameraFocusConfiguration focusConfiguration;

    public MockGenericCameraController(Context context) {
        flashConfiguration = new CameraFlashlightConfiguration(context);
        flashConfiguration.setFlashlight(Camera.Parameters.FLASH_MODE_OFF);

        focusConfiguration = new CameraFocusConfiguration(context);

        cameraController = new AspectRatioCameraController.Builder(context)
                .setCameraListener(this)
                .setPictureGeneratedListener(this)
                .addConfiguration(flashConfiguration)
                .addConfiguration(focusConfiguration)
                .build();
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        eventManager.addListener(this);

        cameraController.setEventHandlerListener(eventManager);
    }

    @Override
    public void onPictureGenerated(Bitmap bitmap) {
        if(eventManager!=null)
            eventManager.broadcastEvent(new MockOnPictureTakenEvent(bitmap));
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
        else cameraController.takePicture();
    }

    @EventMethod(MockSwitchCameraChangedEvent.class)
    private void onSwitchCamera(MockSwitchCameraChangedEvent event) {
        cameraController.getCameraManager().getCameraTypeManager().setCamera(event.getCameraType());
        startCamera();
    }

    @Override
    public void onCameraStart() {
        if(eventManager!=null)
            eventManager.broadcastEvent(new MockOnCameraStartEvent());
    }

    @Override
    public void onCameraStop() { }

    public void startCamera() {
        cameraController.startCamera();
    }

    public void stopCamera() {
        cameraController.stopCamera();
    }

    public CameraManager getCameraManager() {
        return cameraController.getCameraManager();
    }

    public View getCameraView() {
        return cameraController.getView();
    }

    public void resizeCamera(Double aspectRatio) { cameraController.resize(aspectRatio); }

}
