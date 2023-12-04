package com.example.onestopshop;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class PhotosController {
    private StorageReference storageRef;
    private CollectionReference photosRef;
    private OnPhotoListUpdateListener photoListUpdateListener;

    public PhotosController(String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            // dont create reference
            return;
        }
        storageRef = FirebaseStorage.getInstance().getReference().child("photos").child(itemId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        photosRef = db.collection("users").document(userId).collection("items").document(itemId).collection("photos");
        photosRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                // Handle errors
                return;
            }
            if (queryDocumentSnapshots != null) {
                ArrayList<ItemPhoto> updatedData = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String photoUrl = doc.getString("photoUrl");
                    String photoId = doc.getString("photoId");
                    ItemPhoto itemPhoto = new ItemPhoto(photoId, photoUrl);
                    updatedData.add(itemPhoto);
                }
                if (photoListUpdateListener != null) {
                    photoListUpdateListener.onPhotoListUpdated(updatedData);
                }
            }
        });
    }

    public void uploadPhoto(Uri photoUri) {
        // Get a reference to the location where you want to store the photo in Firebase Storage
        String photoId = UUID.randomUUID().toString();


        // Upload the photo to Firebase Storage
        storageRef.child(photoId).putFile(photoUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Photo upload successful, get the download URL
                            Task<Uri> downloadUrlTask = task.getResult().getStorage().getDownloadUrl();
                            downloadUrlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(Task<Uri> uriTask) {
                                    if (uriTask.isSuccessful()) {
                                        // Download URL obtained, store it in Firestore
                                        String photoUrl = uriTask.getResult().toString();
                                        storeDownloadLink(photoId,photoUrl);
                                    }
                                }
                            });
                        }
                    }
                });
    }
    private void storeDownloadLink(String photoId, String photoUrl) {
        // Create a new document in the "images" collection and store the download link
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("photoId", photoId);
        itemData.put("photoUrl", photoUrl);
        photosRef.document(photoId).set(itemData)
                .addOnSuccessListener(aVoid -> {
                    // Successfully stored download link in Firestore
                })
                .addOnFailureListener(e -> {
                    // Handle failure to store download link
                });
    }
    public void uploadAllPhotos(List<Uri> photoUris) {
        for(Uri uri : photoUris){
            uploadPhoto(uri);
        }
    }
    public void deletePhoto(String photoId) {
        // Create references to the photo in Firebase Storage and Firestore
        StorageReference photoStorageRef = storageRef.child(photoId + ".jpg");
        DocumentReference photoDocRef = photosRef.document(photoId);

        // Delete the photo from Firebase Storage
        photoStorageRef.delete()
                .addOnSuccessListener(aVoid -> {
                    // Photo deleted from storage, now delete its reference from Firestore
                    photoDocRef.delete()
                            .addOnSuccessListener(aVoid1 -> {
                                // Photo reference deleted from Firestore

                            })
                            .addOnFailureListener(e -> {
                                // Handle failure to delete photo reference from Firestore
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle failure to delete photo from storage
                });
    }

    public void getDownloadUrl(DownloadUrlCallback callback) {
        photosRef.limit(1).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String downloadUrl = queryDocumentSnapshots.getDocuments().get(0).getString("photoUrl");
                        callback.onSuccess(downloadUrl);
                    } else {

                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e));
    }

    public interface DownloadUrlCallback {
        void onSuccess(String downloadUrl);

        void onFailure(Exception e);
    }
    public void setOnPhotoListUpdateListener(OnPhotoListUpdateListener listener) {
        photoListUpdateListener = listener;
    }
    public interface OnPhotoListUpdateListener {
        void onPhotoListUpdated(List<ItemPhoto> updatedPhotoList);
    }

}
