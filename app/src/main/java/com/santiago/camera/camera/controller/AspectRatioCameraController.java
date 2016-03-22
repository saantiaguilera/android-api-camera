package com.santiago.camera.camera.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.santiago.camera.camera.view.AspectRatioCameraView;
import com.santiago.camera.configs.CameraConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @note: Im not having too much time to develop this, so I did it in a full run of 3 hours long. Its 100% sure this needs a good refactor
 * Like first of all im not too happy that the user has to do all that shit in the MockGenericCameraController
 * But if we do an abstract implementation of this, and the user has to create one for it, im not to sure if it would be a correct approach
 * Since this should be a final class. You implement this. This works like its done, no abstract methods or child delegations needed.
 *
 * Created by santiago on 21/03/16.
 */
public class AspectRatioCameraController extends BaseCameraController<AspectRatioCameraView> {

    public static final int ASPECT_RATIO_UNDEFINED = -1; // Value for a fullscreen camera

    //Holds a reference to the mobile screen dimens
    private static int SCREEN_WIDTH;
    private static int SCREEN_HEIGHT;

    //Possible interfaces listening
    private PictureGeneratedListener pictureGeneratedListener = null;
    private CameraListener cameraListener = null;

    //Desired aspect ratio
    private double aspectRatio;

    /**
     * @Private constructor.
     * Create an instance via the @Builder class
     * @note: Its done this way to avoid pre init problems of some values
     * @param context
     */
    private AspectRatioCameraController(Context context) {
        super(context);

        //Set the mobile dimens
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        SCREEN_WIDTH = display.getWidth();
        SCREEN_HEIGHT = display.getHeight();
    }

    /**
     * @Method for resizing the camera
     * @param aspectRatio
     */
    public void resize(double aspectRatio) {
        //Set the new aspect ratio and start the camera again (draw it again)
        setAspectRatio(aspectRatio);
        startCamera();
    }

    /**
     * @Private method for setting an aspect ratio. If the ratio is higher than the mobile possible boundaries, we set it to @see ASPECT_RATIO_UNDEFINED
     * If you want to set the ratio from outside, call resize which will draw the new camera
     * @note: This validates the aspectRatio prior to set it. If its not a correct one its set as ASPECT_RATIO_UNDEFINED
     * @param aspectRatio value between 1 (inclusive) and 2 (exclusive) eg: 4:3 screen = 1.33d
     */
    private void setAspectRatio(double aspectRatio) {
        //In case the aspect ratio is lower than 1 (be careful with our constant) or is higher than our mobile boundaries, set it to undefined. Else normal setter.
        if((aspectRatio < 1 && aspectRatio != ASPECT_RATIO_UNDEFINED) || aspectRatio > (SCREEN_HEIGHT / (double) SCREEN_WIDTH))
            this.aspectRatio = ASPECT_RATIO_UNDEFINED;
        else this.aspectRatio = aspectRatio;
    }

