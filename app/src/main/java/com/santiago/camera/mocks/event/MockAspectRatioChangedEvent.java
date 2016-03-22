package com.santiago.camera.mocks.event;

import com.santiago.event.Event;

/**
 * Created by santiago on 21/03/16.
 */
public class MockAspectRatioChangedEvent extends Event {

    private double aspectRatio;

    public MockAspectRatioChangedEvent(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }
}
