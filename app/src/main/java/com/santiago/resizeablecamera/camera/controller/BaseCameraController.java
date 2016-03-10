package com.santiago.resizeablecamera.camera.controller;

import android.content.Context;
import android.hardware.Camera;
import android.util.Pair;
import android.view.SurfaceHolder;

import com.santiago.controllers.BaseEventController;
import com.santiago.event.EventManager;
import com.santiago.event.listeners.EventNotifierListener;
import com.santiago.resizeablecamera.camera.utils.CameraSurfaceCallback;
import com.santiago.resizeablecamera.camera.view.BaseCameraView;

import java.io.IOException;

/**
 * Controller for a really basic camera
 *
 * Created by santiago on 09/03/16.
 */
public abstract class BaseCameraController extends BaseEventController<BaseCameraView> implements CameraSurfaceCallback.CameraSurfaceCallbackListener {

    private SurfaceHolder surfaceHolder;
    private CameraSurfaceCallback cameraSurfaceCallback;

    private boolean surfaceActive = false;

    private Camera camera;

    public BaseCameraController(Context context) {
        this(context, null);
    }

    public BaseCameraController(Context context, BaseCameraView baseCameraView) {
        super(context, baseCameraView);
    }

    /*-----------------Class overrides-----------------*/

    @Override
    protected void onViewAttached(BaseCameraView baseCameraView) {
        surfaceHolder = baseCameraView.getSurfaceHolder();

        cameraSurfaceCallback = new CameraSurfaceCallback(camera);
        cameraSurfaceCallback.setListener(this);

        surfaceHolder.addCallback(cameraSurfaceCallback);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Pair<Integer, Integer> size = getSurfaceSize();
        surfaceHolder.setFixedSize(size.first, size.second);

        //In case they dont set us an EventHandler, we do it on our own because we will need to broadcast things internally. If they do then dont mind this
        setEventHandlerListener(new EventManager(getContext()));
    }

    @Override
    public void setEventHandlerListener(EventManager eventManager) {
        super.setEventHandlerListener(eventManager);

        cameraSurfaceCallback.setEventHandler(eventManager);
    }

    @Override
    protected EventNotifierListener getEventNotifierListener() {
        return eventNotifierListener;
    }

    /*-------------------Methods---------------------------------*/

    /**
     * Call for starting the camera
     */
    public void startCamera() {
        camera = Camera.open();
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

        surfaceActive = false;
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

    /**
     * @return Pair<Integer, Integer> with first value as width and second value as height
     */
    protected abstract Pair<Integer, Integer> getSurfaceSize();

    /*--------------------Event Notifier Listener---------------------------*/

    private final EventNotifierListener eventNotifierListener = new EventNotifierListener() {

        @Override
        public void onCameraSurfaceCreated() {
            try {
                //Since the surface is created, set the camera in it
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceVisibilityChanged(boolean visibility) {
            surfaceActive = visibility;
        }
    };

}