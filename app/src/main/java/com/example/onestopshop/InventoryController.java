package com.example.onestopshop;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InventoryController {
    private CollectionReference itemsRef;
    private OnInventoryUpdateListener listener;

    public InventoryController() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");

        itemsRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                // Handle errors, e.g., log the error or display a message.
                return;
            }
            if (queryDocumentSnapshots != null) {
                ArrayList<Item> updatedData = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String itemId = doc.getString("itemId");
                    String itemName = doc.getString("itemName");
                    String purchaseDate = doc.getString("purchaseDate");
                    Double estimatedValue = doc.getDouble("estimatedValue");
                    ArrayList<String> tags = (ArrayList<String>) doc.get("tags");
                    if (itemId != null && itemName != null && purchaseDate != null && estimatedValue != null && tags != null) {
                        Item item = new Item(itemId, itemName, purchaseDate, estimatedValue, tags);
                        updatedData.add(item);
                    }
                }
                if (listener != null) {
                    listener.onInventoryDataChanged(updatedData);
                }
            }
        });
    }

    public void addItem(Item newItem) {
        if (newItem == null) {
            // Handle the case where newItem is null
            return;
        }

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("itemName", newItem.getItemName());
        itemData.put("purchaseDate", newItem.getPurchaseDate());
        itemData.put("estimatedValue", newItem.getEstimatedValue());
        itemData.put("make", newItem.getMake());
        itemData.put("model", newItem.getModel());
        itemData.put("comments", newItem.getComments());
        itemData.put("serialNumber", newItem.getSerialNumber());
        itemData.put("description", newItem.getDescription());

        itemData.put("tags", newItem.getTags());
        // Add the new item to Firestore
        itemsRef.add(itemData)
                .addOnSuccessListener(documentReference -> {
                    // Retrieve the auto-generated document ID and set it in the Item object
                    String itemId = documentReference.getId();
                    // Add field with autogenerated ID for making deletions easier
                    itemsRef.document(itemId).update("itemId", itemId);
                })
                .addOnFailureListener(e -> {
                    // Handle failure to add the item to Firestore, e.g., log the error or display a message.
                });
    }

    public void deleteItem(String itemId) {
        itemsRef.document(itemId).delete();
    }

    public void deleteMultipleItems(ArrayList<String> itemIds) {
        for (String itemId : itemIds) {
            itemsRef.document(itemId).delete();
        }
    }

    public void setListener(OnInventoryUpdateListener listener) {
        this.listener = listener;
    }

    public interface OnInventoryUpdateListener {
        void onInventoryDataChanged(ArrayList<Item> updatedData);
    }
}
