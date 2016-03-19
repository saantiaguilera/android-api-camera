package com.santiago.camera.mocks.controller.configs;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.santiago.camera.mocks.event.MockShootChangedEvent;
import com.santiago.controllers.BaseEventController;

/**
 * Created by santiago on 13/03/16.
 */
public class MockShootController extends BaseEventController<ImageView> {

    public static final int CAMERA_ENABLED = 0;
    public static final int CAMERA_DISABLED = 1;

    private int cameraMode = CAMERA_ENABLED;

    public MockShootController(Context context) {
        this(context, null);
    }

    public MockShootController(Context context, ImageView imageView) {
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
        if(cameraMode == CAMERA_ENABLED) {
            getView().setImageLevel(CAMERA_DISABLED);
            cameraMode = CAMERA_DISABLED;
        } else {
            getView().setImageLevel(CAMERA_ENABLED);
            cameraMode = CAMERA_ENABLED;
        }

        broadcastEvent(new MockShootChangedEvent(cameraMode));
    }

}
