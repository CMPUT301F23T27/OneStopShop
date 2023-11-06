package com.example.onestopshop;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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


public class InventoryActivity extends AppCompatActivity implements InventoryController.OnInventoryUpdateListener{
    private ArrayList<Item> dataList;
    private RecyclerView recyclerView;
    private CustomList itemAdapter;
<<<<<<< HEAD
    private InventoryController inventoryController;
=======
    private FirebaseFirestore db;
    private CollectionReference itemsRef;
    private TextView totalValueTextView;
    private double totalEstimatedValue;
>>>>>>> 9e8737a6299f7bc19f9bb9d8d6c85d7a9f80d357

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        inventoryController = new InventoryController(); // Initialize the Inventory Controller
        inventoryController.setListener(this);
        dataList = new ArrayList<>();


        recyclerView = findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new CustomList(this, dataList);
        recyclerView.setAdapter(itemAdapter);

<<<<<<< HEAD

=======
        totalValueTextView = findViewById(R.id.total_estimated_value);

        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    dataList.clear();
                    totalEstimatedValue = 0;
                    for (QueryDocumentSnapshot doc: querySnapshots) {
                        String itemName = doc.getString("itemName");
                        String purchaseDate = doc.getString("purchaseDate");
                        double estimatedValue = doc.getDouble("estimatedValue");
                        totalEstimatedValue += estimatedValue;
                        ArrayList<String> tags = (ArrayList<String>) doc.get("tags");
                        Log.d("Firestore", String.format("Item(%s, %s) fetched",
                                itemName, purchaseDate));
                        dataList.add(new Item(itemName, purchaseDate, estimatedValue, tags));
                    }
                    itemAdapter.notifyDataSetChanged();
                    totalValueTextView.setText("$" + totalEstimatedValue);
                }
            }
        });
>>>>>>> 9e8737a6299f7bc19f9bb9d8d6c85d7a9f80d357



    }

    @Override
    public void onInventoryDataChanged(ArrayList<Item> updatedData) {
        dataList.clear();
        dataList.addAll(updatedData);
        itemAdapter.notifyDataSetChanged();
    }
}
