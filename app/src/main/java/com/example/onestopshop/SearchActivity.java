package com.example.onestopshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private InventoryController inventoryController;

    private ArrayList<Item> dataList;
    private RecyclerView recyclerView;
    private CustomList itemAdapter;

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        inventoryController = new InventoryController();

        dataList = new ArrayList<>();

        EditText searchField = findViewById(R.id.search);
        Button searchButton = findViewById(R.id.bsearch);
        recyclerView = findViewById(R.id.itemlist);
        backButton = findViewById(R.id.back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new CustomList(this, dataList);
        recyclerView.setAdapter(itemAdapter);

        searchButton.setOnClickListener(view -> {
            String keyword = searchField.getText().toString().toLowerCase();
            searchByKeyword(keyword);
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the Search to go back to the previous screen
                finish();
            }
        });
    }

    // Getter method for inventoryController
    public InventoryController getInventoryController() {
        return inventoryController;
    }

    // Setter method for inventoryController
    public void setInventoryController(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    // Package-private method for search by keyword
    void searchByKeyword(String keyword) {
        inventoryController.searchItemsByKeywords(keyword, new InventoryController.OnInventorySearchListener() {
            @Override
            public void onSearchAnswer(ArrayList<Item> updatedData) {
                dataList.clear();
                dataList.addAll(updatedData);
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void Error(String Message) {
                System.out.println(Message);
                dataList.clear();
            }
        });
    }
}
