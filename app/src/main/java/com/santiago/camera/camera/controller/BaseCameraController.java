package com.santiago.camera.camera.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.View;

import com.santiago.camera.camera.utils.picture.CameraPictureCallback;
import com.santiago.camera.camera.utils.picture.CameraPictureUtilities;
import com.santiago.camera.camera.utils.surface.CameraSurfaceHolder;
import com.santiago.camera.manager.CameraManager;
import com.santiago.camera.manager.orientation.CameraOrientationManager;
import com.santiago.controllers.BaseEventController;
import com.santiago.event.EventManager;

import java.io.IOException;

/**
 * Controller for a really basic camera
 *
 * Created by santiago on 09/03/16.
 */
public abstract class BaseCameraController<T extends View & CameraSurfaceHolder & CameraPictureCallback> extends BaseEventController<T> implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;

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

        //In case they dont set us an EventHandler, we do it on our own because we will need to broadcast things internally. If they do then dont mind this
        setEventHandlerListener(new EventManager(getContext()));

        //As soon as we are setting him a callback, process will start and we will eventually be notified (in the cameraSurfaceHandler class about its creation)
        surfaceHolder.addCallback(this);
    }

    /*----------------------Getters & Setters-------------------------*/

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     * If the camera is showing or not
     * @return
     */
    public boolean isCameraActive() { return surfaceActive; }

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

    /*---------------------------Methods---------------------------------*/

    /**
     * Take a picture and show it in the view
     */
    public void takePicture() {
        cameraManager.prepareForPicture();

        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //Get the bitmap (dont recycle it since it will delete the byte array and camera still uses it
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                boolean isFrontCamera = cameraManager.getCameraTypeManager().getCurrentCamera().getCameraType()==Camera.CameraInfo.CAMERA_FACING_FRONT;
                CameraOrientationManager orientationManager = cameraManager.getCameraOrientationManager();

                int rotation = CameraPictureUtilities.getRotation(orientationManager.getDisplayOrientation(), orientationManager.getNormalOrientation(), orientationManager.getLayoutOrientation(), isFrontCamera);

                bitmap = CameraPictureUtilities.rotatePicture(getContext(), rotation, bitmap);

                if(isFrontCamera)
                    bitmap = CameraPictureUtilities.mirrorImage(bitmap);

                //Set the picture
                getView().onPictureTaken(bitmap);
                getView().onPictureVisibilityChanged(View.VISIBLE);

                //Stop the camera since it wont be used while the picture is showing
                stopCamera();

                //Notify
                onPictureGenerated(bitmap);
            }
        });
    }

    /**
     * Call for starting the camera
     */
    public void startCamera() {
        //Hide the picture if its visible
        getView().onPictureVisibilityChanged(View.GONE);

        //If the camera is already running, stop it
        if(camera!=null)
            stopCamera();

        camera = cameraManager.createNewCamera();

        //Set the preview display again
        setPreviewDisplay();

        camera.startPreview();
    }

    /**
     * Call for stopping the camera
     * <strong> When taking a picture and showing it its higly recommended to stop the camera to free memory </strong>
     * <strong> Always call this when you finished using the camera to make it available to others </strong>
     */
    public void stopCamera() {
        if(camera==null)
            return;

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
    protected abstract Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters);

    protected abstract void onPictureGenerated(Bitmap bitmap);

    /*----------------------Surface Callbacks------------------------*/

    /**
     * Called when the surface has being created
     * @param holder
     */
    public void surfaceCreated(SurfaceHolder holder) {
        //Start the camera when the surface is created
        startCamera();
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

        //Notify them about what size they want us to use
        size = getBestPreviewSize(width, height, parameters);

        //If size has being set, apply it to our camera and broadcast a change of visibility
        if (size != null) {
            parameters.setPreviewSize(size.width, size.height);
            camera.setParameters(parameters);
            camera.startPreview();

            surfaceActive = true;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) { }

}