package com.example.onestopshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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


public class InventoryActivity extends AppCompatActivity {
    private ArrayList<Item> dataList;
    private RecyclerView recyclerView;
    private CustomList itemAdapter;
    private FirebaseFirestore db;
    private CollectionReference itemsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");
        dataList = new ArrayList<>();


        recyclerView = findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new CustomList(this, dataList, new CustomList.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent = new Intent(InventoryActivity.this, ViewItem.class);
                intent.putExtra("ITEM_ID", item.getId()); // Pass the clicked item's ID to ViewItem activity
                startActivity(intent);
            }

        });

        recyclerView.setAdapter(itemAdapter);



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
                    for (QueryDocumentSnapshot doc: querySnapshots) {
                        String itemName = doc.getString("itemName");
                        String description = doc.getString("description");
                        String purchaseDate = doc.getString("purchaseDate");
                        String make = doc.getString("make");
                        String model = doc.getString("model");
                        double estimatedValue = doc.getDouble("estimatedValue");
                        String comment = doc.getString("comment");
                        String serialNumber = doc.getString("serialNumber");
                        ArrayList<String> tags = (ArrayList<String>) doc.get("tags");
                        Log.d("Firestore", String.format("Item(%s, %s) fetched",
                                itemName, purchaseDate));
                        // Create a new Item object with all fields
                        Item item = new Item(itemName, description, purchaseDate, make, model, estimatedValue, comment, serialNumber, tags);
//                        dataList.add(new Item(itemName, purchaseDate, estimatedValue, tags));
                        item.setId(doc.getId()); // Set the document ID from Firestore
                        dataList.add(item);
                    }
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });

    }

}
