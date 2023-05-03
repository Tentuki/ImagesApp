package com.example.imageapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {

    private  ArrayList<Bitmap> arrayList = new ArrayList<>();

    public void setImagesArray(ArrayList<Bitmap> array) {
        arrayList = array;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ImagesViewHolder(inflater.inflate(R.layout.layout_for_images, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        holder.bind(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ImagesViewHolder extends RecyclerView.ViewHolder {

        private ImageView image1;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);

            image1 = itemView.findViewById(R.id.image1);
        }

        void bind(Bitmap bm1) {
            image1.setImageBitmap(bm1);
        }
    }
}
















