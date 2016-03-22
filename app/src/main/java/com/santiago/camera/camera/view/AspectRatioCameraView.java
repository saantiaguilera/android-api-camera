package com.santiago.camera.camera.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.santiago.camera.R;
import com.santiago.camera.camera.controller.AspectRatioCameraController;
import com.santiago.camera.camera.utils.picture.CameraPictureCallback;
import com.santiago.camera.camera.utils.surface.CameraSurfaceHolder;

/**
 * Created by santiago on 21/03/16.
 */
public class AspectRatioCameraView extends FrameLayout implements CameraSurfaceHolder, CameraPictureCallback {

    private SurfaceView surfaceView;
    private ImageView pictureView;
    private FrameLayout cameraContainer;
    private View blockView;

    public AspectRatioCameraView (Context context) {
        this(context, null);
    }

    public AspectRatioCameraView (Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.view_squared_camera, this);

        surfaceView = (SurfaceView) findViewById(R.id.view_aspect_ratio_camera_surface_view);
        pictureView = (ImageView) findViewById(R.id.view_aspect_ratio_camera_image_view);
        cameraContainer = (FrameLayout) findViewById(R.id.view_aspect_ratio_camera_container);
        blockView = findViewById(R.id.view_aspect_ratio_block_view);
    }

    @Override
    public SurfaceHolder getSurfaceHolder() {
        return surfaceView.getHolder();
    }

    @Override
    public void onPictureTaken(@NonNull Bitmap picture) {
        pictureView.setImageBitmap(picture);
    }

    public void setAspectRatio(double virtualRatio, double realRatio, int screenWidth) {
        FrameLayout.LayoutParams containerParams;

        if (virtualRatio == AspectRatioCameraController.ASPECT_RATIO_UNDEFINED) {
            containerParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            blockView.setVisibility(View.GONE);
        } else {
            containerParams = new FrameLayout.LayoutParams(screenWidth, (int) (screenWidth * realRatio));

            FrameLayout.LayoutParams blackBorderParams = new FrameLayout.LayoutParams(screenWidth, (int) (screenWidth * (realRatio - virtualRatio)));
            blackBorderParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            blockView.setVisibility(View.VISIBLE);
            blockView.setLayoutParams(blackBorderParams);
        }

        containerParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        cameraContainer.setLayoutParams(containerParams);
    }

    @Override
    public void onPictureVisibilityChanged(int visibility) {
        pictureView.setVisibility(visibility);
        surfaceView.setVisibility(visibility == VISIBLE ? INVISIBLE : VISIBLE);
    }

}