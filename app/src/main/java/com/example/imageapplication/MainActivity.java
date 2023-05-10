package com.example.imageapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
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
    private Button next;
    private Button previous;
    public int count = 0;

    @SuppressLint("StaticFieldLeak")
    class QueryTask extends AsyncTask<URL, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(URL... urls) {
            ArrayList<String> arrayStringList = new ArrayList<>();
            try {
                String response = Network.getResponseFromUrl(urls[0]);
                if (response != null) {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray array = jsonResponse.getJSONArray("images_results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String image = info.getString("thumbnail");
                        arrayStringList.add(image);
                    }
                } else {
                    textException = "Не введён запрос";
                    return arrayStringList;
                }
            } catch (UnknownHostException e) {
                textException = "Отсутствует подключение к интернету";
                return arrayStringList;
            } catch (JSONException e) {
                textException = "Введён некорректный запрос";
                return arrayStringList;
            } catch (IOException e) {
                textException = "Не введён запрос";
                return arrayStringList;
            }
            return arrayStringList;
        }

        protected void onPostExecute(ArrayList<String> resultArray) {
            if (resultArray.size() == 0) {
                if (textException.equals("Не введён запрос")) {
                    errorMessage.setText("Произошла ошибка, введите запрос");
                } else if (textException.equals("Отсутствует подключение к интернету")) {
                    errorMessage.setText("Произошла ошибка, попробуйте позже");
                } else if (textException.equals("Введён некорректный запрос")) {
                    errorMessage.setText("Произошла ошибка, попробуйте ввести другой запрос");
                }
                errorMessage.setVisibility(View.VISIBLE);
            } else {
                imagesAdapter = new ImagesAdapter();
                recyclerView.setAdapter(imagesAdapter);
                imagesAdapter.setImagesArray(resultArray);
                recyclerView.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.INVISIBLE);
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
        next = findViewById(R.id.button_next);
        previous = findViewById(R.id.button_previous);

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
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.INVISIBLE);
                count++;
                if (count == 49) {
                    next.setVisibility(View.INVISIBLE);
                }
                URL generatedURL = Network.generateNewURL(count);
                previous.setVisibility(View.VISIBLE);
                new QueryTask().execute(generatedURL);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.INVISIBLE);
                count--;
                if (count == 0) {
                    previous.setVisibility(View.INVISIBLE);
                }
                next.setVisibility(View.VISIBLE);
                URL generatedURL = Network.generateNewURL(count);
                new QueryTask().execute(generatedURL);
            }
        });
    }

    public void performSearch() {
        count = 0;
        recyclerView.setVisibility(View.INVISIBLE);
        URL generatedURL = Network.generateURL(textSearch.getText().toString());
        previous.setVisibility(View.INVISIBLE);
        next.setVisibility(View.VISIBLE);
        new QueryTask().execute(generatedURL);
    }
}