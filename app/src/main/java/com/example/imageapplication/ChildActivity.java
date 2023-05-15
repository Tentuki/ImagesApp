package com.example.imageapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.squareup.picasso.Picasso;

import java.io.File;
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
                saveToGallery();
            }
        });
    }

    private void saveToGallery() {
        ActivityCompat.requestPermissions(ChildActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(ChildActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        String folder = Environment.getExternalStorageDirectory() + "/Pictures/MyPics";
        new File(folder).mkdir();
        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            File file = new File(folder, System.currentTimeMillis() / 1000 + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);
            outputStream.flush();
            outputStream.close();
            MediaScannerConnection.scanFile(ChildActivity.this, new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

