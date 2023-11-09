package com.example.onestopshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewItemActivity extends AppCompatActivity {
    private InventoryController inventoryController;
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
        tags = findViewById(R.id.tags);
        comments = findViewById(R.id.comment);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);


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
    }


    private void displayItemDetails(Item item) {
        // Update your views with the retrieved item data
        itemName.setText("Product Name: " + item.getItemName());
        tvDescriptionContent.setText("" + item.getDescription());
        date.setText("Date of Purchase:  " + item.getPurchaseDate());
        make.setText("Make:  "+ item.getMake());
        model.setText("Model:  "+ item.getModel());
        String serialNumberStr = "";
        if(!(item.getSerialNumber() == null || item.getSerialNumber().isEmpty())){
            serialNumberStr = item.getSerialNumber().toString();
        }
        serialNumber.setText("Serial Number:  "+ serialNumberStr);
        estimatedValue.setText("Estimated Value:  "+String.format("$%.2f", item.getEstimatedValue()));
        String commentsStr = "";
        if(!(item.getComments() == null || item.getComments().isEmpty())){
            commentsStr = item.getComments();
        }
        comments.setText("Comments: "+ commentsStr);
        tags.setText("Tags:  "+ item.getTags().toString());
    }

}