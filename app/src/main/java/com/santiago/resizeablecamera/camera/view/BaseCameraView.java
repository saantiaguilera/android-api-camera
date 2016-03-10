package com.santiago.resizeablecamera.camera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.santiago.resizeablecamera.R;
import com.santiago.resizeablecamera.camera.utils.CameraSurfaceCallback;

/**
 * View representing the basic camera
 *
 * Created by santiago on 09/03/16.
 */
public abstract class BaseCameraView extends FrameLayout implements CameraSurfaceCallback.CameraSurfaceCallbackListener {

    private SurfaceView surfaceView;

    public BaseCameraView(Context context) {
        this(context, null);
    }

    public BaseCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.view_base_camera, this);

        surfaceView = (SurfaceView) findViewById(R.id.view_base_camera_surface_view);
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceView.getHolder();
    }

}