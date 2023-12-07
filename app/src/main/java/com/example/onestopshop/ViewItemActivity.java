package com.example.onestopshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Activity for viewing the details of a specific item.
 * This class is responsible for displaying item details
 * and handling user interactions for editing or deleting an item.
 */

public class ViewItemActivity extends AppCompatActivity {
    private InventoryController inventoryController;
    private PhotosController photosController;
    private TextView itemName;
    private TextView tvDescriptionContent;
    private TextView date;
    private TextView make;
    private TextView model;
    private TextView serialNumber;
    private TextView estimatedValue;
    private TextView tags;
    private TextView comments;
    private Button btnBack;
    private Button btnDelete;
    private Button btnEdit;
    ChipGroup tagsGroup;
    private ImageView itemPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        // Initialize Views
        itemName = findViewById(R.id.itemName);
        tvDescriptionContent = findViewById(R.id.tvDescriptionContent);
        date = findViewById(R.id.date);
        make = findViewById(R.id.make);
        model = findViewById(R.id.model);
        serialNumber = findViewById(R.id.serialnumber);
        estimatedValue = findViewById(R.id.value);
        tagsGroup = findViewById(R.id.tagsGroup);
        comments = findViewById(R.id.comment);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);
        itemPhoto = findViewById(R.id.productImage);
        Intent intent = getIntent();
        String itemId = intent.getStringExtra("itemId");
        photosController = new PhotosController(itemId);
        inventoryController = new InventoryController(); // Initialize your controller

        if (itemId != null) {
            inventoryController.getItemById(itemId, new InventoryController.OnItemFetchListener() {
                @Override
                public void onItemFetched(Item item) {
                    if (item != null) {
                        // Item retrieved successfully, now use it to populate the UI
                        displayItemDetails(item);
                    } else {
                        // Handle case when item fetch fails
                    }
                }

                @Override
                public void onItemFetchFailed() {
                    // Handle failure to fetch item
                    Toast.makeText(ViewItemActivity.this, "Error fetching item details.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        photosController.getDownloadUrl(new PhotosController.DownloadUrlCallback() {
            @Override
            public void onSuccess(String downloadUrl) {
                if (downloadUrl != null && !downloadUrl.isEmpty()) {
                    // Use the download URL here, for example, load it into an ImageView
                    Picasso.get().load(downloadUrl).into(itemPhoto);
                    itemPhoto.setBackgroundColor(0);
                } else {
                    // If there's no download URL leave the default image

                }
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure, for example

            }
        });

        itemPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(ViewItemActivity.this, EditGalleryActivity.class);
                galleryIntent.putExtra("itemId", itemId);
                startActivity(galleryIntent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryController.deleteItem(itemId);
                finish();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewItemActivity.this, EditItemActivity.class);
                intent.putExtra("itemId", itemId);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String itemId = intent.getStringExtra("itemId");
        inventoryController = new InventoryController(); // Initialize your controller
        if (itemId != null) {
            inventoryController.getItemById(itemId, new InventoryController.OnItemFetchListener() {
                @Override
                public void onItemFetched(Item item) {
                    if (item != null) {
                        // Item retrieved successfully, now use it to populate the UI
                        displayItemDetails(item);
                    } else {
                        // Handle case when item fetch fails
                    }
                }

                @Override
                public void onItemFetchFailed() {
                    // Handle failure to fetch item
                    Toast.makeText(ViewItemActivity.this, "Error fetching item details.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        photosController.getDownloadUrl(new PhotosController.DownloadUrlCallback() {
            @Override
            public void onSuccess(String downloadUrl) {
                if (downloadUrl != null && !downloadUrl.isEmpty()) {
                    Picasso.get().load(downloadUrl).into(itemPhoto);
                    itemPhoto.setBackgroundColor(0);
                } else {
                    // If there's no download URL leave the default image
                    itemPhoto.setImageResource(R.drawable.baseline_image_24);
                    int defaultColor = ContextCompat.getColor(ViewItemActivity.this, R.color.defaultPurple);
                    itemPhoto.setBackgroundColor(defaultColor);
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure, for example

            }
        });
    }
    private void displayItemDetails (Item item){
        // Update your views with the retrieved item data
        itemName.setText("Product Name: " + item.getItemName());
        tvDescriptionContent.setText("" + item.getDescription());
        tvDescriptionContent.setMovementMethod(new ScrollingMovementMethod());
        date.setText("Date of Purchase:  " + item.getPurchaseDate());
        make.setText("Make:  " + item.getMake());
        make.setMovementMethod(new ScrollingMovementMethod());
        model.setText("Model:  " + item.getModel());
        String serialNumberStr = "";
        if (!(item.getSerialNumber() == null || item.getSerialNumber().isEmpty())) {
            serialNumberStr = item.getSerialNumber().toString();
        }
        serialNumber.setText("Serial Number:  " + serialNumberStr);
        estimatedValue.setText("Estimated Value:  " + String.format("$%.2f", item.getEstimatedValue()));
        String commentsStr = "";
        if (!(item.getComments() == null || item.getComments().isEmpty())) {
            commentsStr = item.getComments();
        }
        comments.setText(commentsStr);
        comments.setMovementMethod(new ScrollingMovementMethod());
        displayTags(tagsGroup, item.getTags());
    }

        private void displayTags(ChipGroup chipGroup, List<String> tags) {
            chipGroup.removeAllViews();
            for (String tag : tags) {
                Chip chip = new Chip(this, null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Action);
                chip.setText(tag);
                chip.setClickable(false);
                chipGroup.addView(chip);
            }
        }


}
