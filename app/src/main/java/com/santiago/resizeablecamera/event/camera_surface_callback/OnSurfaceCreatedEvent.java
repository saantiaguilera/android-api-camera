package com.santiago.resizeablecamera.event.camera_surface_callback;

import com.santiago.event.Event;
import com.santiago.event.listeners.EventHandler;
import com.santiago.event.listeners.EventNotifierListener;

/**
 * Created by santiago on 09/03/16.
 */
public class OnSurfaceCreatedEvent implements Event {

    @Override
    public boolean handle(EventHandler handler) {
        return false;
    }

    @Override
    public void notify(EventNotifierListener listener) {
        listener.onCameraSurfaceCreated();
    }
}
