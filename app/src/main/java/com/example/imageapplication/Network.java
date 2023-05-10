package com.example.imageapplication;

import android.net.Uri;
import android.util.Log;

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
    private static String searches;

    public static URL generateURL(String search) {
        searches = search;
        Uri biultUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(PARAM_ENGINE, "yandex_images")
                .appendQueryParameter(PARAM_API_KEY, "75761d4dbf6b14beadb2877122cf96c0b5ef8812f533ccd5c00fd3d3d195a9c4")
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
                .appendQueryParameter(PARAM_API_KEY, "75761d4dbf6b14beadb2877122cf96c0b5ef8812f533ccd5c00fd3d3d195a9c4")
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