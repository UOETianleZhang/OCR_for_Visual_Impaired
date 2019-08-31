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
/*
    view: 面向用户 (照相按钮)
    controller: 后台的类 (照相)
    model: model会verify the response from the controller, 然后 (判断是不是合理完整的照片)
 */
public class MainActivityUIController {
    private final Activity activity;
    private final Handler mainThreadHandler;

    private TextView resultView;
    private ImageView imageView;

    public MainActivityUIController(Activity activity) {
        this.activity = activity;
        this.mainThreadHandler = new Handler(Looper.getMainLooper());   //Looper.getMainLooper()即主线程的looper
    }

    public void resume() {
        //就初始化一下
        resultView = activity.findViewById(R.id.resultView);
        //这里少了一句，记得看课件加上
        imageView = activity.findViewById(R.id.capturedImage);
    }

    public void updateResultView(final String text) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                resultView.setText(text);
            }
        });
    }

    public void updateImageViewWithBitmap(Bitmap bitmap) {
        //
        imageView.setImageBitmap(bitmap);
    }

    public void showErrorDialogWithMessage(int messageStringID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.error_dialog_title);
        builder.setMessage(messageStringID);
        builder.setPositiveButton(R.string.error_dialog_dismiss_button,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    public void showInternetError() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, R.string.internet_error_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {

            //Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {    //判断现在
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again...
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                //bonus: 可以弹一个窗说明一下为什么需要这个权限
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
            //不管是不是第一次要还是第二次，都问用户要权限 (如果deny, 第二次点照相还是会问权限)
        }
    }
}
