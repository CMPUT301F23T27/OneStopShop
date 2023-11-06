package com.example.onestopshop;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class InventoryController {
    private CollectionReference itemsRef;
    private OnInventoryUpdateListener listener;

    public InventoryController() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");

        itemsRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                // Handle errors
                return;
            }
            if (queryDocumentSnapshots != null) {
                ArrayList<Item> updatedData = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String itemName = doc.getString("itemName");
                    String purchaseDate = doc.getString("purchaseDate");
                    double estimatedValue = doc.getDouble("estimatedValue");
                    // Assuming 'tags' is an ArrayList stored in Firestore
                    ArrayList<String> tags = (ArrayList<String>) doc.get("tags");

                    Item item = new Item(itemName, purchaseDate, estimatedValue, tags);
                    updatedData.add(item);
                }
                if (listener != null) {
                    listener.onInventoryDataChanged(updatedData);
                }
            }
        });
    }

    public void setListener(OnInventoryUpdateListener listener) {
        this.listener = listener;
    }

    public interface OnInventoryUpdateListener {
        void onInventoryDataChanged(ArrayList<Item> updatedData);
    }
}
