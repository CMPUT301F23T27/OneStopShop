package com.example.onestopshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * EditItemActivity allows the user to edit details of an existing item in the inventory.
 */
public class EditItemActivity extends AppCompatActivity {
    private InventoryController inventoryController;
    private PhotosController photosController;
    private TagsController tagsController;
    private EditText itemNameText;
    private EditText descriptionText;
    private EditText purchaseDateText;
    private EditText makeText;
    private EditText modelText;
    private EditText serialNumberText;
    private EditText estimatedValueText;
    private ChipGroup tagsGroup;
    private List<String> selectedTags;
    private TextView addTagBtn;
    private EditText commentsText;
    private Button btnCancel;
    private Button confirm;
    private ImageButton addPhoto;
    private ImageView itemPhoto;
    private ImageView scanButton;
    private Button scanBarcodeButton;
    private final ActivityResultLauncher<Intent> startForSerialNumberResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == this.RESULT_OK) {
                            // Retrieve data from the intent returned by ScanActivity
                            Intent data = result.getData();
                            String serialNumberScan = data.getStringExtra("serialNumber");
                            serialNumberText.setText(serialNumberScan);
                            // Handle the received data
                        } else {
                            // Handle when the result is not OK
                        }
                    });
    private final ActivityResultLauncher<Intent> scanBarcodeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Log.d("AddItemBarcode", "ResultOK");
                    // Handle the result, extract the scanned information
                    Intent data = result.getData();
                    if (data != null) {
                        Log.d("AddItemBarcode", "Data Available");
                        String scannedDescription = data.getStringExtra("scannedDescription");
                        String scannedMake = data.getStringExtra("scannedMake");
                        Double scannedEstimatedValue = data.getDoubleExtra("scannedEstimatedValue", 0);
                        Log.d("AddItemBarcode", scannedDescription);
                        // Set the text to EditText
                        descriptionText.setText(scannedDescription);
                        makeText.setText(scannedMake);
                        estimatedValueText.setText("" + scannedEstimatedValue);
                    }
                }
            }
    );
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
        tagsGroup = findViewById(R.id.tagsGroup);
        addTagBtn = findViewById(R.id.add_tag_button);
        scanButton = findViewById(R.id.scanButton);
        commentsText = findViewById(R.id.comments);
        btnCancel = findViewById(R.id.btnCancel);
        addPhoto = findViewById(R.id.add_image_button);
        itemPhoto = findViewById(R.id.productImage);
        scanBarcodeButton = findViewById(R.id.btn_scan_barcode_edit);
        Intent intent = getIntent();
        String itemId = intent.getStringExtra("itemId");

        inventoryController = new InventoryController();
        photosController = new PhotosController(itemId);
        tagsController = new TagsController();
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
                        Double estimatedValue = 0.0;
                        if(estimatedValueText.getText().toString() != null && estimatedValueText.getText().toString().isEmpty() == false) {
                            try {
                                estimatedValue = Double.parseDouble(estimatedValueText.getText().toString());
                            }
                            catch(Exception e) {
                                Toast.makeText(EditItemActivity.this, "Invalid Estimated Value", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }else {
                            Toast.makeText(EditItemActivity.this, "Invalid Estimated Value", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String comments = commentsText.getText().toString();
                        boolean valid = validItem(new Item(itemName, description, purchaseDate, make,
                                model, estimatedValue, comments, serialNumber, selectedTags));
                        if(valid){
                            inventoryController = new InventoryController();
                            inventoryController.updateItem(itemId, new Item(itemName, description, purchaseDate, make,
                                    model, estimatedValue, comments, serialNumber, selectedTags));
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onItemFetchFailed() {

            }
        });

        photosController.getDownloadUrl(new PhotosController.DownloadUrlCallback() {
            @Override
            public void onSuccess(String downloadUrl) {
                if (downloadUrl != null && !downloadUrl.isEmpty()) {
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
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditItemActivity.this, ScanActivity.class);
                startForSerialNumberResult.launch(intent);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(EditItemActivity.this, EditGalleryActivity.class);
                galleryIntent.putExtra("itemId", itemId);
                startActivity(galleryIntent);
            }
        });
        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsController.fetchExistingTags(new TagsController.OnTagsFetchListener() {
                    @Override
                    public void onSuccess(List<String> existingTags) {
                        // send existing tags to dialog
                        TagDialog tagDialog = new TagDialog(EditItemActivity.this,selectedTags, existingTags, tagsGroup);
                        tagDialog.show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Handle failure
                        e.printStackTrace();
                    }
                });
            }
        });
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditItemActivity.this, BarcodeActivity.class);
                scanBarcodeLauncher.launch(intent);
            }
        });


    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("EditItemActivity", "onResume");
        photosController.getDownloadUrl(new PhotosController.DownloadUrlCallback() {
            @Override
            public void onSuccess(String downloadUrl) {
                Log.d("EditItemActivity", "onResume-SgotDurl");
                if (downloadUrl != null && !downloadUrl.isEmpty()) {
                    Log.d("EditItemActivity", "onResume-SgotDurlexists");
                    Picasso.get().load(downloadUrl).into(itemPhoto);
                    itemPhoto.setBackgroundColor(0);
                } else {
                    // If there's no download URL leave the default image
                    Log.d("EditItemActivity", "onResume-SgotDurlNotexists");
                    itemPhoto.setImageResource(R.drawable.baseline_image_24);
                    int defaultColor = ContextCompat.getColor(EditItemActivity.this, R.color.defaultPurple);
                    itemPhoto.setBackgroundColor(defaultColor);
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure, for example
                Log.d("EditItemActivity", "onResume-Failure");

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
        selectedTags = item.getTags();
        displayTags(tagsGroup, selectedTags);
    }
    /**
     * Validates the input fields of the item.
     *
     * @return True if the item is valid; false otherwise.
     */
    public boolean validItem(Item item) {
        boolean valid = true;
        if(item.getItemName() == null || item.getItemName().isEmpty()) {
            Toast.makeText(EditItemActivity.this, "Invalid Product Name", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(item.getDescription() == null || item.getDescription().isEmpty()) {
            Toast.makeText(EditItemActivity.this, "Invalid Description", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(TextUtils.isEmpty(item.getPurchaseDate())){
            Toast.makeText(EditItemActivity.this, "Invalid Purchase Date", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(item.getMake() == null || item.getMake().isEmpty()) {
            Toast.makeText(EditItemActivity.this, "Invalid Make", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(item.getModel() == null || item.getModel().isEmpty()) {
            Toast.makeText(EditItemActivity.this, "Invalid Model", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(item.getEstimatedValue() < 0) {
            Toast.makeText(EditItemActivity.this, "Value must be greater than 0", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(selectedTags.size() == 0) {
            Toast.makeText(EditItemActivity.this, "Must have at least one tag", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
    /**
     * Displays the selected tags in the ChipGroup.
     *
     * @param chipGroup The ChipGroup where tags should be displayed.
     * @param tags      The list of tags to be displayed.
     */
    private void displayTags(ChipGroup chipGroup, List<String> tags) {
        for (String tag : tags) {
            Chip chip = new Chip(this, null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Action);
            chip.setText(tag);
            chip.setClickable(false);
            chip.setCloseIconVisible(true);
            chipGroup.addView(chip);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selectedTags.remove(tag);
                }
            });
        }
    }
    /**
     * Shows the date picker dialog for selecting the purchase date.
     *
     * @param v The View that triggered the method (in this case, a button click).
     */
    public void showDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EditItemActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Update the EditText with the selected date
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    purchaseDateText.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

}