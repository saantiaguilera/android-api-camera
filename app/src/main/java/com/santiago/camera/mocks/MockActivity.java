package com.santiago.camera.mocks;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.santiago.camera.R;
import com.santiago.camera.camera.view.BaseCameraView;
import com.santiago.camera.mocks.controller.MockCameraController;
import com.santiago.camera.mocks.controller.MockFlashController;
import com.santiago.camera.mocks.controller.MockShootController;
import com.santiago.camera.mocks.controller.MockSwitchCameraController;
import com.santiago.event.EventManager;

/**
 * Created by santiago on 10/03/16.
 */
public class MockActivity extends Activity {

    private EventManager eventManager;

    private MockCameraController cameraController;

    private MockFlashController flashController;
    private MockShootController shootController;
    private MockSwitchCameraController switchCameraController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventManager = new EventManager(this);

        setContentView(R.layout.activity_mock);

        cameraController = new MockCameraController(this, (BaseCameraView) findViewById(R.id.activity_mock_base_camera_view));

        flashController = new MockFlashController(this, (ImageView) findViewById(R.id.activity_mock_flash_button));
        shootController = new MockShootController(this, (ImageView) findViewById(R.id.activity_mock_shoot_button));
        switchCameraController = new MockSwitchCameraController(this, (ImageView) findViewById(R.id.activity_mock_switch_camera));

        cameraController.setEventHandlerListener(eventManager);
        flashController.setEventHandlerListener(eventManager);
        shootController.setEventHandlerListener(eventManager);
        switchCameraController.setEventHandlerListener(eventManager);

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
