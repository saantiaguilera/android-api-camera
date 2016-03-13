package com.santiago.camera.mocks.controller;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.santiago.camera.manager.type.CameraType;
import com.santiago.camera.mocks.event.MockSwitchCameraChangedEvent;
import com.santiago.controllers.BaseEventController;

/**
 * Created by santiago on 13/03/16.
 */
public class MockSwitchCameraController extends BaseEventController<ImageView> {

    private CameraType cameraMode = CameraType.FRONT;

    public MockSwitchCameraController(Context context) {
        this(context, null);
    }

    public MockSwitchCameraController(Context context, ImageView imageView) {
        super(context, imageView);
    }

    @Override
    protected void onViewAttached(ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShootClicked();
            }
        });
    }

    private void onShootClicked() {
        if(cameraMode == CameraType.FRONT) {
            cameraMode = CameraType.BACK;
        } else {
            cameraMode = CameraType.FRONT;
        }

        broadcastEvent(new MockSwitchCameraChangedEvent(cameraMode));
    }

}
