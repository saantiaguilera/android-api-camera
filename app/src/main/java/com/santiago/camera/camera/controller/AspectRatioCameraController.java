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
 * Created by santiago on 21/03/16.
 */
public class AspectRatioCameraController extends BaseCameraController<AspectRatioCameraView> {

    public static final int ASPECT_RATIO_UNDEFINED = -1;

    private static int SCREEN_WIDTH;
    private static int SCREEN_HEIGHT;

    private PictureGeneratedListener pictureGeneratedListener = null;
    private CameraListener cameraListener = null;

    private double aspectRatio;

    private AspectRatioCameraController(Context context) {
        super(context);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        SCREEN_WIDTH = display.getWidth();
        SCREEN_HEIGHT = display.getHeight();
    }

    public void resize(double aspectRatio) {
        validateAspectRatio(aspectRatio);

        setAspectRatio(aspectRatio);

        startCamera();
    }

    public void setAspectRatio(double aspectRatio) {
        if(aspectRatio > (SCREEN_HEIGHT / (double) SCREEN_WIDTH))
            this.aspectRatio = ASPECT_RATIO_UNDEFINED;
        else this.aspectRatio = aspectRatio;
    }

    @Override
    protected void attachPicture(Bitmap bitmap) {
        //Set the picture
        getView().onPictureTaken(bitmap);
        getView().onPictureVisibilityChanged(View.VISIBLE);

        Bitmap newBitmap = bitmap;

        if(aspectRatio != ASPECT_RATIO_UNDEFINED) {
            if (bitmap.getWidth() >= bitmap.getHeight())
                newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) (bitmap.getHeight() / aspectRatio), (int) (bitmap.getHeight() / aspectRatio));
            else
                newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) (bitmap.getWidth() / aspectRatio), (int) (bitmap.getWidth() / aspectRatio));
        }

        //Notify
        onPictureGenerated(newBitmap);
    }

    @Override
    protected void updateSurface(Camera.Parameters parameters) {
        Camera.Size sizeGotten = parameters.getPreviewSize();

        double cameraAspectRatio = sizeGotten.width / (float) sizeGotten.height;

        if(aspectRatio == ASPECT_RATIO_UNDEFINED)
            aspectRatio = cameraAspectRatio;

        updateViewAspectRatio(aspectRatio, cameraAspectRatio);

        super.updateSurface(parameters);
    }

    @Override
    protected void refreshSurface(int width, int height) {
        //If we dont track of the width/height of our surface, we cant do this operation
        if(aspectRatio == ASPECT_RATIO_UNDEFINED || ratioConflicts(aspectRatio))
            super.refreshSurface(width, height);
        else {
            if(width == BaseCameraSurfaceCallback.NO_VALUE)
                return;

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

    private static boolean validateAspectRatio(double aspectRatio) throws IllegalArgumentException {
        if((aspectRatio < 1 || aspectRatio >=2) && aspectRatio != ASPECT_RATIO_UNDEFINED)
            throw new IllegalArgumentException("aspect ratio has to be between 1 (inclusive) and 2 (exclusive) in AspectRatioCameraController.Builder");

        return true;
    }

    private boolean ratioConflicts(double ratio) {
        if(SCREEN_WIDTH * ratio > SCREEN_HEIGHT)
            return true;

        return false;
    }

    private void updateViewAspectRatio(double virtualRatio, double realRatio) {
        if(ratioConflicts(realRatio))
            getView().setAspectRatio(virtualRatio, (SCREEN_HEIGHT / (double) SCREEN_WIDTH ) ,SCREEN_WIDTH);
        else getView().setAspectRatio(virtualRatio, realRatio, SCREEN_WIDTH);
    }

    /*-------------------------------------------------Listener-------------------------------------------------*/

    @Override
    protected void onPictureGenerated(Bitmap bitmap) {
        if(pictureGeneratedListener != null)
            pictureGeneratedListener.onPictureGenerated(bitmap);
    }

    @Override
    public void startCamera() {
        super.startCamera();

        if(cameraListener != null)
            cameraListener.onCameraStart();
    }

    @Override
    public void stopCamera() {
        super.stopCamera();

        if(cameraListener != null)
            cameraListener.onCameraStop();
    }

    public void setPictureGeneratedListener(PictureGeneratedListener listener) {
        this.pictureGeneratedListener = listener;
    }

    public void setCameraListener(CameraListener listener) {
        this.cameraListener = listener;
    }

    /*-----------------------------------------------Inner Classes-----------------------------------------------*/

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
            validateAspectRatio(aspectRatio);

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

    public interface PictureGeneratedListener {
        void onPictureGenerated(Bitmap bitmap);
    }

    public interface CameraListener {
        void onCameraStart();
        void onCameraStop();
    }

}