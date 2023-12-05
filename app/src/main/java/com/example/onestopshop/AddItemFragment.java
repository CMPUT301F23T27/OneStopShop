package com.example.onestopshop;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Fragment to add an item to the inventory
 */
public class AddItemFragment extends Fragment {

    // Inventory controller to manage inventory data
    private InventoryController inventoryController;
    private String itemId;
    private PhotosController photosController;
    private TagsController tagsController;
    private List<Uri> localUris;

    // UI elements
    private EditText itemNameText;
    private EditText descriptionText;
    private EditText purchaseDateText;
    private EditText makeText;
    private EditText modelText;
    private EditText serialNumberText;
    private EditText estimatedValueText;
    private List<String> selectedTags;
    private ChipGroup tagsChipGroup;
    //private EditText tagsText;
    private TextView addTagBtn;
    private EditText commentsText;
    private ImageView itemPhotoView;
    private Button btnCancel;
    private Button confirm;
    private ImageView scanButton;
    private Button scanBarcodeButton;
    private ImageButton addPhoto;

    // Activity result launcher for handling results from ScanActivity
    private final ActivityResultLauncher<Intent> startForSerialNumberResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == requireActivity().RESULT_OK) {
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
                if (result.getResultCode() == requireActivity().RESULT_OK) {
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

    public AddItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        // Initialize the 'Confirm' button
        confirm = view.findViewById(R.id.btn_add_item);

        // Initialize Views
        itemPhotoView = view.findViewById(R.id.productImage);
        itemNameText = view.findViewById(R.id.itemName);
        descriptionText = view.findViewById(R.id.description);
        purchaseDateText = view.findViewById(R.id.purchaseDate);
        makeText = view.findViewById(R.id.make);
        modelText = view.findViewById(R.id.model);
        serialNumberText = view.findViewById(R.id.serialnumber);
        estimatedValueText = view.findViewById(R.id.estimatedValue);
        scanBarcodeButton = view.findViewById(R.id.btn_scan_barcode);
        //tagsText = view.findViewById(R.id.tags);
        tagsChipGroup = view.findViewById(R.id.addFragmentTagsChipGroup);
        addTagBtn = view.findViewById(R.id.add_tag_button);
        commentsText = view.findViewById(R.id.comments);
        btnCancel = view.findViewById(R.id.btnCancel);
        scanButton = view.findViewById(R.id.scanButton);
        addPhoto = view.findViewById(R.id.add_image_button);
        localUris = new ArrayList<>();
        selectedTags = new ArrayList<>();

        localUris = ((AddItemActivity) requireActivity()).getUriList();
        tagsController = new TagsController();
        // Set up click listener for the 'Scan' button
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ScanActivity.class);
                startForSerialNumberResult.launch(intent);
            }
        });

        // Set up click listener for the 'Confirm' button
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract data from UI elements
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
                        Toast.makeText(getContext(), "Invalid Estimated Value", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else {
                    Toast.makeText(getContext(), "Invalid Estimated Value", Toast.LENGTH_SHORT).show();
                    return;
                }
                //List<String> tags = Arrays.asList(tagsText.getText().toString().split(","));
                String comments = commentsText.getText().toString();

                // Validate the item
                boolean validItem = validItem(new Item(itemName, description, purchaseDate, make,
                        model, estimatedValue, comments, serialNumber, selectedTags));

                // If the item is valid, add it to the inventory
                if (validItem) {
                    inventoryController = new InventoryController();

                    inventoryController.addItem(new Item(itemName, description, purchaseDate, make,
                            model, estimatedValue, comments, serialNumber, selectedTags), new InventoryController.ItemAddedCallback() {
                        @Override
                        public void onItemAdded(String addedItemId) {
                            // Update the itemId variable
                            itemId = addedItemId;
                            //Upload photos to Firebase
                            photosController = new PhotosController(itemId);
                            //tagsController.uploadNewTagsToTagsField(selectedTags);
                            String tag = "AddItemActivity";
                            Log.d(tag, "" + localUris.size());
                            Log.d(tag, "The itemId is: " + itemId);
                            if(localUris.size() > 0) {
                                photosController.uploadAllPhotos(localUris);
                            }

                        }
                    });
                    requireActivity().finish();
                }
            }
        });

        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsController.fetchExistingTags(new TagsController.OnTagsFetchListener() {
                    @Override
                    public void onSuccess(List<String> existingTags) {
                        // send existing tags to dialog
                        TagDialog tagDialog = new TagDialog(requireContext(),selectedTags, existingTags, tagsChipGroup);
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
        purchaseDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        // Set up click listener for the 'Cancel' button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGalleryFragment();
            }
        });
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), BarcodeActivity.class);
                scanBarcodeLauncher.launch(intent);
            }
        });

        return view;
    }

    /**
     * Validates the item before adding it to the inventory.
     *
     * @return True if the item is valid, false otherwise.
     */
    public boolean validItem(Item item) {
        boolean valid = true;
        if(item.getItemName() == null || item.getItemName().isEmpty()) {
            Toast.makeText(getContext(), "Invalid Product Name", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(item.getDescription() == null || item.getDescription().isEmpty()) {
            Toast.makeText(getContext(), "Invalid Description", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(TextUtils.isEmpty(item.getPurchaseDate())){
            Toast.makeText(getContext(), "Invalid Purchase Date", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(item.getMake() == null || item.getMake().isEmpty()) {
            Toast.makeText(getContext(), "Invalid Make", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(item.getModel() == null || item.getModel().isEmpty()) {
            Toast.makeText(getContext(), "Invalid Model", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(item.getEstimatedValue() < 0) {
            Toast.makeText(getContext(), "Value must be greater than 0", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(selectedTags.size() == 0) {
            Toast.makeText(getContext(), "Must have at least one tag", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
    /**
     * Launches the gallery fragment for adding images.
     */
    public void launchGalleryFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new GalleryFragment())
                .addToBackStack(null)
                .commit();
    }
    /**
     * Shows a date picker dialog for selecting the purchase date.
     *
     * @param v The view that triggered the date picker.
     */
    public void showDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Update the EditText with the selected date
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    purchaseDateText.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }
    /**
     * Resumes the fragment, updating the item photo if available.
     */
    @Override
    public void onResume() {
        super.onResume();
        String tag = "AddItemActivity";
        Log.d(tag, "" + localUris.size());
        if(localUris.size() > 0) {
            Picasso.get().load(localUris.get(0)).into(itemPhotoView);
            itemPhotoView.setBackgroundColor(0);
        }
    }

}