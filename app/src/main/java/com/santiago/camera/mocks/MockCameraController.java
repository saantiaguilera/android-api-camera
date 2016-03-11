package com.santiago.camera.mocks;

import android.content.Context;
import android.hardware.Camera;

import com.santiago.camera.camera.controller.BaseCameraController;
import com.santiago.camera.camera.view.BaseCameraView;

import java.util.List;

/**
 * Created by santiago on 10/03/16.
 */
public class MockCameraController extends BaseCameraController<BaseCameraView> {

    public MockCameraController(Context context) {
        super(context);
    }

    public MockCameraController(Context context, BaseCameraView baseCameraView) {
        super(context, baseCameraView);
    }



    @Override
    public Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double) height / width;

        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

        if (parameters.getSupportedPreviewSizes() == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
