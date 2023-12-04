package com.example.onestopshop;

import androidx.appcompat.app.AppCompatActivity;


import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

import java.util.List;

/**
 * Activity for adding a new item to the inventory.
 */
public class AddItemActivity extends AppCompatActivity {

    private List<Uri> localUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        localUris = new ArrayList<>();
        loadAddFragment();
    }

    private void loadAddFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddItemFragment())
                .addToBackStack(null) // Add to back stack for navigation
                .commit();
    }

    public List<Uri> getUriList() {
        return localUris;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}

