package com.santiago.camera.mocks;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.santiago.camera.R;
import com.santiago.camera.mocks.controller.camera.MockGenericCameraController;
import com.santiago.camera.mocks.controller.configs.MockFlashController;
import com.santiago.camera.mocks.controller.configs.MockShootController;
import com.santiago.camera.mocks.controller.configs.MockSwitchCameraController;
import com.santiago.camera.mocks.controller.misc.MockInputAspectRatioController;
import com.santiago.camera.mocks.event.MockAspectRatioChangedEvent;
import com.santiago.camera.mocks.view.MockInputAspectRatioView;
import com.santiago.event.EventManager;
import com.santiago.event.anotation.EventMethod;

/**
 * Created by santiago on 10/03/16.
 */
public class MockActivity extends Activity {


    private EventManager eventManager;

    private MockGenericCameraController cameraController;

    private MockFlashController flashController;
    private MockShootController shootController;
    private MockSwitchCameraController switchCameraController;
    private MockInputAspectRatioController aspectRatioController;

    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventManager = new EventManager(this);
        eventManager.addListener(this);

        setContentView(R.layout.mock_activity);

        //Inflate al the stuff we will use
        flashController = new MockFlashController(this, (ImageView) findViewById(R.id.activity_mock_flash_button));
        shootController = new MockShootController(this, (ImageView) findViewById(R.id.activity_mock_shoot_button));
        switchCameraController = new MockSwitchCameraController(this, (ImageView) findViewById(R.id.activity_mock_switch_camera));
        container = (FrameLayout) findViewById(R.id.activity_mock_camera_container);
        aspectRatioController = new MockInputAspectRatioController(this, (MockInputAspectRatioView) findViewById(R.id.activity_mock_input_aspect_ratio));

        //Manage events and initialize the camera
        flashController.setEventHandlerListener(eventManager);
        shootController.setEventHandlerListener(eventManager);
        switchCameraController.setEventHandlerListener(eventManager);
        aspectRatioController.setEventHandlerListener(eventManager);

        //Create the specified camera
        cameraController = new MockGenericCameraController(this);

        //Set the event manager
        cameraController.setEventManager(eventManager);

        //Update the default camera
        switchCameraController.setDefaultCameraMode(cameraController.getCameraManager().getCameraTypeManager().getCurrentCamera());

        //Add and start it
        container.removeAllViews();
        container.addView(cameraController.getCameraView());
    }

    @EventMethod(MockAspectRatioChangedEvent.class)
    private void onAspectRatioChange(MockAspectRatioChangedEvent event) {
        if(event.getAspectRatio() >= 2 || event.getAspectRatio() < 1)
            return;

        cameraController.resizeCamera(event.getAspectRatio());
    }

    @Override
    protected void onResume() {
        super.onResume();

        cameraController.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();

        cameraController.stopCamera();
    }

}