package com.santiago.camera.event.camera.surface_callback;

import com.santiago.event.Event;

/**
 * Created by santiago on 09/03/16.
 */
public class OnSurfaceVisibilityChangedEvent extends Event {

    private boolean visibility = false;

    public OnSurfaceVisibilityChangedEvent(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean isSurfaceVisible() {
        return visibility;
    }

}
