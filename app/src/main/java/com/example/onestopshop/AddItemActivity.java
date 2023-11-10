package com.example.onestopshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddItemActivity extends AppCompatActivity {
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
    private ImageView scanButton;

    private final ActivityResultLauncher<Intent> startForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            // Retrieve data from the intent returned by ScanActivity
                            Intent data = result.getData();
                            String serialNumberScan = data.getStringExtra("serialNumber");
                            serialNumberText.setText(serialNumberScan);
                            // Handle the received data
                        } else {
                            // Handle when the result is not OK
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        confirm = findViewById(R.id.btn_add_item);
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
        scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_CODE = 1;
                Intent intent = new Intent(AddItemActivity.this, ScanActivity.class);
                startForResult.launch(intent);
            }
        });
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
                    inventoryController.addItem(new Item(itemName, description, purchaseDate, make,
                            model, estimatedValue, comments, serialNumber, tags));
                    finish();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public boolean validItem(){
        return true;
    }
}