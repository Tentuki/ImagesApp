package com.example.imageapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText textSearch;
    private Button buttonSearch;
    private RecyclerView recyclerView;
    private ImagesAdapter imagesAdapter;
    private ProgressBar progressBar;
    private TextView errorMessage;
    private String textException = "";

    @SuppressLint("StaticFieldLeak")
    class QueryTask extends AsyncTask<URL, Void, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(URL... urls) {
            ArrayList<Bitmap> bmArray = new ArrayList<>();
            try {
                String response = Network.getResponseFromUrl(urls[0]);
                if (response != null) {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray array = jsonResponse.getJSONArray("images_results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String image = info.getString("thumbnail");
                        Bitmap bitmap = Network.downloadImage(image);
                        bmArray.add(bitmap);
                    }
                } else {
                    textException = "Не введён запрос";
                    return bmArray;
                }
            } catch (UnknownHostException e) {
                textException = "Отсутствует подключение к интернету";
                return bmArray;
            } catch (JSONException e) {
                textException = "Введён некорректный запрос";
                return bmArray;
            } catch (IOException e) {
                textException = "Не введён запрос";
                return bmArray;
            }
            return bmArray;
        }

        protected void onPostExecute(ArrayList<Bitmap> resultArray) {
            if (resultArray.size() == 0) {
                if (textException.equals("Не введён запрос")) {
                    errorMessage.setText("Произошла ошибка, введите запрос");
                } else if (textException.equals("Отсутствует подключение к интернету")) {
                    errorMessage.setText("Произошла ошибка, попробуйте позже");
                } else if (textException.equals("Введён некорректный запрос")) {
                    errorMessage.setText("Произошла ошибка, попробуйте ввести другой запрос");
                }
                errorMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                imagesAdapter = new ImagesAdapter();
                recyclerView.setAdapter(imagesAdapter);
                imagesAdapter.setImagesArray(resultArray);
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {
            errorMessage.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSearch = findViewById(R.id.et_search);
        buttonSearch = findViewById(R.id.b_search);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        errorMessage = findViewById(R.id.error_message);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setHasFixedSize(true);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    errorMessage.setText("Произошла ошибка, введите запрос");
                    errorMessage.setVisibility(View.VISIBLE);
                }
                performSearch();
            }
        });
        textSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    InputMethodManager inputManager = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
    }

    public void performSearch() {
        recyclerView.setVisibility(View.INVISIBLE);
        URL generatedURL = Network.generateURL(textSearch.getText().toString());
        new QueryTask().execute(generatedURL);
    }
}