package com.santiago.camera.event.camera;

import com.santiago.event.Event;
import com.santiago.event.listeners.EventHandler;
import com.santiago.event.listeners.EventNotifierListener;

/**
 * Created by santiago on 10/03/16.
 */
public class OnCameraModifiedEvent implements Event {
    @Override
    public boolean handle(EventHandler handler) {
        return false;
    }

    @Override
    public void notify(EventNotifierListener listener) {
        listener.onCameraModified();
    }

}
