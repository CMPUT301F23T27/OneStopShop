package com.example.onestopshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * MainActivity serves as the entry point of the application, handling user authentication and navigation to InventoryActivity.
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            // If not signed in, start the authentication flow
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            // When the app starts, launch the InventoryActivity
            Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
            startActivity(intent);
        }

    }
}










