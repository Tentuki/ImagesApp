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
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText textSearch;
    private Button buttonSearch;
    private RecyclerView recyclerView;
    private ImagesAdapter imagesAdapter;
    private ProgressBar progressBar;
    private TextView errorMessage;

    class QueryTask extends AsyncTask<URL, Void, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(URL... urls) {
            ArrayList<Bitmap> bmArray = new ArrayList<>();
            try {
                String response = Network.getResponseFromUrl(urls[0]);
                assert response != null;
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray array = jsonResponse.getJSONArray("images_results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject info = array.getJSONObject(i);
                    String image = info.getString("thumbnail");
                    Bitmap bitmap = Network.downloadImage(image);
                    bmArray.add(bitmap);
                }
            } catch (Exception e) {
                return bmArray;
            }
            return bmArray;
        }

        protected void onPostExecute(ArrayList<Bitmap> resultArray) {
            if (resultArray.size() != 0) {
                imagesAdapter = new ImagesAdapter();
                recyclerView.setAdapter(imagesAdapter);
                imagesAdapter.setImagesArray(resultArray);
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                errorMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
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