package com.example.onestopshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter class for binding ItemPhoto data to RecyclerView.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<ItemPhoto> photoList;

    public PhotoAdapter( List<ItemPhoto> photoList) {
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        ItemPhoto currentPhoto = photoList.get(position);
        Picasso.get().load(currentPhoto.getPhotoUrl()).fit().centerCrop().into(holder.photoImageView);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    /**
     * ViewHolder class representing each item in the RecyclerView.
     */
    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;

         PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
        }
    }
}
