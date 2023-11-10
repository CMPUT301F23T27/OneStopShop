package com.example.onestopshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity {
    private Button logOutButton;
    private Button backButton;
    private TextView displayName;
    private TextView email;
    private InventoryController inventoryController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        inventoryController = new InventoryController();
        logOutButton = findViewById(R.id.btnLogout);
        backButton = findViewById(R.id.buttonBack);
        email = findViewById(R.id.tvEmail);
        displayName = findViewById(R.id.tvName);
        email.setText("Email: " + inventoryController.getUserEmail());
        displayName.setText("Name: " + inventoryController.getUserName());
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                // Redirect the user to the LoginActivity
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: Finish the MainActivity to prevent going back via back button

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}