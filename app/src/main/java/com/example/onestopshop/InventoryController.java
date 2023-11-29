package com.example.onestopshop;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryController {
    private CollectionReference itemsRef;

    private OnInventoryUpdateListener listener;

    public InventoryController() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        itemsRef = db.collection("users").document(userId).collection("items");

        itemsRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                // Handle errors
                return;
            }
            if (queryDocumentSnapshots != null) {
                ArrayList<Item> updatedData = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String itemId = doc.getString("itemId");
                    String itemName = doc.getString("itemName");
                    String purchaseDate = doc.getString("purchaseDate");
                    double estimatedValue = doc.getDouble("estimatedValue");
                    List<String> tags = (List<String>) doc.get("tags");

                    Item item = new Item(itemId, itemName, purchaseDate, estimatedValue, tags);
                    updatedData.add(item);
                }
                if (listener != null) {
                    listener.onInventoryDataChanged(updatedData);
                }
            }
        });
    }

    public void addItem(Item newItem) {
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("itemName", newItem.getItemName());
        itemData.put("purchaseDate", newItem.getPurchaseDate());
        itemData.put("estimatedValue", newItem.getEstimatedValue());
        itemData.put("tags", newItem.getTags());
        itemData.put("serialNumber", newItem.getSerialNumber());
        itemData.put("make", newItem.getMake());
        itemData.put("model", newItem.getModel());
        itemData.put("comments", newItem.getComments());
        itemData.put("description", newItem.getDescription());

        // Add the new item to Firestore
        itemsRef.add(itemData)
                .addOnSuccessListener(documentReference -> {
                    // Retrieve the auto-generated document ID and set it in the Item object
                    String itemId = documentReference.getId();
                    //Add field with autogenerated ID for making deletions easier
                    itemsRef.document(itemId).update("itemId", itemId);
                })
                .addOnFailureListener(e -> {
                    // Handle failure to add the item to Firestore
                });
    }
    public void updateItem(String itemId, Item newItem) {
        DocumentReference itemRef = itemsRef.document(itemId);
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("itemName", newItem.getItemName());
        itemData.put("purchaseDate", newItem.getPurchaseDate());
        itemData.put("estimatedValue", newItem.getEstimatedValue());
        itemData.put("tags", newItem.getTags());
        itemData.put("serialNumber", newItem.getSerialNumber());
        itemData.put("make", newItem.getMake());
        itemData.put("model", newItem.getModel());
        itemData.put("comments", newItem.getComments());
        itemData.put("description", newItem.getDescription());

        // Add the new item to Firestore
        itemRef.update(itemData)
                .addOnSuccessListener(documentReference -> {

                })
                .addOnFailureListener(e -> {
                    // Handle failure to add the item to Firestore
                });
    }

    public void deleteItem(String itemId) {
        itemsRef.document(itemId).delete();
    }
    public void deleteMultipleItems(ArrayList<String> itemIds) {
        for(String itemId: itemIds) {
            itemsRef.document(itemId).delete();
        }
    }
    public void getItemById(String itemId, final OnItemFetchListener listener) {
        itemsRef.document(itemId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Item item = documentSnapshot.toObject(Item.class);
                        listener.onItemFetched(item);
                    } else {
                        listener.onItemFetchFailed();
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onItemFetchFailed();
                });
    }
    public String getUserEmail(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String email = user.getEmail();

            if (email != null) {
                return email;
            }
        } else {
            // User is not signed in
        }
        return "";
    }
    public String getUserName(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String displayName = user.getDisplayName();
            String email = user.getEmail();

            // Use the display name and email as needed
            if (displayName != null) {
                return displayName;
            }

        } else {
            // User is not signed in

        }
        return "";
    }



    public void setListener(OnInventoryUpdateListener listener) {
        this.listener = listener;
    }

    public interface OnInventoryUpdateListener {
        void onInventoryDataChanged(ArrayList<Item> updatedData);
    }
    public interface OnItemFetchListener {
        void onItemFetched(Item item);
        void onItemFetchFailed();
    }


    public void fetchDataFilteredAndSortedByMake(String makeFilter) {
        Query query = itemsRef;
        Log.d("InventoryController", "makeFilter: " + makeFilter);
        if (makeFilter != null && !makeFilter.isEmpty()) {
            query = query.whereEqualTo("make", makeFilter);
        }
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Item> filteredList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Item item = document.toObject(Item.class);
                    Log.d("InventoryController", "Item: " + item.getMake());
                    filteredList.add(item);

                }
                Log.d("InventoryController", "Size: " + filteredList.size());
                if (listener != null) {
                    listener.onInventoryDataChanged(filteredList);
                }
            } else {
                Log.e("InventoryController", "Error fetching filtered and sorted data: ", task.getException());
            }
        });
    }
    public void fetchDataFilteredAndSortedByDate(String date_A, String date_B) {
        Query query = itemsRef;
        Log.d("InventoryController", "Date Range: " + date_A + " to " + date_B);

        if (date_A != null && !date_A.isEmpty() && date_B != null && !date_B.isEmpty()) {
            // Assuming you have a "date" field in your Firestore documents
            query = query.whereGreaterThanOrEqualTo("purchaseDate", date_A)
                    .whereLessThanOrEqualTo("purchaseDate", date_B);
        }

        // Assuming you want to sort by the "date" field
        query = query.orderBy("purchaseDate");

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Item> filteredList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Item item = document.toObject(Item.class);
                    Log.d("InventoryController", "Item: " + item.getPurchaseDate());
                    filteredList.add(item);
                }
                Log.d("InventoryController", "Size: " + filteredList.size());
                if (listener != null) {
                    listener.onInventoryDataChanged(filteredList);
                }
            } else {
                Log.e("InventoryController", "Error fetching filtered and sorted data: ", task.getException());
            }
        });
    }


}
