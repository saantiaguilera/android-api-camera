package com.santiago.camera.mocks.event;

import com.santiago.event.Event;
import com.santiago.event.listeners.EventHandler;
import com.santiago.event.listeners.EventNotifierListener;

/**
 * Created by santiago on 13/03/16.
 */
public class MockShootChangedEvent implements Event {

    private int cameraMode;

    public MockShootChangedEvent(int cameraMode) {
        this.cameraMode = cameraMode;
    }

    @Override
    public boolean handle(EventHandler handler) {
        return false;
    }

    @Override
    public void notify(EventNotifierListener listener) {
        listener.mockShootChange(cameraMode);
    }

}

