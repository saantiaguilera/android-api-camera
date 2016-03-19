package com.santiago.camera.mocks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.santiago.camera.R;
import com.santiago.camera.camera.view.SquaredCameraView;
import com.santiago.camera.mocks.controller.MockCameraController;
import com.santiago.camera.mocks.controller.MockFlashController;
import com.santiago.camera.mocks.controller.MockShootController;
import com.santiago.camera.mocks.controller.MockSwitchCameraController;
import com.santiago.camera.mocks.event.MockOnPictureTakenEvent;
import com.santiago.event.EventManager;
import com.santiago.event.anotation.EventMethod;

/**
 * Created by santiago on 10/03/16.
 */
public class MockActivity extends Activity {

    private EventManager eventManager;

    private MockCameraController cameraController;

    private MockFlashController flashController;
    private MockShootController shootController;
    private MockSwitchCameraController switchCameraController;

    private ImageView testingImage;
    private FrameLayout testingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventManager = new EventManager(this);
        eventManager.addListener(this);

        setContentView(R.layout.mock_activity);

        cameraController = new MockCameraController(this, (SquaredCameraView) findViewById(R.id.activity_mock_camera_view));

        flashController = new MockFlashController(this, (ImageView) findViewById(R.id.activity_mock_flash_button));
        shootController = new MockShootController(this, (ImageView) findViewById(R.id.activity_mock_shoot_button));
        switchCameraController = new MockSwitchCameraController(this, (ImageView) findViewById(R.id.activity_mock_switch_camera));

        testingImage = (ImageView) findViewById(R.id.testing);
        testingContainer = (FrameLayout) findViewById(R.id.testing_container);

        cameraController.setEventHandlerListener(eventManager);
        flashController.setEventHandlerListener(eventManager);
        shootController.setEventHandlerListener(eventManager);
        switchCameraController.setEventHandlerListener(eventManager);

        switchCameraController.setDefaultCameraMode(cameraController.getCameraManager().getCameraTypeManager().getCurrentCamera());
    }

    @EventMethod(MockOnPictureTakenEvent.class)
    private void onPictureTaken(MockOnPictureTakenEvent event) {
        testingContainer.setVisibility(View.VISIBLE);
        testingImage.setImageBitmap(event.getBitmap());
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
