package com.example.onestopshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // When the app starts, launch the profile
        Intent intent = new Intent(MainActivity.this, profile.class);
        startActivity(intent);
    }
}