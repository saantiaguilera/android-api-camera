package com.santiago.camera.camera.utils.surface;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.santiago.camera.event.camera_surface_callback.OnSurfaceCreatedEvent;
import com.santiago.camera.event.camera_surface_callback.OnSurfaceVisibilityChangedEvent;
import com.santiago.event.EventManager;

/**
 * Class that interacts with the callbacks of the surface holders and notifies the user about certain events
 * Pay close atention to the EventManager. If you dont set it you wont be notified about anything
 * Pay close atention to the listener. If you dont set it you wont be the one setting the size of the camera
 *
 * Created by santiago on 09/03/16.
 */
public class CameraSurfaceHandler implements SurfaceHolder.Callback {

    private Camera camera;

    private EventManager eventManager;

    private CameraSurfaceCallbackListener listener;

    public CameraSurfaceHandler(Camera camera) {
        this.camera = camera;
    }

    /**
     * You should ALWAYS set the camera if it has changed
     *
     * @param camera
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Will be used for broadcasting creations or changes of our surface
     * @param eventHandler
     */
    public void setEventHandler(EventManager eventHandler) {
        if(eventHandler==null)
            throw new NullPointerException(EventManager.class.getName() + " cant be null in " + getClass().getName());

        this.eventManager = eventHandler;
    }

    /**
     * Since we depend on preview sizes, if you plan on using a specific or desired size, you will have to implement this
     * @param listener
     */
    public void setListener(CameraSurfaceCallbackListener listener) {
        this.listener = listener;
    }

    /**
     * Called when the surface has being created
     * @param holder
     */
    public void surfaceCreated(SurfaceHolder holder) {
        //Broadcast an event telling the surface has created.
        if(eventManager!=null)
            eventManager.broadcastEvent(new OnSurfaceCreatedEvent());
    }

    /**
     * Called when the surface suffers some change (rotation, picture taken, etc)
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(camera==null)
            return;

        //Data will be using
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size;

        //If someone is listening, notify them about what size they want us to use
        if(listener==null)
            size = null;
        else size = listener.getBestPreviewSize(width, height, parameters);

        //If size has being set, apply it to our camera and broadcast a change of visibility
        if (size != null) {
            parameters.setPreviewSize(size.width, size.height);
            camera.setParameters(parameters);
            camera.startPreview();

            if(eventManager!=null)
                eventManager.broadcastEvent(new OnSurfaceVisibilityChangedEvent(true));
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) { }

    public interface CameraSurfaceCallbackListener {
        Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters params);
    }

}