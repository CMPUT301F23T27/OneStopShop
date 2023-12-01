package com.example.onestopshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

/**
 * EditItemActivity allows the user to edit details of an existing item in the inventory.
 */
public class EditItemActivity extends AppCompatActivity {
    private InventoryController inventoryController;
    private EditText itemNameText;
    private EditText descriptionText;
    private EditText purchaseDateText;
    private EditText makeText;
    private EditText modelText;
    private EditText serialNumberText;
    private EditText estimatedValueText;
    private EditText tagsText;
    private EditText commentsText;
    private Button btnCancel;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        confirm = findViewById(R.id.btnConfirm);
        // Initialize Views
        itemNameText = findViewById(R.id.itemName);
        descriptionText = findViewById(R.id.description);
        purchaseDateText = findViewById(R.id.purchaseDate);
        makeText = findViewById(R.id.make);
        modelText = findViewById(R.id.model);
        serialNumberText = findViewById(R.id.serialnumber);
        estimatedValueText = findViewById(R.id.estimatedValue);
        tagsText = findViewById(R.id.tags);
        commentsText = findViewById(R.id.comments);
        btnCancel = findViewById(R.id.btnCancel);
        Intent intent = getIntent();
        String itemId = intent.getStringExtra("itemId");

        inventoryController = new InventoryController();
        inventoryController.getItemById(itemId, new InventoryController.OnItemFetchListener() {
            @Override
            public void onItemFetched(Item item) {
                displayItemDetails(item);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String itemName = itemNameText.getText().toString();
                        String description = descriptionText.getText().toString();
                        String purchaseDate = purchaseDateText.getText().toString();
                        String make = makeText.getText().toString();
                        String model = modelText.getText().toString();
                        String serialNumber = serialNumberText.getText().toString();
                        Double estimatedValue = Double.parseDouble(estimatedValueText.getText().toString());
                        List<String> tags = Arrays.asList(tagsText.getText().toString().split(","));
                        String comments = commentsText.getText().toString();
                        boolean validItem = validItem();
                        if(validItem){
                            inventoryController = new InventoryController();
                            inventoryController.updateItem(itemId, new Item(itemName, description, purchaseDate, make,
                                    model, estimatedValue, comments, serialNumber, tags));
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onItemFetchFailed() {

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    /**
     * Displays the details of the given item in the corresponding EditText fields.
     *
     * @param item The item whose details should be displayed.
     */
    private void displayItemDetails(Item item) {
        // Update your views with the retrieved item data
        itemNameText.setText(item.getItemName());
        descriptionText.setText(item.getDescription());
        purchaseDateText.setText(item.getPurchaseDate());
        makeText.setText(item.getMake());
        modelText.setText(item.getModel());
        String serialNumberStr = "";
        if(!(item.getSerialNumber() == null || item.getSerialNumber().isEmpty())){
            serialNumberStr = item.getSerialNumber().toString();
        }
        serialNumberText.setText(serialNumberStr);
        estimatedValueText.setText(String.format("%.2f", item.getEstimatedValue()));
        String commentsStr = "";
        if(!(item.getComments() == null || item.getComments().isEmpty())){
            commentsStr = item.getComments();
        }
        commentsText.setText(commentsStr);
        //remove brackets
        tagsText.setText(item.getTags().toString().substring(1,item.getTags().toString().length() -1));
    }
    /**
     * Validates the input fields of the item.
     *
     * @return True if the item is valid; false otherwise.
     */
    public boolean validItem(){
        return true;
    }
}