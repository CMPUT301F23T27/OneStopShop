package com.example.onestopshop;

import android.app.AlertDialog;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    private List<Uri> localUris;
    private List<ItemPhoto> photoList;
    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private Button addBtn;
    private Button backBtn;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        addBtn = view.findViewById(R.id.btnAddPhoto);
        backBtn = view.findViewById(R.id.back_button);
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
        return view;
    }
    private void showPhotoOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add a Photo");

        // Add options to the dialog
        builder.setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, (dialog, choice) -> {
            switch (choice) {
                case 0:
                    // Take Photo option clicked

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
    private void dispatchPickImageIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageLauncher.launch(pickImageIntent);
    }


}
