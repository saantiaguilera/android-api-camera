package com.santiago.camera.mocks.event;

import com.santiago.camera.manager.type.CameraType;
import com.santiago.event.Event;
import com.santiago.event.listeners.EventHandler;
import com.santiago.event.listeners.EventNotifierListener;

/**
 * Created by santiago on 13/03/16.
 */
public class MockSwitchCameraChangedEvent implements Event {

    private CameraType cameraType;

    public MockSwitchCameraChangedEvent(CameraType cameraType) {
        this.cameraType = cameraType;
    }

    @Override
    public boolean handle(EventHandler handler) {
        return false;
    }

    @Override
    public void notify(EventNotifierListener listener) {
        listener.mockSwitchCameraChange(cameraType);
    }

}