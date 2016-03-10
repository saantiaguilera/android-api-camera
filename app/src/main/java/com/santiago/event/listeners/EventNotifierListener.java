package com.santiago.event.listeners;

/**
 * Class that holds all the methods that can be notified about
 * @see com.santiago.event.Event
 *
 * Created by santiaguilera@theamalgama.com on 01/03/16.
 */
public abstract class EventNotifierListener {

    //Camera surface callback
    public void onCameraSurfaceCreated() {};
    public void onSurfaceVisibilityChanged(boolean visibility) {};


}
