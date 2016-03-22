package com.santiago.camera.mocks.controller.misc;

import android.content.Context;
import android.view.View;

import com.santiago.camera.mocks.event.MockAspectRatioChangedEvent;
import com.santiago.camera.mocks.view.MockInputAspectRatioView;
import com.santiago.controllers.BaseEventController;

/**
 * Created by santiago on 21/03/16.
 */
public class MockInputAspectRatioController extends BaseEventController<MockInputAspectRatioView> {

    public MockInputAspectRatioController(Context context) {
        super(context);
    }

    public MockInputAspectRatioController(Context context, MockInputAspectRatioView mockInputAspectRatioView) {
        super(context, mockInputAspectRatioView);
    }

    @Override
    protected void onViewAttached(MockInputAspectRatioView mockInputAspectRatioView) {
        mockInputAspectRatioView.setDoneViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broadcastEvent(new MockAspectRatioChangedEvent(getView().getAspectRatio()));
            }
        });
    }

}
