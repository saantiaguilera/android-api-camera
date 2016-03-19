package com.santiago.camera.mocks.controller.configs;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.santiago.camera.mocks.event.MockFlashChangedEvent;
import com.santiago.camera.mocks.event.MockOnCameraStartEvent;
import com.santiago.camera.mocks.event.MockOnPictureTakenEvent;
import com.santiago.controllers.BaseEventController;
import com.santiago.event.anotation.EventMethod;

/**
 * Created by santiago on 13/03/16.
 */
public class MockFlashController extends BaseEventController<ImageView> {

    public static final int FLASH_OFF = 0;
    public static final int FLASH_ON = 1;

    private int flashMode = FLASH_ON;

    public MockFlashController(Context context) {
        this(context, null);
    }

    public MockFlashController(Context context, ImageView imageView) {
        super(context, imageView);
    }

    @Override
    protected void onViewAttached(ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFlashButtonClick();
            }
        });
    }

    private void onFlashButtonClick() {
        if(flashMode == FLASH_ON) {
            getView().setImageLevel(FLASH_OFF);
            flashMode = FLASH_OFF;
        } else {
            getView().setImageLevel(FLASH_ON);
            flashMode = FLASH_ON;
        }

        broadcastEvent(new MockFlashChangedEvent(flashMode));
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
