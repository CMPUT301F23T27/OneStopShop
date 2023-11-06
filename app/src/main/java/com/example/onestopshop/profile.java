// ProfileActivity.java
package com.example.onestopshop;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView tvName, tvEmail;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        profileImage = findViewById(R.id.profileImage);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);

        db = FirebaseFirestore.getInstance();

        loadUserProfile();
    }

    private void loadUserProfile() {
        // TODO: Replace with the actual user ID to fetch from Firestore
        String userId = "PgoaOvxqnhlklpQlRd6P";

        db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Userprofile userProfile = document.toObject(Userprofile.class);
                        if (userProfile != null) {
                            tvName.setText("Name:-  " + userProfile.getName());
                            tvEmail.setText("Email:-  " + userProfile.getEmail());
                            if (userProfile.getImageUrl() != null && !userProfile.getImageUrl().isEmpty()) {
                                Glide.with(profile.this)
                                        .load(userProfile.getImageUrl())
                                        .into(profileImage);
                            }
                        }
                    } else {
                        Toast.makeText(profile.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(profile.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
