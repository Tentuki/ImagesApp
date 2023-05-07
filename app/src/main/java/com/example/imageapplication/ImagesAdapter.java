package com.example.imageapplication;

import static android.provider.LiveFolders.INTENT;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {

    private ArrayList<Bitmap> arrayList = new ArrayList<>();
    private ArrayList<String> arr = new ArrayList<>();

    public void setImagesArray(ArrayList<Bitmap> array, ArrayList<String> arr) {
        arrayList = array;
        this.arr = arr;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ImagesViewHolder(inflater.inflate(R.layout.layout_for_images, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Bitmap bmp = arrayList.get(position);
        holder.bind(bmp);
        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent Intent = new Intent(context, ChildActivity.class);
                String uri = arr.get(position);
                Intent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
                context.startActivity(Intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class ImagesViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image1;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);

            image1 = itemView.findViewById(R.id.image1);
        }

        void bind(Bitmap bm) {
            image1.setImageBitmap(bm);
        }
    }
}