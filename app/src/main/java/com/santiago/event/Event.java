package com.santiago.event;

import com.santiago.event.listeners.EventHandler;
import com.santiago.event.listeners.EventNotifierListener;

/**
 * Created by santiaguilera@theamalgama.com on 01/03/16.
 */
public interface Event {

    /**
     * Notify the handler about this event if needed
     * @param handler
     * @return true if should consume the event (and dont broadcast it to the listeners) or false if it didnt consume the event (it will broadcast to everyone)
     */
    boolean handle(EventHandler handler);

    /**
     * Notify the listener about this event if needed
     * @param listener
     */
    void notify(EventNotifierListener listener);

}
