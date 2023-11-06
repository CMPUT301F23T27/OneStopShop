package com.example.onestopshop;

import android.os.Bundle;
import android.util.Log;
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


public class InventoryActivity extends AppCompatActivity implements InventoryController.OnInventoryUpdateListener{
    private ArrayList<Item> dataList;
    private RecyclerView recyclerView;
    private CustomList itemAdapter;
    private InventoryController inventoryController;

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



    }

    @Override
    public void onInventoryDataChanged(ArrayList<Item> updatedData) {
        dataList.clear();
        dataList.addAll(updatedData);
        itemAdapter.notifyDataSetChanged();
    }
}
