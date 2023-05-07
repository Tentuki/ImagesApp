package com.example.imageapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Network {
    private static final String BASE_URL = "https://serpapi.com/search.json";
    private static final String PARAM_ENGINE = "engine";
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_TEXT = "text";

    public static URL generateURL(String search) {
        Uri biultUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(PARAM_ENGINE, "yandex_images")
                .appendQueryParameter(PARAM_API_KEY, "75761d4dbf6b14beadb2877122cf96c0b5ef8812f533ccd5c00fd3d3d195a9c4")
                .appendQueryParameter(PARAM_TEXT, search)
                .build();

        URL url = null;
        try {
            url = new URL(biultUri.toString());
        } catch (Exception ignored) {

        }
        return url;
    }

    public static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Bitmap downloadImage(String url) {
        Bitmap bm = null;
        try {
            InputStream is = new URL(url).openStream();
            bm = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
        }
        return bm;
    }
}