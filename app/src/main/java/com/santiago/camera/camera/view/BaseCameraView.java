package com.santiago.camera.camera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.santiago.camera.R;
import com.santiago.camera.camera.utils.CameraSurfaceHolder;

/**
 * View representing the basic camera
 *
 * Created by santiago on 09/03/16.
 */
public class BaseCameraView extends FrameLayout implements CameraSurfaceHolder {

    private SurfaceView surfaceView;

    public BaseCameraView(Context context) {
        this(context, null);
    }

    public BaseCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.view_base_camera, this);

        surfaceView = (SurfaceView) findViewById(R.id.view_base_camera_surface_view);
    }

    @Override
    public SurfaceHolder getSurfaceHolder() {
        return surfaceView.getHolder();
    }

}