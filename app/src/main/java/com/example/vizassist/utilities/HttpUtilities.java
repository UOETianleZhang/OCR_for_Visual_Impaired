package com.example.vizassist.utilities;

import android.graphics.Bitmap;

import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility class with methods to perform http operations
 */
public class HttpUtilities {

    /**
     * Make a {@link HttpURLConnection} that can be used to send a POST request with image data.
     *
     * @param bitmap    image to be sent to server
     * @param urlString URL address of OCR server
     * @return {@link HttpURLConnection} to be used to connect to server
     * @throws IOException
     */
    public static HttpURLConnection makeHttpPostConnectionToUploadImage(Bitmap bitmap,
                                                                        String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");    //可有可无，有的话好处是可以保持这个Connection 接通后一直联通，省时间

        //将图片打包成二进制的字符串
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos); //90表示0.9的quality；
        byte[] data = bos.toByteArray();    //将图片存成byte[]
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(data, ContentType.IMAGE_JPEG); //打包到ByteArrayEntity

        conn.addRequestProperty("Content-length", byteArrayEntity.getContentLength() + "");
        conn.addRequestProperty(byteArrayEntity.getContentType().getName(), //告诉server 我们content的type是什么 <type, >
                byteArrayEntity.getContentType().getValue());

        OutputStream os = conn.getOutputStream();
        byteArrayEntity.writeTo(conn.getOutputStream());    //这里的"写"其实不是写进文件，而是直接发给server (http)
        os.close();
        return conn;
    }

    /**
     * Parse OCR response return by OCR server.
     *
     * @param httpURLConnection @{@link HttpURLConnection} used to connect to OCR server, which
     *                          contains a response JSON if succeeded.
     * @return a string representing text found in the image sent to OCR server
     * @throws JSONException
     * @throws IOException
     */
    public static String parseOCRResponse(HttpURLConnection httpURLConnection) throws JSONException,
            IOException {
        JSONObject resultObject = new JSONObject(readStream(httpURLConnection.getInputStream()));   //此库用来解码Json和打包成Json
        String result = resultObject.getString("text");
        return result;
    }

    private static String readStream(InputStream in) {  //将input stream转换成字符串, 其实是通用函数
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
