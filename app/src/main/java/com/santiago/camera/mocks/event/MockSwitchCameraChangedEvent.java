package com.santiago.camera.mocks.event;

import com.santiago.camera.manager.type.CameraType;
import com.santiago.event.Event;

/**
 * Created by santiago on 13/03/16.
 */
public class MockSwitchCameraChangedEvent extends Event {

    private CameraType cameraType;

    public MockSwitchCameraChangedEvent(CameraType cameraType) {
        this.cameraType = cameraType;
    }

    public CameraType getCameraType() {
        return cameraType;
    }

}