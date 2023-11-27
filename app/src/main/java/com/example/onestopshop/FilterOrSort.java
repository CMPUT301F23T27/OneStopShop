package com.example.onestopshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FilterOrSort extends AppCompatActivity implements InventoryController.OnInventoryUpdateListener {

    private ArrayList<Item> dataList;
    private RecyclerView recyclerView;
    private CustomList itemAdapter;
    private InventoryController inventoryController;
    private Spinner sortSpinner;
    private ImageButton switchSortButton; // Separate button for ascending/descending
    private boolean isAscending = true;
    private EditText makeEditText;
    private AppCompatButton applyFilterButton;
    private String currentFilterText = "";

    private ImageButton cancelButton;

    private AppCompatButton resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_or_filter);

        recyclerView = findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        inventoryController = new InventoryController();
        inventoryController.setListener(this);

        dataList = new ArrayList<>();

        sortSpinner = findViewById(R.id.sort_spinner);
        switchSortButton = findViewById(R.id.switch_sort); // Separate button for ascending/descending

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new CustomList(this, dataList);
        recyclerView.setAdapter(itemAdapter);
        makeEditText = findViewById(R.id.make); // Replace with your actual EditText ID for entering the make
        applyFilterButton = findViewById(R.id.select_button);
        cancelButton = findViewById(R.id.sort_cancel);
        resetButton = findViewById(R.id.reset_button);

        if (!currentFilterText.isEmpty()) {
            makeEditText.setText(currentFilterText);
        }
        SharedPreferences preferences = getSharedPreferences("FilterPreferences", MODE_PRIVATE);
        String savedFilterText = preferences.getString("filterText", "");

        // Set the filter text in the EditText
        makeEditText.setText(savedFilterText);

        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentFilterText = makeEditText.getText().toString();
                Intent intent = new Intent(FilterOrSort.this, InventoryActivity.class);
                intent.putExtra("MAKE_FILTER", currentFilterText); // Pass the make filter
                SharedPreferences preferences = getSharedPreferences("FilterPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filterText", currentFilterText);
                editor.apply();
                startActivity(intent);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEditText.setText("");
            }
        });

        // Create an ArrayAdapter for the sort Spinner
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.sort_criteria,
                android.R.layout.simple_spinner_item
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the sorting based on the selected item
                String selectedSortCriteria = parentView.getItemAtPosition(position).toString();
                sortItemList(selectedSortCriteria);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here for now
            }
        });

        switchSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle between ascending and descending
                isAscending = !isAscending;
                sortItemList(sortSpinner.getSelectedItem().toString());
                updateSwitchSortButtonAppearance();
            }
        });
    }

    @Override
    public void onInventoryDataChanged(ArrayList<Item> updatedData) {
        dataList.clear();
        dataList.addAll(updatedData);
        itemAdapter.notifyDataSetChanged();
    }

    private void sortItemList(String selectedSortCriteria) {
        // Extract sorting details
        String sortField = selectedSortCriteria.split(" ")[0];

        // Use the appropriate comparator based on the selected criteria
        switch (sortField) {
            case "Date":
                sortItemsByDate();
                break;
            case "Description":
                sortItemsByDescription();
                break;
            case "Make":
                sortItemsByMake();
                break;
            case "Estimated":
                sortItemsByEstimatedValue();
                break;
            // Handle additional sorting criteria if needed
        }

        // Notify the adapter of the data change
        itemAdapter.notifyDataSetChanged();
    }

    private void updateSwitchSortButtonAppearance() {
        if (isAscending) {
            switchSortButton.setImageResource(R.drawable.rsz_arrow_copy);
        } else {
            switchSortButton.setImageResource(R.drawable.rsz_arrow);
        }
    }

    private void sortItemsByDate() {
        Collections.sort(dataList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return item1.getPurchaseDate().compareTo(item2.getPurchaseDate());
            }
        });
        if (!isAscending) {
            Collections.reverse(dataList);
        }
    }

    private void sortItemsByDescription() {
        Collections.sort(dataList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                String description1 = item1.getDescription();
                String description2 = item2.getDescription();

                // Handle null descriptions
                if (description1 == null && description2 == null) {
                    return 0;
                } else if (description1 == null) {
                    return -1;
                } else if (description2 == null) {
                    return 1;
                }

                int result = description1.compareTo(description2);

                // Adjust result based on the ascending order
                return isAscending ? result : -result;
            }
        });

        // Reverse the list
        Collections.reverse(dataList);
    }

    private void sortItemsByMake() {
        Collections.sort(dataList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                // Check if the make contains a number
                String make1 = item1.getMake();
                String make2 = item2.getMake();

                if (make1 == null && make2 == null) {
                    return 0;
                } else if (make1 == null) {
                    return -1;
                } else if (make2 == null) {
                    return 1;
                }

                int result = make1.compareTo(make2);

                // Adjust result based on the ascending order
                return isAscending ? result : -result;
            }
        });

        // Reverse the list
        Collections.reverse(dataList);
    }

    private void sortItemsByEstimatedValue() {
        Collections.sort(dataList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return Double.compare(item1.getEstimatedValue(), item2.getEstimatedValue());
            }
        });
        if (!isAscending) {
            Collections.reverse(dataList);
        }
    }
}
