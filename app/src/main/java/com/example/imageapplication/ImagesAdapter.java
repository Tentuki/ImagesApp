package com.example.imageapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {

    private ArrayList<Bitmap> arrayList = new ArrayList<>();

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
        Bitmap bmp = arrayList.get(position);
        holder.bind(bmp);
        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent childIntent = new Intent(context, ChildActivity.class);
                view.buildDrawingCache();
                Bitmap image= view.getDrawingCache();

                Bundle extras = new Bundle();
                extras.putParcelable("imageBitmap", image);
                childIntent.putExtras(extras);
                context.startActivity(childIntent);
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
















