package com.example.onestopshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity {
    private Button logOutButton;
    private Button backButton;
    private TextView displayName;
    private TextView email;
    private InventoryController inventoryController;
    GoogleSignInClient googleSignInClient;
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
        googleSignInClient = GoogleSignIn.getClient(UserProfileActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Check condition
                        if (task.isSuccessful()) {
                            // When task is successful sign out from firebase
                            FirebaseAuth.getInstance().signOut();
                            // Display Toast
                            Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                            // Finish activity
                            finish();
                        }
                    }
                });
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