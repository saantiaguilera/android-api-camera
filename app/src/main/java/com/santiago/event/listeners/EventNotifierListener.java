package com.santiago.event.listeners;

import com.santiago.camera.manager.type.CameraType;

/**
 * Class that holds all the methods that can be notified about
 * @see com.santiago.event.Event
 *
 * Created by santiaguilera@theamalgama.com on 01/03/16.
 */
public abstract class EventNotifierListener {

    //Camera surface callback
    public void onCameraSurfaceCreated() {};
    public void onSurfaceVisibilityChanged(boolean visibility) {};

    //Camera
    public void onCameraModified() {};

    //Mocks
    public void mockFlashChange(int flashMode) {};
    public void mockShootChange(int cameraMode) {};
    public void mockSwitchCameraChange(CameraType cameraType) {};

}
