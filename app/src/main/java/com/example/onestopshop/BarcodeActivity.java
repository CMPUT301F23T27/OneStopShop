package com.example.onestopshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity for scanning and adding barcodes to the inventory.
 */
public class BarcodeActivity extends AppCompatActivity {

    private Button scanButton;
    private Button addButton;
    private ActivityResultLauncher<Intent> takePictureLauncherAddBarcode;
    private ActivityResultLauncher<Intent> pickImageLauncherAddBarcode;
    private ActivityResultLauncher<Intent> takePictureLauncherScanBarcode;
    private ActivityResultLauncher<Intent> pickImageLauncherScanBarcode;
    String descriptionVal, makeVal, scannedDescription, scannedMake;
    Double estimatedValueVal, scannedEstimatedValue;
    /**
     * Creates the activity, initializes UI elements, and sets up click listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        addButton = findViewById(R.id.add_barcode);
        scanButton = findViewById(R.id.scan_barcode);
        Button cancelButton = findViewById(R.id.cancel_button);
        descriptionVal = "";
        makeVal = "";
        estimatedValueVal = 0.0;
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoOptionsDialog();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBarcodeDialog addBarcodeDialog = new AddBarcodeDialog(BarcodeActivity.this);
                addBarcodeDialog.show();
            }
        });
        cancelButton.setOnClickListener(view-> finish());
        pickImageLauncherScanBarcode = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            processBarcodeForScan(imageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        pickImageLauncherAddBarcode = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(BarcodeActivity.this.getContentResolver(), selectedImage);
                            processBarcodeForAdd(imageBitmap, descriptionVal, makeVal, estimatedValueVal);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    /**
     * Custom dialog for adding barcode information.
     */
    public class AddBarcodeDialog extends Dialog {
        private Button choosePhoto;
        private Button takePhoto;
        private EditText description, make, estimatedValueText;
        private Double estimatedValue;
        /**
         * Constructor for the custom dialog.
         *
         * @param context The context of the dialog.
         */
        public AddBarcodeDialog(@NonNull Context context) {
            super(context);
            setContentView(R.layout.dialog_add_barcode);
            choosePhoto = findViewById(R.id.choose_photo_button);
            takePhoto = findViewById(R.id.take_photo_button);
            description = findViewById(R.id.description);
            make = findViewById(R.id.make);
            estimatedValueText = findViewById(R.id.estimatedValue);

            choosePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    estimatedValue = 0.0;
                    if(estimatedValueText.getText().toString() != null && estimatedValueText.getText().toString().isEmpty() == false) {
                        try {
                            estimatedValue = Double.parseDouble(estimatedValueText.getText().toString());
                        }
                        catch(Exception e) {
                            Toast.makeText(getContext(), "Invalid Estimated Value", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        Toast.makeText(getContext(), "Invalid Estimated Value", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(validFields()){
                        estimatedValueVal = estimatedValue;
                        descriptionVal = description.getText().toString();
                        makeVal = make.getText().toString();
                        dispatchPickImageIntentAdd();
                        dismiss();
                    }
                }
            });

        }
        /**
         * Validates the input fields in the dialog.
         *
         * @return True if the fields are valid, false otherwise.
         */
        public boolean validFields(){
            boolean valid = true;
            if(TextUtils.isEmpty(description.getText().toString())){
                Toast.makeText(getContext(), "Invalid Description", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if(TextUtils.isEmpty(make.getText().toString())){
                Toast.makeText(getContext(), "Invalid Make", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if(estimatedValue < 0){
                Toast.makeText(getContext(), "Invalid Estimated value", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            return valid;
        }

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
                    dispatchPickImageIntentScan();
                    break;
            }
        });

        // Create and show the dialog
        builder.create().show();
    }
    /**
     * Launches the image picker intent for adding barcode images when adding a barcode.
     */
    private void dispatchPickImageIntentAdd() {
        Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickImageIntent.setType("image/*");
        pickImageLauncherAddBarcode.launch(pickImageIntent);
    }
    /**
     * Launches the image picker intent for adding barcode images when scanning a barcode.
     */
    private void dispatchPickImageIntentScan() {
        Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickImageIntent.setType("image/*");
        pickImageLauncherScanBarcode.launch(pickImageIntent);
    }


    /**
     * Processes the barcode from the scanned image and retrieves information from Firestore.
     *
     * @param imageBitmap The bitmap image containing the barcode.
     */
    public void processBarcodeForScan(Bitmap imageBitmap){
        //check firestore for existence
        //Barcode scanning from MLKit documentation
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_CODE_128, Barcode.FORMAT_CODE_39, Barcode.FORMAT_CODE_93,
                                Barcode.FORMAT_CODABAR, Barcode.FORMAT_DATA_MATRIX, Barcode.FORMAT_EAN_13,
                                Barcode.FORMAT_EAN_8, Barcode.FORMAT_ITF, Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_UPC_A, Barcode.FORMAT_UPC_E, Barcode.FORMAT_PDF417,
                                Barcode.FORMAT_AZTEC)
                        .build();
        BarcodeScanner barcodeScanner = BarcodeScanning.getClient(options);
        InputImage image = InputImage.fromBitmap(imageBitmap, 0);
        barcodeScanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    Log.d("AddBarcode", "Success");
                    Log.d("AddBarcode", ""+barcodes.size());
                    if (barcodes.size() > 0) {
                        // Get the first barcode (assuming there is only one in the image)
                        Barcode barcode = barcodes.get(0);
                        // Extract information from the barcode
                        String barcodeValue = barcode.getDisplayValue();
                        Log.d("AddBarcode", barcodeValue);
                        // Upload barcode information to Firestore
                        retrieveBarcodeFromFirestore(barcodeValue);
                    }
                });

    }

