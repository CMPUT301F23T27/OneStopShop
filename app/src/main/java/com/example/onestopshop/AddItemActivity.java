package com.example.onestopshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
                .replace(R.id.fragment_container, new AddFragment())
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

