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
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for editing the gallery of photos associated with an item.
 */
public class EditGalleryActivity extends AppCompatActivity implements PhotosController.OnPhotoListUpdateListener {
    private List<ItemPhoto> photoList;
    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private PhotosController photosController;
    private Button addBtn;
    private Button deleteBtn;
    private Button backBtn;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;


    /**
     * Called when the activity is created. Initializes UI elements and sets up necessary listeners.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gallery);
        Intent intent = getIntent();
        String itemId = intent.getStringExtra("itemId");
        recyclerView = findViewById(R.id.recyclerView);
        addBtn = findViewById(R.id.btnAddPhoto);
        deleteBtn = findViewById(R.id.btnDelete);
        backBtn = findViewById(R.id.back_button);
        photoList = new ArrayList<>();
        photosController = new PhotosController(itemId);
        photosController.setOnPhotoListUpdateListener(this);
        addBtn.setOnClickListener(v -> showPhotoOptionsDialog());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        photoAdapter = new PhotoAdapter(photoList);
        recyclerView.setAdapter(photoAdapter);

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        photosController.uploadPhoto(selectedImage);
                    }
                });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        backBtn.setOnClickListener(v -> finish());

    }
    /**
     * Displays a dialog with options for adding a photo.
     */
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
                    break;
            }
        });

        // Create and show the dialog
        builder.create().show();
    }
    /**
     * Launches the image picker intent for adding gallery images.
     */
    private void dispatchPickImageIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickImageIntent.setType("image/*");
        pickImageLauncher.launch(pickImageIntent);
    }

    /**
     * Callback method invoked when the photo list is updated.
     *
     * @param updatedPhotoList The updated list of item photos.
     */
    @Override
    public void onPhotoListUpdated(List<ItemPhoto> updatedPhotoList) {
        photoList.clear();
        photoList.addAll(updatedPhotoList);
        photoAdapter.notifyDataSetChanged();
    }

}