    /**
     * Retrieves barcode information from Firestore based on the barcode value.
     *
     * @param barcodeValue The value of the scanned barcode.
     */
    public void retrieveBarcodeFromFirestore(String barcodeValue) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference barcodesRef = db.collection("users").document(userId).collection("barcodes");
        // Query to find the barcode by its value
        Query query = barcodesRef.whereEqualTo("barcodeValue", barcodeValue);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Barcode found, assuming there is only one matching barcode
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                        // Extract information from the Firestore document
                        String description = documentSnapshot.getString("description");
                        String make = documentSnapshot.getString("make");
                        double estimatedValue = documentSnapshot.getDouble("estimatedValue");
                        Toast.makeText(BarcodeActivity.this, "Barcode found", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("scannedDescription", description);
                        resultIntent.putExtra("scannedMake", make);
                        resultIntent.putExtra("scannedEstimatedValue", estimatedValue);
                        setResult(RESULT_OK, resultIntent);
                        finish();

                    } else {
                        // Barcode not found
                        Toast.makeText(BarcodeActivity.this, "Barcode not found in Firestore", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(BarcodeActivity.this, "Failed to search barcode in Firestore", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Processes the barcode from the manually entered image and uploads information to Firestore.
     *
     * @param imageBitmap      The bitmap image containing the barcode.
     * @param description      The description of the item.
     * @param make             The make of the item.
     * @param estimatedValue   The estimated value of the item.
     */
    public void processBarcodeForAdd(Bitmap imageBitmap, String description, String make, Double estimatedValue){
        //add barcode to firestore
        //Barcode scanning from MLKit documentation
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_CODE_128, Barcode.FORMAT_CODE_39, Barcode.FORMAT_CODE_93,
                                Barcode.FORMAT_CODABAR, Barcode.FORMAT_DATA_MATRIX, Barcode.FORMAT_EAN_13,
                                Barcode.FORMAT_EAN_8, Barcode.FORMAT_ITF, Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_UPC_A, Barcode.FORMAT_UPC_E, Barcode.FORMAT_PDF417,
                                Barcode.FORMAT_AZTEC
                                )
                        .build();
        BarcodeScanner barcodeScanner = BarcodeScanning.getClient(options);
        InputImage image = InputImage.fromBitmap(imageBitmap, 0);
        barcodeScanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    Log.d("AddBarcode", "Success");
                    Log.d("AddBarcode", ""+barcodes.size());
                    if (barcodes.size() > 0) {
                        // Get the first barcode (assuming there is only one in the image)
                        Barcode barcode = barcodes.get(0);
                        // Extract information from the barcode
                        String barcodeValue = barcode.getDisplayValue();
                        Log.d("AddBarcode", barcodeValue);
                        // Upload barcode information to Firestore
                        uploadBarcodeToFirestore(barcodeValue, description, make, estimatedValue);
                    }
                    else{
                        Toast.makeText(BarcodeActivity.this, "Barcode not found in this image (Format not supported)", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * Uploads barcode information to Firestore.
     *
     * @param barcodeValue    The value of the scanned barcode.
     * @param description     The description of the item.
     * @param make            The make of the item.
     * @param estimatedValue  The estimated value of the item.
     */
    public void uploadBarcodeToFirestore(String barcodeValue, String description, String make, Double estimatedValue){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference barcodesRef = db.collection("users").document(userId).collection("barcodes");

        barcodesRef.whereEqualTo("barcodeValue", barcodeValue)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Barcode with the same value already exists
                            Toast.makeText(BarcodeActivity.this, "Barcode already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            // Create a map with the barcode information
                            Map<String, Object> barcodeData = new HashMap<>();
                            barcodeData.put("barcodeValue", barcodeValue);
                            barcodeData.put("description", description);
                            barcodeData.put("make", make);
                            barcodeData.put("estimatedValue", estimatedValue);

                            // Add the document to the "barcodes" collection
                            barcodesRef// Use the barcode value as the document ID
                                    .add(barcodeData)
                                    .addOnSuccessListener(aVoid -> {
                                        // Handle success
                                        Toast.makeText(BarcodeActivity.this, "Barcode information uploaded successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure
                                        Toast.makeText(BarcodeActivity.this, "Failed to upload barcode information", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }else{
                        Toast.makeText(BarcodeActivity.this, "Error checking barcode existence", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}