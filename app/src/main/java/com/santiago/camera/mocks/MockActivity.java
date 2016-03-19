package com.santiago.camera.mocks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.santiago.camera.R;
import com.santiago.camera.camera.controller.BaseCameraController;
import com.santiago.camera.camera.view.BaseCameraView;
import com.santiago.camera.camera.view.SquaredCameraView;
import com.santiago.camera.mocks.controller.camera.MockCameraController;
import com.santiago.camera.mocks.controller.camera.MockSquaredCameraController;
import com.santiago.camera.mocks.controller.configs.MockFlashController;
import com.santiago.camera.mocks.controller.configs.MockShootController;
import com.santiago.camera.mocks.controller.configs.MockSwitchCameraController;
import com.santiago.event.EventManager;

/**
 * Created by santiago on 10/03/16.
 */
public class MockActivity extends Activity {

    private enum CAMERA { FULL, SQUARED }

    private EventManager eventManager;

    private BaseCameraController cameraController;
    private CAMERA currentCamera = null;

    private MockFlashController flashController;
    private MockShootController shootController;
    private MockSwitchCameraController switchCameraController;

    private FrameLayout container;
    private View squaredCamera;
    private View fullCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventManager = new EventManager(this);

        setContentView(R.layout.mock_activity);

        //Inflate al the stuff we will use
        flashController = new MockFlashController(this, (ImageView) findViewById(R.id.activity_mock_flash_button));
        shootController = new MockShootController(this, (ImageView) findViewById(R.id.activity_mock_shoot_button));
        switchCameraController = new MockSwitchCameraController(this, (ImageView) findViewById(R.id.activity_mock_switch_camera));

        container = (FrameLayout) findViewById(R.id.activity_mock_camera_container);
        fullCamera = findViewById(R.id.activity_mock_full_camera);
        squaredCamera = findViewById(R.id.activity_mock_squared_camera);

        //Set listeners to our cameras (since they are mocks I got a little lazy for creating a controller of them)
        fullCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCamera(CAMERA.FULL);
            }
        });

        squaredCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCamera(CAMERA.SQUARED);
            }
        });

        //Manage events and initialize the camera
        flashController.setEventHandlerListener(eventManager);
        shootController.setEventHandlerListener(eventManager);
        switchCameraController.setEventHandlerListener(eventManager);

        setCamera(CAMERA.FULL);
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

    private void setCamera(CAMERA camera) {
        if(currentCamera == camera)
            return;

        currentCamera = camera;

        //Release all the previous camera things to avoid problems
        if(cameraController!=null)
            cameraController.stopCamera();

        eventManager.removeListener(cameraController);

        //Create the specified camera
        switch(camera) {
            case FULL:
                cameraController = new MockCameraController(this, new BaseCameraView(this));
                break;

            case SQUARED:
                cameraController = new MockSquaredCameraController(this, new SquaredCameraView(this));
        }

        //Set the event manager
        cameraController.setEventHandlerListener(eventManager);

        //Update the default camera
        switchCameraController.setDefaultCameraMode(cameraController.getCameraManager().getCameraTypeManager().getCurrentCamera());

        //Add and start it
        container.removeAllViews();
        container.addView(cameraController.getView());

        cameraController.startCamera();
    }

}