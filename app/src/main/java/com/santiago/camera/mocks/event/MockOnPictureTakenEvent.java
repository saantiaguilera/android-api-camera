package com.santiago.camera.mocks.event;

import android.graphics.Bitmap;

import com.santiago.event.Event;

/**
 * Created by santiago on 17/03/16.
 */
public class MockOnPictureTakenEvent extends Event {

    private Bitmap bitmap;

    public MockOnPictureTakenEvent(Bitmap bmp) {
        this.bitmap = bmp;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
