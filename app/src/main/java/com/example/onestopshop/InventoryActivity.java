package com.example.onestopshop;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;


public class InventoryActivity extends AppCompatActivity {
    private ArrayList<Item> dataList;
    private RecyclerView recyclerView;
    private CustomList itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        dataList = new ArrayList<>();
        // Example items
        Item item1 = new Item("Phone", new Date(), 1070.744, "Tag A");
        Item item2 = new Item("Shirt", new Date(), 150.0, "Tag B");
        Item item3 = new Item("Pants", new Date(), 120.0, "Tag A");
        dataList.add(item1);
        dataList.add(item2);
        dataList.add(item3);


        recyclerView = findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new CustomList(this, dataList);
        recyclerView.setAdapter(itemAdapter);

    }

}
