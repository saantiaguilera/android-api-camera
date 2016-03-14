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

    public static CameraExifData getExifData(byte[] data) {
        //Initialize variables
        int rotation = 0;
        Flip flip = Flip.NORMAL;
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

            switch(Integer.valueOf(orientation)) {
                case ExifInterface.ORIENTATION_UNDEFINED:
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
            }

            switch(Integer.valueOf(orientation)) {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    flip = Flip.HORIZONTAL;
                    break;

                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    flip = Flip.VERTICAL;
                    break;
            }

            //Close the stream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Remove the created file
        pictureFile.delete();

        return new CameraExifData(rotation, flip);
    }

    public static class CameraExifData {

        private int rotation;
        private Flip flip;

        public CameraExifData(int rotation, Flip flip) {
            this.rotation = rotation;
            this.flip = flip;
        }

        public Flip getFlip() {
            return flip;
        }

        public int getRotation() {
            return rotation;
        }

    }

    public enum Flip {

        NORMAL(Flip.NORMAL_INDEX),
        HORIZONTAL(Flip.HORIZONTAL_INDEX),
        VERTICAL(Flip.VERTICAL_INDEX);

        public static final int NORMAL_INDEX = 0;
        public static final int HORIZONTAL_INDEX = 1;
        public static final int VERTICAL_INDEX = 2;

        private final int index;

        Flip(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

    }

}
