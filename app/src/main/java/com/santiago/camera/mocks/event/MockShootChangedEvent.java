package com.santiago.camera.mocks.event;

import com.santiago.event.Event;

/**
 * Created by santiago on 13/03/16.
 */
public class MockShootChangedEvent extends Event {

    private int cameraMode;

    public MockShootChangedEvent(int cameraMode) {
        this.cameraMode = cameraMode;
    }

    public int getCameraMode() {
        return cameraMode;
    }
}

