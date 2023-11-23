package com.example.onestopshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

import java.util.List;

public class AddGalleryActivity extends AppCompatActivity {
    private List<ItemPhoto> photoList;
    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private Button addBtn;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        recyclerView = findViewById(R.id.recyclerView);
        addBtn = findViewById(R.id.btnAddPhoto);

        addBtn.setOnClickListener(v -> showPhotoOptionsDialog());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        photoAdapter = new PhotoAdapter(photoList);
        recyclerView.setAdapter(photoAdapter);
    }
    private void showPhotoOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                            result -> {
                                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                    Uri selectedImage = result.getData().getData();

                                }
                            });
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