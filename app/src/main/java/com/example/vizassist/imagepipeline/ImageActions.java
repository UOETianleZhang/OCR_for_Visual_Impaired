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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, requestCode);   //如果用startActivityForResult(), activity2运行完会能传回来
    }

    /**
     * Start photo gallery image picker to select a saved image.
     * @param activity origin activity in which the intent will be from.
     * @param requestCode request code to get result when the gallery activity is dismissed.
     */
    public static void startGalleryActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);  //告诉android SDK我们要一个pick img的activity, 最后把代表文件位置的信息传回来
        activity.startActivityForResult(intent, requestCode);
    }
}
