package com.example.onestopshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class ViewItem extends AppCompatActivity {

    // Declare UI elements
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

    // Declare variables
    private String itemId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        // Initialize Views and Firebase Firestore
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
        db = FirebaseFirestore.getInstance();

        // Get the item ID from the Intent
        Intent intent = getIntent();
        itemId = intent.getStringExtra("ITEM_ID");

        if (itemId == null || itemId.isEmpty()) {
            // The itemId is null or empty
            Toast.makeText(this, "Error: Item ID is missing.", Toast.LENGTH_SHORT).show();
            Log.e("ViewItem", "Item ID was not provided in the intent.");
            // Close the activity or inform the user
            finish();
        }

        // Set Back Button action
        btnBack.setOnClickListener(v -> finish());

        // Set Delete button click listener
        btnDelete.setOnClickListener(v -> deleteItem());

        // Load item details from Firestore
        loadItemDetails();
    }

    private void loadItemDetails() {
        DocumentReference itemRef = db.collection("items").document(itemId);
        itemRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Retrieve item data from the documentSnapshot
                    String NameStr = documentSnapshot.getString("itemName");
                    String description = documentSnapshot.getString("description");
                    String purchaseDate = documentSnapshot.getString("purchaseDate");
                    String makeStr = documentSnapshot.getString("make");
                    String modelStr = documentSnapshot.getString("model");
                    String serialNumberStr = documentSnapshot.getString("serialNumber");
                    double estimatedValueDouble = documentSnapshot.getDouble("estimatedValue");
                    String commentsStr = documentSnapshot.getString("comments");
                    ArrayList<String> tagsList = (ArrayList<String>) documentSnapshot.get("tags");

                    // Update views with the retrieved item data
                    itemName.setText("Product Name: " + NameStr);
                    tvDescriptionContent.setText(description);
                    date.setText("Date of Purchase: " + purchaseDate);
                    make.setText("Make: " + makeStr);
                    model.setText("Model: " + modelStr);
                    serialNumber.setText("Serial Number: " + serialNumberStr);
                    estimatedValue.setText("Estimated Value: " + String.format("$%.2f", estimatedValueDouble));
                    comments.setText("Comments: " + commentsStr);
                    tags.setText("Tags: " + android.text.TextUtils.join(", ", tagsList));
                } else {
                    // The document does not exist
                    Toast.makeText(ViewItem.this, "Item not found.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                // Handle the error
                Toast.makeText(ViewItem.this, "Error fetching item details.", Toast.LENGTH_SHORT).show();
                if (task.getException() != null) {
                    Log.e("ViewItem", task.getException().getMessage());
                }
                finish();
            }
        });
    }

    private void deleteItem() {
        DocumentReference itemRef = db.collection("items").document(itemId);
        itemRef.delete().addOnSuccessListener(aVoid -> {
            // Item deleted successfully
            Toast.makeText(ViewItem.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            finish(); // Navigate back to the inventory activity
        }).addOnFailureListener(e -> {
            // Handle deletion failure
            Toast.makeText(ViewItem.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
            Log.e("DeleteItem", "Error deleting item: " + e.getMessage());
        });
    }
}

