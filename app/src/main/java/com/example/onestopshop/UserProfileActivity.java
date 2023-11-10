package com.example.onestopshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity {
    private Button logOutButton; // Button for logging out
    private Button backButton;   // Button for going back
    private TextView displayName; // TextView for displaying user's name
    private TextView email;       // TextView for displaying user's email
    private InventoryController inventoryController; // Controller for managing inventory data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize the inventory controller
        inventoryController = new InventoryController();

        // Initialize UI elements by finding them in the layout XML
        logOutButton = findViewById(R.id.btnLogout);
        backButton = findViewById(R.id.buttonBack);
        email = findViewById(R.id.tvEmail);
        displayName = findViewById(R.id.tvName);

        // Set the email and display name based on user data from the controller
        email.setText("Email: " + inventoryController.getUserEmail());
        displayName.setText("Name: " + inventoryController.getUserName());

        // Set an onClickListener for the log out button
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user from Firebase Authentication
                FirebaseAuth.getInstance().signOut();

                // Redirect the user to the LoginActivity
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);

                finish(); // Optional: Finish the UserProfileActivity to prevent going back via the back button
            }
        });

        // Set an onClickListener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the UserProfileActivity to go back to the previous screen
                finish();
            }
        });
    }
}
