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

/**
 * Class for TagsController that fetches tags from firestore
 */
public class TagsController {
    private CollectionReference itemsRef;

    public TagsController() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        itemsRef = db.collection("users").document(userId).collection("items");
    }

    /**
     * Fetches existing tags from Firestore and invokes the listener's onSuccess method with the unique tags.
     *
     * @param listener The listener to handle the result of tag fetch operation.
     */
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

    /**
     * Uploads a tag to the specified item in Firestore.
     *
     * @param itemId The ID of the item to which the tag is added.
     * @param tag    The tag to be added to the item.
     */
    public void uploadTag(String itemId, String tag) {
        itemsRef.document(itemId).update("tags", FieldValue.arrayUnion(tag))
                .addOnSuccessListener(documentReference -> {

                })
                .addOnFailureListener(e -> {
                    // Handle failure to add the item to Firestore
                });
    }

    /**
     * The interface to listen for tag fetch operations.
     */
    public interface OnTagsFetchListener {
        void onSuccess(List<String> existingTags);

        void onFailure(Exception e);
    }

}
