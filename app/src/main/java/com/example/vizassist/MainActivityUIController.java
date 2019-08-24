package com.example.vizassist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Controller of main activity.
 */
public class MainActivityUIController {
    private final Activity activity;
    private final Handler mainThreadHandler;

    private TextView resultView;
    private ImageView imageView;

    public MainActivityUIController(Activity activity) {
        this.activity = activity;
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public void resume() {
    }

    public void updateResultView(final String text) {
    }

    public void updateImageViewWithBitmap(Bitmap bitmap) {
    }

    public void showErrorDialogWithMessage(int messageStringID) {
    }

    public void showInternetError() {
    }

    public void askForPermission(String permission, Integer requestCode) {
    }
}
