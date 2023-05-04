package com.example.imageapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ChildActivity extends AppCompatActivity {

    private ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        imageView = findViewById(R.id.own_image);

        Bundle bundle = getIntent().getExtras();
        Bitmap bmp = bundle.getParcelable("imageBitmap");
        imageView.setImageBitmap(bmp);
    }
}

