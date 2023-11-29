package com.example.onestopshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class InventoryActivity extends AppCompatActivity implements InventoryController.OnInventoryUpdateListener{
    private ArrayList<Item> dataList;

    // Add this field in InventoryActivity
    private String currentMakeFilter = null;
    private ArrayAdapter adapter;
    private RecyclerView recyclerView;
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

        sortButton = findViewById(R.id.sort_button);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, FilterOrSort.class));
            }
        });
        Intent intent = getIntent();
        currentMakeFilter = intent.getStringExtra("MAKE_FILTER");
        String dateB = intent.getStringExtra("DATE_BEFORE");
        String dateA = intent.getStringExtra("DATE_AFTER");

        Log.e("InventoryActivity", "Current Make Filter: " + currentMakeFilter);
        Log.e("InventoryActivity", "Current Before: " + dateB);
        Log.e("InventoryActivity", "After: " + dateA);
        if (currentMakeFilter != null && !currentMakeFilter.isEmpty()) {
            fetchDataFilteredAndSortedByMake(currentMakeFilter);
        }

        if (dateA != null && !dateA.isEmpty() && dateB != null && !dateB.isEmpty()) {
            fetchDataFilteredAndSortedByDate(dateA,dateB);
        }


        // Use these dates to filter your data
    }



    private void fetchDataFilteredAndSortedByDate(String date_a,String date_b){
        inventoryController.fetchDataFilteredAndSortedByDate(date_a,date_b);
    }

    private void fetchDataFilteredAndSortedByMake(String makeFilter) {
        // Call the method from the InventoryController
        inventoryController.fetchDataFilteredAndSortedByMake(makeFilter);
    }









    // Inside InventoryActivity


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
