package com.santiago.camera.mocks.event;

import com.santiago.event.Event;

/**
 * Created by santiago on 13/03/16.
 */
public class MockFlashChangedEvent extends Event {

    private int flashMode;

    public MockFlashChangedEvent(int flashMode) {
        this.flashMode = flashMode;
    }

    public int getFlashMode() {
        return flashMode;
    }
}
