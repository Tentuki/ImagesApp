package com.example.imageapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;


public class ChildActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button share;
    private Button load;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        imageView = findViewById(R.id.own_image);
        share = findViewById(R.id.button_share);
        load = findViewById(R.id.button_load);

        Intent intent = getIntent();
        String imageUri = intent.getStringExtra(Intent.EXTRA_TEXT);

        Picasso.get().load(imageUri).into(imageView);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, imageUri);
                i = Intent.createChooser(i, "Share via");
                startActivity(i);
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

