package com.santiago.camera.camera.utils.picture;

import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by santiago on 13/03/16.
 */
public class ExifReader {

    public static int getRotation(byte[] data) {
        //Initialize variables
        int rotation = 0;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + timeStamp + ".jpeg";

        //Since we can only read EXIF metadata from a file, we will have to save the image first
        File pictureFile = new File(fileName);

        //Remove if the file already exists
        if (pictureFile.exists())
            pictureFile.delete();

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);

            //Get exit information and check each case
            ExifInterface exif= new ExifInterface(pictureFile.toString());
            String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);

            if(orientation.equalsIgnoreCase("6"))
                rotation = 90;
            else if(orientation.equalsIgnoreCase("8"))
                rotation = 270;
            else if(orientation.equalsIgnoreCase("3"))
                rotation = 180;
            else if(orientation.equalsIgnoreCase("0"))
                rotation = 90;

            //Close the stream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Remove the created file
        pictureFile.delete();

        return rotation;
    }

}
