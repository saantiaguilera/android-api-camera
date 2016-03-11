package com.santiago.camera.mocks;

import android.app.Activity;
import android.os.Bundle;

import com.santiago.camera.R;
import com.santiago.camera.camera.view.BaseCameraView;

/**
 * Created by santiago on 10/03/16.
 */
public class MockActivity extends Activity {

    private MockCameraController cameraController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mock);

        cameraController = new MockCameraController(this, (BaseCameraView) findViewById(R.id.activity_mock_base_camera_view));
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
