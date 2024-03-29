package com.example.vizassist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vizassist.imagepipeline.ImageActions;
import com.example.vizassist.utilities.HttpUtilities;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    private static final String UPLOAD_HTTP_URL = "http://173.255.117.247:8080/vizassist/annotate";

    private static final int IMAGE_CAPTURE_CODE = 1;
    private static final int SELECT_IMAGE_CODE = 2;


    private static final int CAMERA_PERMISSION_REQUEST = 1001;

    private MainActivityUIController mainActivityUIController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityUIController = new MainActivityUIController(this);  //set up
    }

    @Override
    public void onResume() {    //大部分的前台逻辑从这里开始
        super.onResume();
        mainActivityUIController.resume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_capture:
                mainActivityUIController.updateResultView(getString(R.string.result_placeholder));  //替换之前的View? 更新ResultView
                if (ContextCompat.checkSelfPermission(MainActivity.this,    //没有权限的话问用户要权限. ContextCompat与状态有关的工具类
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    mainActivityUIController.askForPermission(
                            Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST);
                } else {
                    ImageActions.startCameraActivity(this, IMAGE_CAPTURE_CODE);
                }
                break;
            case R.id.action_gallery:
                mainActivityUIController.updateResultView(getString(R.string.result_placeholder));
                ImageActions.startGalleryActivity(this, SELECT_IMAGE_CODE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);  //xml是抽象的, inflate之后就变成了菜单栏实体，所以用inflate
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            if (resultCode == IMAGE_CAPTURE_CODE) {
                bitmap = (Bitmap) data.getExtras().get("data");
                mainActivityUIController.updateImageViewWithBitmap(bitmap);
            } else if (requestCode == SELECT_IMAGE_CODE) {
                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    mainActivityUIController.updateImageViewWithBitmap(bitmap);
                } catch (IOException e) {
                    mainActivityUIController.showErrorDialogWithMessage(
                            R.string.reading_error_message);
                }
            }

            //
            if (bitmap != null) {
                final Bitmap bitmapToUpload = bitmap;   //inner class 要用的必须是final, 否则线程打包出去以后会有安全问题
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadImage(bitmapToUpload);    //上传给Server图片，需要用Post request (post不能刷新，否则表单和文件可能会被上传两次)
                    }
                });
                thread.start();
            }
        }


    }

    private void uploadImage(Bitmap bitmap) {
        try {
            HttpURLConnection conn = HttpUtilities.makeHttpPostConnectionToUploadImage(bitmap, UPLOAD_HTTP_URL);    //看看能不能连
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {  //能连的话解析
                mainActivityUIController.updateResultView(HttpUtilities.parseOCRResponse(conn));
            } else {
                mainActivityUIController.showInternetError();   //报错, 弹对话框 (网络错误啥的)
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            mainActivityUIController.showInternetError();
        } catch (IOException e) {
            e.printStackTrace();
            mainActivityUIController.showInternetError();
        } catch (JSONException e) {
            e.printStackTrace();
            mainActivityUIController.showInternetError();
        }
    }

}