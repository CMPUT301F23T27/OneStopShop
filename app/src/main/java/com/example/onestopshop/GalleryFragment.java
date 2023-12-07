package com.example.onestopshop;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * GalleryFragment is a Fragment that allows the user to view and manage photos associated with an item.
 */
public class GalleryFragment extends Fragment implements CameraXFragment.OnImageCapturedListener{
    private List<Uri> localUris;
    private List<ItemPhoto> photoList;
    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private Button addBtn;
    private Button deleteButton;
    private Button backBtn;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        addBtn = view.findViewById(R.id.btnAddPhoto);
        backBtn = view.findViewById(R.id.back_button);
        deleteButton = view.findViewById(R.id.btnDelete);
        photoList = new ArrayList<>();
        localUris = ((AddItemActivity) requireActivity()).getUriList();
        //treat uris as download urls for the adapter so we can use the same adapter
        for(Uri uri : localUris) {
            photoList.add(new ItemPhoto(uri.toString()));
        }
        addBtn.setOnClickListener(v -> showPhotoOptionsDialog());
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        photoAdapter = new PhotoAdapter(photoList);
        recyclerView.setAdapter(photoAdapter);

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        photoList.add(new ItemPhoto(selectedImage.toString()));
                        localUris.add(selectedImage);
                        //String tag = "AddGalleryActivity";
                        //Log.d(tag, "" + localUris.size());
                        photoAdapter.notifyDataSetChanged();
                    }
                });

        backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemPhoto selectedPhoto = photoAdapter.getSelectedPhoto();

                // Handle the deletion action here
                if (selectedPhoto != null) {
                    // Remove the selected photo from the list
                    localUris.remove(Uri.parse(selectedPhoto.getPhotoUrl()));
                    photoList.remove(selectedPhoto);
                    // Clear the selection state in the adapter
                    photoAdapter.clearSelection();
                    // Notify the adapter of the data change
                    photoAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
    /**
     * Displays the photo options dialog when the "Add a Photo" button is clicked.
     */
    private void showPhotoOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add a Photo");

        // Add options to the dialog
        builder.setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, (dialog, choice) -> {
            switch (choice) {
                case 0:
                    // Take Photo option clicked
                    dispatchTakePictureFragment();
                    break;
                case 1:
                    // Choose from Gallery option clicked
                    dispatchPickImageIntent();
                    break;
            }
        });

        // Create and show the dialog
        builder.create().show();
    }

    /**
     * Launches the intent to pick an image from the gallery.
     */
    private void dispatchPickImageIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickImageIntent.setType("image/*");
        pickImageLauncher.launch(pickImageIntent);
    }

    private void dispatchTakePictureFragment() {
        CameraXFragment cameraXFragment = new CameraXFragment();
        cameraXFragment.setOnImageCapturedListener(this);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, cameraXFragment)
                .addToBackStack(null)
                .commit();

    }
    @Override
    public void onImageCaptured(Uri selectedImage) {
        photoList.add(new ItemPhoto(selectedImage.toString()));
        localUris.add(selectedImage);
        //String tag = "AddGalleryActivity";
        //Log.d(tag, "" + localUris.size());
        photoAdapter.notifyDataSetChanged();
    }



}
