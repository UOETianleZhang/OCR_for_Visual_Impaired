package com.example.vizassist.utilities;

import android.graphics.Bitmap;

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
        return null;
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
        return null;
    }

    private static String readStream(InputStream in) {
        return null;
    }
}
