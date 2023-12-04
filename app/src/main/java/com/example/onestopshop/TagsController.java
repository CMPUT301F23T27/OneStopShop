package com.example.onestopshop;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagsController {
    private CollectionReference itemsRef;

    public TagsController() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        itemsRef = db.collection("users").document(userId).collection("items");
    }
    public void fetchExistingTags(OnTagsFetchListener listener) {
        // Fetch existing tags from Firestore
        itemsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> existingTags = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Check if 'tags' field exists in the document
                        if (document.contains("tags")) {
                            List<String> itemTags = (List<String>) document.get("tags");
                            existingTags.addAll(itemTags);
                        }
                    }

                    // Remove duplicates by converting to a Set and back to a List
                    Set<String> uniqueTagsSet = new HashSet<>(existingTags);
                    List<String> uniqueTagsList = new ArrayList<>(uniqueTagsSet);

                    // Invoke the listener's onSuccess method with the existing tags
                    listener.onSuccess(uniqueTagsList);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    listener.onFailure(e);
                });
    }
    /*public void uploadNewTagsToTagsField(List<String> selectedTags) {

        // Get the current document to check if the 'tags' field exists
        usersRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, check if 'tags' field exists
                        if (documentSnapshot.contains("tags")) {
                            String[] selectedTagsArray = selectedTags.toArray(new String[0]);
                            // 'tags' field exists, update it
                            usersRef
                                    .update("tags", FieldValue.arrayUnion(selectedTagsArray))
                                    .addOnSuccessListener(aVoid -> {
                                        // Tag added successfully
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the failure to add the tag
                                    });
                        } else {
                            // 'tags' field does not exist, create it with the new tag
                            usersRef
                                    .update("tags", selectedTags)
                                    .addOnSuccessListener(aVoid -> {
                                        // Tag added successfully
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the failure to add the tag
                                    });
                        }
                    } else {
                        // Handle the case where the document does not exist
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }*/

    public interface OnTagsFetchListener {
        void onSuccess(List<String> existingTags);

        void onFailure(Exception e);
    }

}
