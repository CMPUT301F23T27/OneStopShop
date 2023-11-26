package com.example.onestopshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class FilterOrSort extends AppCompatActivity {

    private EditText makeEditText;
    private AppCompatButton applyFilterButton;
    private String currentFilterText = "";

    private ImageButton cancelButton;

    private AppCompatButton resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_or_filter); // Make sure this layout has the necessary EditText and Button

        makeEditText = findViewById(R.id.make); // Replace with your actual EditText ID for entering the make
        applyFilterButton = findViewById(R.id.select_button);
        cancelButton = findViewById(R.id.sort_cancel);
        resetButton = findViewById(R.id.reset_button);


        if (!currentFilterText.isEmpty()) {
            makeEditText.setText(currentFilterText);
        }
        SharedPreferences preferences = getSharedPreferences("FilterPreferences", MODE_PRIVATE);
        String savedFilterText = preferences.getString("filterText", "");

        // Set the filter text in the EditText
        makeEditText.setText(savedFilterText);


        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentFilterText = makeEditText.getText().toString();
                Intent intent = new Intent(FilterOrSort.this, InventoryActivity.class);
                intent.putExtra("MAKE_FILTER", currentFilterText); // Pass the make filter
                SharedPreferences preferences = getSharedPreferences("FilterPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filterText", currentFilterText);
                editor.apply();
                startActivity(intent);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEditText.setText("");
            }
        });
    }


}
