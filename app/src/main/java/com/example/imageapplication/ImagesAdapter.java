package com.example.imageapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {

    private ArrayList<String> arrayUriList = new ArrayList<>();

    public void setImagesArray(ArrayList<String> arr) {
        arrayUriList = arr;
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
        Picasso.get().load(arrayUriList.get(position)).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent Intent = new Intent(context, ChildActivity.class);
                String uri = arrayUriList.get(position);
                Intent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
                context.startActivity(Intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayUriList.size();
    }

    static class ImagesViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image1);
        }
    }
}