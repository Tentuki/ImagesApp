package com.example.imageapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;


public class ChildActivity extends AppCompatActivity {

    private ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        imageView = findViewById(R.id.own_image);

        Intent intent = getIntent();
        String uri = intent.getStringExtra(Intent.EXTRA_TEXT);

        Picasso.get().load(uri).into(imageView);
    }
}

