package com.example.onestopshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * InventoryActivity displays the user's inventory, allowing them to view, add, and manage items.
 */
public class InventoryActivity extends AppCompatActivity implements InventoryController.OnInventoryUpdateListener{
    private ArrayList<Item> dataList;

    // Add this field in InventoryActivity
    private String currentMakeFilter = null;
    private ArrayAdapter adapter;
    private RecyclerView recyclerView;
    private Switch filterSwitch;
    private CustomList itemAdapter;
    private InventoryController inventoryController;

    private FirebaseFirestore db;
    private ImageView sortButton;
    private CollectionReference itemsRef;

    private TextView totalValueTextView;
    private double totalEstimatedValue;
    private ImageView addButton;
    private ImageView profileButton;

    private long dateBefore;
    private long dateAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        inventoryController = new InventoryController(); // Initialize the Inventory Controller
        inventoryController.setListener(this);
        dataList = new ArrayList<>();




        totalValueTextView = findViewById(R.id.total_estimated_value);
        recyclerView = findViewById(R.id.item_list);
        filterSwitch = findViewById(R.id.switch2);
        addButton = findViewById(R.id.add_button);
        profileButton = findViewById(R.id.profile_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new CustomList(this, dataList);
        recyclerView.setAdapter(itemAdapter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, AddItemActivity.class));
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, UserProfileActivity.class));
            }
        });
        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    applyFilter();
                } else {
                    clearFilter();
                }
            }
        });

        sortButton = findViewById(R.id.sort_button);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, FilterOrSort.class));
            }
        });
        Intent intent = getIntent();
        currentMakeFilter = intent.getStringExtra("MAKE_FILTER");


        Log.e("InventoryActivity", "Current Make Filter: " + currentMakeFilter);

        if (currentMakeFilter != null && !currentMakeFilter.isEmpty()) {
            filterSwitch.setChecked(true);
            fetchDataFilteredAndSortedByMake(currentMakeFilter);
        }


        // Use these dates to filter your data
    }





    private void fetchDataFilteredAndSortedByMake(String makeFilter) {
        // Call the method from the InventoryController
        inventoryController.fetchDataFilteredAndSortedByMake(makeFilter);
    }


    private void applyFilter() {
        Log.e("InventoryActivity", "make: "+ currentMakeFilter);
        filterSwitch.setChecked(true);
        if (currentMakeFilter != null && !currentMakeFilter.isEmpty()) {
            fetchDataFilteredAndSortedByMake(currentMakeFilter);
        }

    }

    private void clearFilter() {
        filterSwitch.setChecked(false);
        fetchDataOriginal();
    }
    private void fetchDataOriginal() {
        inventoryController.fetchAllData();
    }









    // Inside InventoryActivity



    /**
     * Callback method invoked when the inventory data is changed.
     *
     * @param updatedData The updated list of items in the inventory.
     */

    @Override
    public void onInventoryDataChanged(ArrayList<Item> updatedData) {
        dataList.clear();
        dataList.addAll(updatedData);
        totalEstimatedValue = 0;
        for(Item item : dataList) {
            totalEstimatedValue += item.getEstimatedValue();

        }
        totalValueTextView.setText("$" + String.format("%.2f", totalEstimatedValue));
        itemAdapter.notifyDataSetChanged();
    }


    // Inside InventoryActivity






}
