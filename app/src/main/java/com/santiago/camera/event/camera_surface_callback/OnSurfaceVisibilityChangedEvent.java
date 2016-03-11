package com.santiago.camera.event.camera_surface_callback;

import com.santiago.event.Event;
import com.santiago.event.listeners.EventHandler;
import com.santiago.event.listeners.EventNotifierListener;

/**
 * Created by santiago on 09/03/16.
 */
public class OnSurfaceVisibilityChangedEvent implements Event {

    private boolean visibility = false;

    public OnSurfaceVisibilityChangedEvent(boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean handle(EventHandler handler) {
        return false;
    }

    @Override
    public void notify(EventNotifierListener listener) {
        listener.onSurfaceVisibilityChanged(visibility);
    }
}
