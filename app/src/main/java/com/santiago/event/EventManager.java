package com.santiago.event;

import android.content.Context;

import com.santiago.event.listeners.EventHandler;
import com.santiago.event.listeners.EventListener;
import com.santiago.event.listeners.EventNotifierListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Class in charge of receiving events (from listeners or holders) and manage them or else broadcast them to the other listeners
 * if no action will be done
 *
 * Created by santiaguilera@theamalgama.com on 01/03/16.
 */
public class EventManager implements EventListener {

    private Context context = null;

    private List<EventNotifierListener> eventNotifierListenerList = new ArrayList<>(); //List of all the instances willing to receive events

    public EventManager(Context context){

        if(context == null)
            throw new NullPointerException("context cannot be null in EventManager");

        this.context = context;

    }

    public Context getContext() {
        return context;
    }

    /**
     * Adds an instance (that must implement EventNotifierListener) to the list of all the
     * classes that will be notified in the income of an event
     * @param listener
     */
    public void addEventNotifierListener(EventNotifierListener listener){

        if(listener==null)
            return;

        eventNotifierListenerList.add(listener);

    }

    /**
     * Removes an instance to the list of all the classes that will be notified in the income of an event
     * @param listener
     * @return if it was successfully removed
     */
    public boolean removeEventNotifierListener(EventNotifierListener listener){

        if(listener==null)
            return false;

        return eventNotifierListenerList.remove(listener);

    }

    /**
     * Override and implmement an instance of EventHandler if you will handle a particular event
     * in your EventManager class before broadcasting it
     *
     * Useful for requests. Eg. You will  broadcast a requestAEvent. The EventHandler will have a
     * onHandleRequestA(dataForA); and so when you receive this method you will act accordingly, but
     * the listeners of the broadcast wont do nothing (maybe yes, but lets say no, its an eg).
     * In the success or failure of it you will broadcast a new Event called either
     * FailureAEvent or SuccessAEvent which will or wont have handlers (also this depends if you
     * will still take further actions) but will broadcast to the public and do something on some
     * of them.
     * @return EventHandler for handling the particular event before broadcasting it to the public
     */
    protected EventHandler getEventHandler() { return null; }

    /**
     * Method called (by one sending an event) for the purpose of managing that event, or else broadcast it to the
     * list of classes listening
     * @param event
     * @return if the event was handled or not by the EventManager
     */
    @Override
    public boolean broadcastEvent(Event event) {
        //If we do have an instance of EventHandler, and handle returns true (it consumes the event, preventing from broadcasting it) (see Event)
        if(getEventHandler()!=null && event.handle(getEventHandler()))
            return true;

        for(EventNotifierListener listener: new ArrayList<>(eventNotifierListenerList)){
            if(listener!=null)
                event.notify(listener);
        }

        return eventNotifierListenerList.isEmpty();
    }

}
