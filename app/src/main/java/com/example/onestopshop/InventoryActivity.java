package com.example.onestopshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * InventoryActivity displays the user's inventory, allowing them to view, add, and manage items.
 */
public class InventoryActivity extends AppCompatActivity implements InventoryController.OnInventoryUpdateListener{
    private ArrayList<Item> dataList;
    private RecyclerView recyclerView;
    private CustomList itemAdapter;
    private boolean shouldFilter;
    private ImageButton filterButton;
    private InventoryController inventoryController;
    private TextView totalValueTextView;
    private double totalEstimatedValue;
    private String startDate, endDate, makeFilter;
    private ArrayList<String> tagsFilter;
    private ImageView addButton;
    private ImageView profileButton;
    private final ActivityResultLauncher<Intent> filterActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == this.RESULT_OK) {
                    // Retrieve data from the intent returned by ScanActivity
                    Intent filtersIntent = result.getData();
                    boolean clear = filtersIntent.getBooleanExtra("clear", false);
                    if(clear) {
                        recyclerView.setAdapter(itemAdapter);
                        totalEstimatedValue = 0;
                        for(Item item : dataList) {
                            totalEstimatedValue += item.getEstimatedValue();
                        }
                        totalValueTextView.setText("$" + String.format("%.2f", totalEstimatedValue));
                        startDate = "";
                        endDate = "";
                        makeFilter = "";
                        tagsFilter = new ArrayList<>();
                        return;
                    }
                    startDate = filtersIntent.getStringExtra("startDate");
                    endDate = filtersIntent.getStringExtra("endDate");
                    makeFilter = filtersIntent.getStringExtra("make");
                    tagsFilter = filtersIntent.getStringArrayListExtra("tags");
                    ArrayList<Item> filteredData = filterData(dataList, startDate, endDate, makeFilter, tagsFilter);
                    CustomList filteredItemsAdapter = new CustomList(this, filteredData);
                    recyclerView.setAdapter(filteredItemsAdapter);
                    totalEstimatedValue = 0;
                    for(Item item : filteredData) {
                        totalEstimatedValue += item.getEstimatedValue();
                    }
                    totalValueTextView.setText("$" + String.format("%.2f", totalEstimatedValue));
                    filteredItemsAdapter.notifyDataSetChanged();

                    // Handle the received data
                } else {
                    // Handle when the result is not OK
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        startDate = "";
        endDate = "";
        makeFilter = "";
        tagsFilter = new ArrayList<>();
        inventoryController = new InventoryController(); // Initialize the Inventory Controller
        inventoryController.setListener(this);
        dataList = new ArrayList<>();
        filterButton = findViewById(R.id.filter_button);
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
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventoryActivity.this, FilterActivity.class);
                //give filter activity previous filters
                intent.putExtra("endDateFilter", endDate);
                intent.putExtra("startDateFilter", startDate);
                intent.putExtra("makeFilter", makeFilter);
                intent.putStringArrayListExtra("tagsFilter", tagsFilter);
                filterActivityLauncher.launch(intent);
            }
        });

    }

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
    public ArrayList<Item> filterData(ArrayList<Item> updatedData, String startDate, String endDate, String makeFilter, ArrayList<String> tagsFilter) {
        ArrayList<Item> filteredList = new ArrayList<>();
        boolean passFilter;
        Log.d("Debug", makeFilter);
        for (Item item : updatedData) {
            Log.d("Debug", item.getItemName());

            passFilter = true;

            // Check startDate filter
            if (!TextUtils.isEmpty(startDate)) {
                Log.d("Inside", "dB");
                passFilter = passFilter && item.getPurchaseDate().compareTo(startDate) >= 0;
            }

            // Check endDate filter
            if (!TextUtils.isEmpty(endDate)) {
                Log.d("Inside", "dA");
                passFilter = passFilter && item.getPurchaseDate().compareTo(endDate) <= 0;
            }

            // Check makeFilter
            if (!TextUtils.isEmpty(makeFilter)) {
                Log.d("Inside", "make");
                passFilter = passFilter && item.getMake().equals(makeFilter);

            }

            // Check tagsFilter
            if (tagsFilter != null && tagsFilter.size()>0) {
                Log.d("Inside", "tags:" + tagsFilter.size());
                passFilter = passFilter && itemContainsAnyTags(item, tagsFilter);
            }

            // If all conditions are met, add the item to the filtered list
            if (passFilter) {
                filteredList.add(item);
            }
        }
        Log.d("Debug", "" + filteredList.size());
        return filteredList;
    }
    private boolean itemContainsAnyTags(Item item, ArrayList<String> tagsFilter) {
        for (String tag : tagsFilter) {
            if (item.getTags().contains(tag)) {
                return true;
            }
        }
        return false;
    }

}
