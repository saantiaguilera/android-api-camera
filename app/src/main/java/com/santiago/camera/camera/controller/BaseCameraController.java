package com.santiago.camera.camera.controller;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.View;

import com.santiago.camera.camera.utils.CameraSurfaceHolder;
import com.santiago.camera.configs.CameraManager;
import com.santiago.camera.event.camera.OnCameraModifiedEvent;
import com.santiago.controllers.BaseEventController;
import com.santiago.event.EventManager;
import com.santiago.event.listeners.EventNotifierListener;
import com.santiago.camera.camera.utils.CameraSurfaceCallback;

import java.io.IOException;

/**
 * Controller for a really basic camera
 *
 * Created by santiago on 09/03/16.
 */
public abstract class BaseCameraController<T extends View & CameraSurfaceHolder> extends BaseEventController<T> implements CameraSurfaceCallback.CameraSurfaceCallbackListener {

    private SurfaceHolder surfaceHolder;
    private CameraSurfaceCallback cameraSurfaceCallback;

    private CameraManager cameraManager;

    private boolean surfaceActive = false;

    private EventNotifierListener eventNotifierListener;

    private Camera camera;

    public BaseCameraController(Context context) {
        this(context, null);
    }

    public BaseCameraController(Context context, T t) {
        super(context, t);

        cameraManager = new CameraManager(context);
    }

    /*-----------------Class overrides-----------------*/

    @Override
    protected void onViewAttached(T t) {
        surfaceHolder = t.getSurfaceHolder();

        cameraSurfaceCallback = new CameraSurfaceCallback(camera);
        cameraSurfaceCallback.setListener(this);

        //In case they dont set us an EventHandler, we do it on our own because we will need to broadcast things internally. If they do then dont mind this
        setEventHandlerListener(new EventManager(getContext()));

        //As soon as we are setting him a callback, process will start and we will eventually be notified (in the cameraSurfaceCallback class about its creation)
        surfaceHolder.addCallback(cameraSurfaceCallback);
    }

    @Override
    public void setEventHandlerListener(EventManager eventManager) {
        super.setEventHandlerListener(eventManager);

        cameraSurfaceCallback.setEventHandler(eventManager);
    }

    @Override
    protected EventNotifierListener getEventNotifierListener() {
        if (eventNotifierListener == null) {
            eventNotifierListener = new EventNotifierListener() {

                @Override
                public void onCameraSurfaceCreated() {
                    startCamera();
                }

                @Override
                public void onSurfaceVisibilityChanged(boolean visibility) {
                    surfaceActive = visibility;
                }

                @Override
                public void onCameraModified() {
                    cameraSurfaceCallback.setCamera(camera);
                }
            };
        }

        return eventNotifierListener;
    }

    /*-------------------Methods---------------------------------*/

    /**
     * If the camera exists sets the surfaceholder in it
     */
    private void setPreviewDisplay() {
        if(camera==null)
            return;

        try {
            //Since the surface is created, set the camera in it
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call for starting the camera
     */
    public void startCamera() {
        //If the camera is already running, stop it
        if(camera!=null)
            stopCamera();

        camera = cameraManager.createNewCamera();

        //Set the preview display again
        setPreviewDisplay();

        //Notify to the people listening that our camera has suffer modifications
        notifyCameraChanged();

        camera.startPreview();
    }

    /**
     * Call for stopping the camera
     * <strong> When taking a picture and showing it its higly recommended to stop the camera to free memory </strong>
     * <strong> Always call this when you finished using the camera to make it available to others </strong>
     */
    public void stopCamera() {
        if (surfaceActive)
            camera.stopPreview();

        camera.release();
        camera = null;

        notifyCameraChanged();

        surfaceActive = false;
    }

    /**
     * Call it everytime the camera suffers a change to make the people interact with the modifications
     *
     * Pretty much like the BaseAdapter notifyDataSetChanged() thing
     */
    public void notifyCameraChanged() {
        broadcastEvent(new OnCameraModifiedEvent());
    }

    /*--------------------Abstracty methods---------------------------*/

    /**
     * Adapt to your particular camera (if you need a square preview or a specific one)
     * and return the sizes you want the preview to be
     * @param width
     * @param height
     * @param parameters
     * @return Camera.Size or null for skipping it
     */
    @Override
    public abstract Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters);

}