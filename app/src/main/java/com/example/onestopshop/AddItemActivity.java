package com.example.onestopshop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText edtItemName, edtDescription, edtPurchaseDate, edtMake, edtModel, edtEstimatedValue, edtSerialNumber, edtComments;
    private Button btnAddItem, BtnCancel;
    private InventoryController inventoryController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Initialize InventoryController
        inventoryController = new InventoryController();

        // Initialize UI elements
        edtItemName = findViewById(R.id.Name);
        edtDescription = findViewById(R.id.tvDescriptionContent);
        edtPurchaseDate = findViewById(R.id.date);
        edtMake = findViewById(R.id.make);
        edtModel = findViewById(R.id.model);
        edtEstimatedValue = findViewById(R.id.value);
        edtSerialNumber = findViewById(R.id.serialnumber);
        edtComments = findViewById(R.id.comment);
        btnAddItem = findViewById(R.id.btn_add_item);

        // Set up click listener for Add Item button
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToFirebase();
            }
        });

        // Set up click listener for Cancel button
        BtnCancel = findViewById(R.id.btnCancel);
        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    // Method to add item to Firebase
    private void addItemToFirebase() {
        // Get data from UI elements
        String itemName = edtItemName.getText().toString();
        String description = edtDescription.getText().toString();
        String purchaseDate = edtPurchaseDate.getText().toString();
        String make = edtMake.getText().toString();
        String model = edtModel.getText().toString();
        double estimatedValue = Double.parseDouble(edtEstimatedValue.getText().toString());
        String serialNumber = edtSerialNumber.getText().toString();
        String comments = edtComments.getText().toString();

        // Create an Item object
        Item newItem = new Item(itemName, description, purchaseDate, make, model, estimatedValue, comments, serialNumber, new ArrayList<String>());

        // Add the item to Firebase using InventoryController
        inventoryController.addItem(newItem);

        // Finish the activity or perform any other actions you want
        finish();
    }
}
