package com.example.onestopshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;


import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Button btnTakePhoto = findViewById(R.id.btnTakePhoto);
        Button btnChooseFromGallery = findViewById(R.id.btnChooseFromGallery);

        btnTakePhoto.setOnClickListener(view -> dispatchTakePictureIntent());
        btnChooseFromGallery.setOnClickListener(view -> dispatchPickImageIntent());

        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        processImageWithMLKit(imageBitmap);
                    }
                });

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            processImageWithMLKit(imageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        }
    }

    private void dispatchPickImageIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageLauncher.launch(pickImageIntent);
    }

    private void processImageWithMLKit(Bitmap imageBitmap) {
        TextRecognizer textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        if (imageBitmap != null) {
            InputImage inputImage = InputImage.fromBitmap(imageBitmap, 0);
            textRecognizer.process(inputImage)
                    .addOnSuccessListener(visionText -> {
                        String resultText = visionText.getText();
                        extractSerialNumber(resultText);
                    })
                    .addOnFailureListener(e -> {
                        if (e instanceof MlKitException) {
                            // Handle ML Kit errors
                            MlKitException mlKitException = (MlKitException) e;
                            Toast.makeText(this, "ML Kit Error: " + mlKitException.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void extractSerialNumber(String resultText) {
        // Pattern to match a sequence of digits or an uppercase letter followed by digits until a space is encountered
        String pattern = "(?:(?=[A-Z])[A-Z0-9]*\\d[A-Z0-9]*|\\d{6,})";
        Pattern serialNumberPattern = Pattern.compile(pattern);
        Matcher matcher = serialNumberPattern.matcher(resultText);

        if (matcher.find()) {
            String extractedSerialNumber = matcher.group();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("serialNumber", extractedSerialNumber);
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(this, "Serial Number: " + extractedSerialNumber, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "No valid serial number found in the text.", Toast.LENGTH_LONG).show();
        }

    }

}