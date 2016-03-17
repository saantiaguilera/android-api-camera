package com.santiago.camera.camera.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.View;

import com.santiago.camera.camera.utils.picture.BitmapUtils;
import com.santiago.camera.camera.utils.picture.CameraPictureCallback;
import com.santiago.camera.camera.utils.picture.ExifReader;
import com.santiago.camera.camera.utils.surface.CameraSurfaceHandler;
import com.santiago.camera.camera.utils.surface.CameraSurfaceHolder;
import com.santiago.camera.event.camera.OnCameraModifiedEvent;
import com.santiago.camera.event.camera_surface_callback.OnSurfaceCreatedEvent;
import com.santiago.camera.event.camera_surface_callback.OnSurfaceVisibilityChangedEvent;
import com.santiago.camera.manager.CameraManager;
import com.santiago.controllers.BaseEventController;
import com.santiago.event.EventManager;
import com.santiago.event.anotation.EventMethod;

import java.io.IOException;

/**
 * Controller for a really basic camera
 *
 * Created by santiago on 09/03/16.
 */
public abstract class BaseCameraController<T extends View & CameraSurfaceHolder & CameraPictureCallback> extends BaseEventController<T> implements CameraSurfaceHandler.CameraSurfaceCallbackListener {

    private SurfaceHolder surfaceHolder;
    private CameraSurfaceHandler cameraSurfaceHandler;

    private CameraManager cameraManager;

    private boolean surfaceActive = false;

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

        cameraSurfaceHandler = new CameraSurfaceHandler(camera);
        cameraSurfaceHandler.setListener(this);

        //In case they dont set us an EventHandler, we do it on our own because we will need to broadcast things internally. If they do then dont mind this
        setEventHandlerListener(new EventManager(getContext()));

        //As soon as we are setting him a callback, process will start and we will eventually be notified (in the cameraSurfaceHandler class about its creation)
        surfaceHolder.addCallback(cameraSurfaceHandler);
    }

    @Override
    public void setEventHandlerListener(EventManager eventManager) {
        super.setEventHandlerListener(eventManager);

        cameraSurfaceHandler.setEventHandler(eventManager);
    }

    /*----------------------Getters & Setters-------------------------*/

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /*---------------------------Methods---------------------------------*/

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

    public void takePicture() {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //Get the bitmap (dont recycle it since it will delete the byte array and camera still uses it
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                //Modify picture acording to its EXIF data
                ExifReader.CameraExifData exifData = ExifReader.getExifData(data);
                Bitmap newBitmap = BitmapUtils.ChangeBitmapFromExif(bitmap, exifData);

                //Set the picture
                getView().onPictureTaken(newBitmap);
                getView().onPictureVisibilityChanged(View.VISIBLE);

                //Stop the camera since it wont be used while the picture is showing
                stopCamera();

                //Notify
                onPictureGenerated(newBitmap);
            }
        });
    }

    @EventMethod(OnSurfaceVisibilityChangedEvent.class)
    private void onSurfaceVisibilityChanged(OnSurfaceVisibilityChangedEvent event) {
        surfaceActive = event.isSurfaceVisible();
    }

    @EventMethod(OnCameraModifiedEvent.class)
    private void onCameraModified() {
        cameraSurfaceHandler.setCamera(camera);
    }

    /**
     * Call for starting the camera
     */
    @EventMethod(OnSurfaceCreatedEvent.class)
    public void startCamera() {
        //Hide the picture if its visible
        getView().onPictureVisibilityChanged(View.GONE);

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

    protected abstract void onPictureGenerated(Bitmap bitmap);

}