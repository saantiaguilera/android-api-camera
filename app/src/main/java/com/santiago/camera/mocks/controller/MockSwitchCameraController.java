package com.santiago.camera.mocks.controller;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.santiago.camera.manager.type.CameraType;
import com.santiago.camera.mocks.event.MockOnCameraStartEvent;
import com.santiago.camera.mocks.event.MockOnPictureTakenEvent;
import com.santiago.camera.mocks.event.MockSwitchCameraChangedEvent;
import com.santiago.controllers.BaseEventController;
import com.santiago.event.anotation.EventMethod;

/**
 * Created by santiago on 13/03/16.
 */
public class MockSwitchCameraController extends BaseEventController<ImageView> {

    private CameraType cameraMode = CameraType.BACK;

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
                onSwitchClick();
            }
        });
    }

    public void setDefaultCameraMode(CameraType cameraType) {
        this.cameraMode = cameraType;
    }

    private void onSwitchClick() {
        if(cameraMode == CameraType.FRONT) {
            cameraMode = CameraType.BACK;
        } else {
            cameraMode = CameraType.FRONT;
        }

        broadcastEvent(new MockSwitchCameraChangedEvent(cameraMode));
    }

    @EventMethod(MockOnPictureTakenEvent.class)
    private void onPictureTaken() {
        getView().setVisibility(View.INVISIBLE);
    }

    @EventMethod(MockOnCameraStartEvent.class)
    private void onCameraStart() {
        getView().setVisibility(View.VISIBLE);
    }

}
