package com.example.onestopshop;

import androidx.appcompat.app.AppCompatActivity;


import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

import java.util.List;

/**
 * Activity for managing fragments to add a new item to the inventory.
 */
public class AddItemActivity extends AppCompatActivity {

    private List<Uri> localUris;

    /**
     * Initializes the activity and sets the content view.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        localUris = new ArrayList<>();
        loadAddFragment();
    }

    /**
     * Loads the fragment responsible for adding a new item.
     */
    private void loadAddFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddItemFragment())
                .addToBackStack(null) // Add to back stack for navigation
                .commit();
    }

    /**
     * Retrieves the list of URIs associated with the added items.
     *
     * @return The list of URIs.
     */
    public List<Uri> getUriList() {
        return localUris;
    }

    /**
     * Overrides the default behavior of the back button.
     * If there are fragments in the back stack, pops the top fragment;
     * otherwise, performs the default back action.
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}

