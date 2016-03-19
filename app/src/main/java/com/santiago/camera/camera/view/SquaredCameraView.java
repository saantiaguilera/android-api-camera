package com.santiago.camera.camera.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.santiago.camera.R;
import com.santiago.camera.camera.utils.picture.CameraPictureCallback;
import com.santiago.camera.camera.utils.surface.CameraSurfaceHolder;

/**
 * Created by santiago on 19/03/16.
 */
public class SquaredCameraView extends FrameLayout implements CameraSurfaceHolder, CameraPictureCallback {

    private static final double BEST_RATIO_TOLERANCE = 4 / 3d;

    private SurfaceView surfaceView;
    private ImageView pictureView;

    public SquaredCameraView(Context context) {
        this(context, null);
    }

    public SquaredCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.view_squared_camera, this);

        surfaceView = (SurfaceView) findViewById(R.id.view_squared_camera_surface_view);
        pictureView = (ImageView) findViewById(R.id.view_squared_camera_image_view);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(display.getWidth(), (int) (display.getWidth() * BEST_RATIO_TOLERANCE));
        containerParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        findViewById(R.id.mock_view_squared_camera_container).setLayoutParams(containerParams);

        FrameLayout.LayoutParams blackBorderParams = new FrameLayout.LayoutParams(display.getWidth(), (int) (display.getWidth() * (BEST_RATIO_TOLERANCE - 1)));
        blackBorderParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        findViewById(R.id.mock_view_squared_camera_squared_banner_for_hiding_bottom).setLayoutParams(blackBorderParams);
    }

    @Override
    public SurfaceHolder getSurfaceHolder() {
        return surfaceView.getHolder();
    }

    @Override
    public void onPictureTaken(@NonNull Bitmap picture) {
        pictureView.setImageBitmap(picture);
    }

    @Override
    public void onPictureVisibilityChanged(int visibility) {
        pictureView.setVisibility(visibility);
        surfaceView.setVisibility(visibility == VISIBLE ? INVISIBLE : VISIBLE);
    }

}