    /**
     * @Overriden attachPicture so that we crop the image depending on the aspectRatio we have.
     * Everything else is as super.attachPicture(bitmap);
     * @param bitmap
     */
    @Override
    protected void attachPicture(Bitmap bitmap) {
        //Set the picture
        getView().onPictureTaken(bitmap);
        getView().onPictureVisibilityChanged(View.VISIBLE);

        Bitmap newBitmap = bitmap;

        /**
         * If the aspectRatio is defined, crop the image (from top, since our view has top gravity with this purpose)
         * considering it. This way it will look the same as the image.
         * If you want a different gravity, in the previous branch I did a PictureCropper that considered the gravity.
         * But since this camera will be always top (I dont have time for doing it generic for layout gravity too)
         * Defaults to top
         * @note: Refactor. I think the if is useless since aspectRatio will change value to the best ratio found. So it will always enter here.
         */
        if(aspectRatio != ASPECT_RATIO_UNDEFINED) {
            if (bitmap.getWidth() >= bitmap.getHeight())
                newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) (bitmap.getHeight() / aspectRatio), (int) (bitmap.getHeight() / aspectRatio));
            else
                newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) (bitmap.getWidth() / aspectRatio), (int) (bitmap.getWidth() / aspectRatio));
        }

        //Notify
        onPictureGenerated(newBitmap);
    }

    /**
     * @Overriden updateSurface.
     * Before letting the surface update with the new sizes, compare the previewSize with our aspectRatio
     * @note: If our aspectRatio is ASPECT_RATIO_UNDEFINED it will adapt to the best parameter obtained (this means it wont be MATCH_PARENT, MATCH_PARENT)
     * @param parameters
     */
    @Override
    protected void updateSurface(Camera.Parameters parameters) {
        Camera.Size sizeGotten = parameters.getPreviewSize();

        /**
         * This value represents the REAL surfaceview size. Note that we hide a part of it, so the user has the same aspectratio he prompted
         * But, as camera.sizes are independent, sometimes that ratio wont exist, so we get the closest one, and simulate it (hiding parts of the surfaceview)
         * So:
         * @param cameraAspectRatio represents the whole surfaceView. Lets call it realAspectRatio
         * @param aspectRatio represents the virtual surfaceView. The one the user sees and interacts with. Lets call it virtualAspectRatio
         */

        double cameraAspectRatio = sizeGotten.width / (float) sizeGotten.height;

        /**
         * If ratio was undefined, set it to the camera one to avoid a bad image
         * @note: I think I have read somewhere that you can do a "not fixed size" surface view. So it will itself resize to the camera.size
         * Maybe look for it and implement it IN THIS PARTICULAR CASE (that we wont to adapt to the best camera.size)
         */
        if(aspectRatio == ASPECT_RATIO_UNDEFINED)
            aspectRatio = cameraAspectRatio;

        //TODO Refactor!! Set it. I pass SCREEN_WIDTH since it will be used as max width possible. Shouldnt it use the layout_width of the view itself ? Since we dont know if all cases will be SCREEN_WIDTH as layout_width
        getView().setAspectRatio(aspectRatio, cameraAspectRatio, SCREEN_WIDTH);

        //Call super and let it set
        super.updateSurface(parameters);
    }

    /**
     * @Overriden method from superclass. If we have undefined aspect ratio or it has conflicts let the superclass handle it
     * Else get the "suppossed" height we should have and find what fits best for it
     *
     * @note: should refactor this ?
     * @param width
     * @param height
     */
    @Override
    protected void refreshSurface(int width, int height) {
        //If we dont track of the width/height of our surface, we cant do this operation
        if(aspectRatio == ASPECT_RATIO_UNDEFINED)
            super.refreshSurface(width, height);
        else {
            if(width == BaseCameraSurfaceCallback.NO_VALUE)
                return;

            //We will use our own height. It should be w*r. So lets try to find the best approximation with this
            int newHeight = (int) (width * aspectRatio);

            //Data will be using
            Camera.Parameters parameters = getCamera().getParameters();
            Camera.Size previewSize;
            Camera.Size pictureSize;

            //Get the best size for this surface and if exists, set it and calculate the one for the picture (with the ratio setted for the preview)
            previewSize = approximateToBestCameraSize(width, newHeight, parameters.getSupportedPreviewSizes());

            if(previewSize!=null) {
                parameters.setPreviewSize(previewSize.width, previewSize.height);

                //Get the best picture size for this surface, in relation with the setted preview size and set it
                pictureSize = approximateToBestCameraSize(previewSize.height, previewSize.width, parameters.getSupportedPictureSizes());
                if (pictureSize != null)
                    parameters.setPictureSize(pictureSize.width, pictureSize.height);
            }

            updateSurface(parameters);
        }
    }

    /*-------------------------------------------------Listener-------------------------------------------------*/

    /**
     * @note: <strong>I dont like this. Specially the start and stop camera. Refactor ASAP</strong>
     */

    /**
     * Notify about a picture generated to the listener
     * @param bitmap
     */
    @Override
    protected void onPictureGenerated(Bitmap bitmap) {
        if(pictureGeneratedListener != null)
            pictureGeneratedListener.onPictureGenerated(bitmap);
    }

    /**
     * Notify about camera starting to our listener
     */
    @Override
    public void startCamera() {
        super.startCamera();

        if(cameraListener != null)
            cameraListener.onCameraStart();
    }

    /**
     * Notify about camera stopping to our listener
     */
    @Override
    public void stopCamera() {
        super.stopCamera();

        if(cameraListener != null)
            cameraListener.onCameraStop();
    }

    /*-------------------------------------------Listener setters---------------------------------------------------------*/

    public void setPictureGeneratedListener(PictureGeneratedListener listener) {
        this.pictureGeneratedListener = listener;
    }

    public void setCameraListener(CameraListener listener) {
        this.cameraListener = listener;
    }

    /*-----------------------------------------------Inner Classes-----------------------------------------------*/

    /**
     * Builder class for creating instances of this
     * Wont explain much, its a common builder
     * What you can do:
     *  - Set an initial aspect ratio
     *  - Listen to picture generation events
     *  - Listen to camera events (start & stop)
     *  - Add n amount of configurations to the camera
     *  - Set the view to attach
     * @note: If no view attached it will use one by default
     * @note: If no configurations added wont have any ?
     * @note: If no aspect ratio defined, default will be @param ASPECT_RATIO_UNDEFINED
     */
    public static class Builder {

        private double aspectRatio = ASPECT_RATIO_UNDEFINED;
        private Context context;
        private PictureGeneratedListener pictureListener = null;
        private CameraListener cameraListener = null;
        private List<CameraConfiguration> configurations;
        private AspectRatioCameraView view;

        public Builder(Context context) {
            this.context = context;
            configurations = new ArrayList<>();
        }

        public Builder setAspectRatio(double aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public Builder setPictureGeneratedListener(PictureGeneratedListener pictureListener) {
            this.pictureListener = pictureListener;
            return this;
        }

        public Builder setCameraListener(CameraListener cameraListener) {
            this.cameraListener = cameraListener;
            return this;
        }

        public Builder addConfiguration(CameraConfiguration configuration) {
            configurations.add(configuration);
            return this;
        }

        public Builder setViewToAttach(AspectRatioCameraView view) {
            this.view = view;
            return this;
        }

        public AspectRatioCameraController build() {
            AspectRatioCameraController controller = new AspectRatioCameraController(context);
            controller.setAspectRatio(aspectRatio);

            if(pictureListener != null)
                controller.setPictureGeneratedListener(pictureListener);

            if(cameraListener != null)
                controller.setCameraListener(cameraListener);

            for(CameraConfiguration configuration : configurations)
                controller.getCameraManager().addConfiguration(configuration);

            controller.attachView(view == null ? new AspectRatioCameraView(context) : view);

            return controller;
        }

    }

    /**
     * Interface for the picture generation events
     */
    public interface PictureGeneratedListener {
        void onPictureGenerated(Bitmap bitmap);
    }

    /**
     * Interface for the camera events
     */
    public interface CameraListener {
        void onCameraStart();
        void onCameraStop();
    }

}