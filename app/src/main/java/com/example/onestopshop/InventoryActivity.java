package com.example.onestopshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * InventoryActivity displays the user's inventory, allowing them to view, add, and manage items.
 */
public class InventoryActivity extends AppCompatActivity implements InventoryController.OnInventoryUpdateListener{
    private ArrayList<Item> dataList;
    private RecyclerView recyclerView;
    private CustomList itemAdapter;
    private InventoryController inventoryController;
    private TextView totalValueTextView;
    private double totalEstimatedValue;
    private ImageView addButton;
    private ImageView profileButton;
    private Button selectButton;
    private CheckBox itemCheckBox;
    private boolean checkboxVisible;

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
        selectButton = findViewById(R.id.select_button);
        checkboxVisible = false;
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
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxVisible = !checkboxVisible;
                selectButton.setText(checkboxVisible ? "DESELECT" : "SELECT");
                updateCheckboxVisibility(recyclerView);
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

    /**
     * Changes the visibility of checkboxes
     *
     * @param recycler recyclerview containing checkbox
     */
    private void updateCheckboxVisibility(RecyclerView recycler) {
        for (int i = 0; i < recycler.getChildCount(); i++) {
            View itemView = recycler.getChildAt(i);
            CheckBox checkBox = itemView.findViewById(R.id.itemCheckBox);

            // OnClickListener sets checkboxVisible to what we want
            if (checkBox != null) {
                if (checkboxVisible) {
                    checkBox.setVisibility(View.VISIBLE);
                } else {
                    checkBox.setVisibility(View.INVISIBLE);
                }
            }
        }

        // Notify the adapter
        recycler.getAdapter().notifyDataSetChanged();
    }
}
