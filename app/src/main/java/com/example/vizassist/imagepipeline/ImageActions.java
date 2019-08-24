package com.example.vizassist.imagepipeline;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

/**
 * Actions to get images from either the device back camera or photo gallery
 */
public class ImageActions {

    /**
     * Start the built-in back camera to capture a still image.
     * @param activity origin activity in which the intent will be from.
     * @param requestCode request code to get result when the camera activity is dismissed.
     */
    public static void startCameraActivity(Activity activity, int requestCode) {
    }

    /**
     * Start photo gallery image picker to select a saved image.
     * @param activity origin activity in which the intent will be from.
     * @param requestCode request code to get result when the gallery activity is dismissed.
     */
    public static void startGalleryActivity(Activity activity, int requestCode) {
    }
}
