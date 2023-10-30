package com.example.onestopshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // When the app starts, launch the InventoryActivity
        Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
        startActivity(intent);
    }
}