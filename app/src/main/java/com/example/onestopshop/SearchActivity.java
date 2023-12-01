package com.example.onestopshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private InventoryController inventoryController;

//    TextView textview;

    private ArrayList<Item> dataList;
    private RecyclerView recyclerView;
    private CustomList itemAdapter;

//    private ImageView addButton;
//    private ImageView profileButton;

    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        inventoryController = new InventoryController();

        dataList = new ArrayList<>();

        EditText searchField = findViewById(R.id.search);
        Button searchButton = findViewById(R.id.bsearch);
//         textview = findViewById(R.id.responsetext);
        recyclerView = findViewById(R.id.itemlist);
//        addButton = findViewById(R.id.add_button);
//        profileButton = findViewById(R.id.profile_button);
        backButton = findViewById(R.id.back);

//        textview.setText("hello world");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new CustomList(this, dataList);
        recyclerView.setAdapter(itemAdapter);
        searchButton.setOnClickListener(view -> {
//            textview.setText("hello");
            String keyword = searchField.getText().toString().toLowerCase();
            SearchByKeyword(keyword);
        });

//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SearchActivity.this, AddItemActivity.class));
//            }
//        });
//        profileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SearchActivity.this, UserProfileActivity.class));
//            }
//        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the Search to go back to the previous screen
                finish();
            }
        });
    }

    private void SearchByKeyword(String keyword) {
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