package com.example.imageapplication;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Network {
    private static final String BASE_URL = "https://serpapi.com/search.json";
    private static final String PARAM_ENGINE = "engine";
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_TEXT = "text";
    private static final String PARAM_PAGE = "p";
    private static final String API_KEY = "e55703c66330b8e5b24d1f4fee6fd30ac44ce6aa82643e6500ca26a27491b95a";
    private static String searches;

    public static URL generateURL(String search) {
        searches = search;
        Uri biultUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(PARAM_ENGINE, "yandex_images")
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_PAGE, "0")
                .appendQueryParameter(PARAM_TEXT, search)
                .build();

        URL url = null;
        try {
            url = new URL(biultUri.toString());
        } catch (Exception ignored) {

        }
        return url;
    }

    public static URL generateNewURL(int count) {
        Uri biultUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(PARAM_ENGINE, "yandex_images")
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_PAGE, String.valueOf(count))
                .appendQueryParameter(PARAM_TEXT, searches)
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
}