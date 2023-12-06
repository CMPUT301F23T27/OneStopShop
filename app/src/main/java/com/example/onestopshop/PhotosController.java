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

/**
 * Controller class for managing photos related operations, including upload, download, and deletion.
 */
public class PhotosController {
    private StorageReference storageRef;
    private CollectionReference photosRef;
    private OnPhotoListUpdateListener photoListUpdateListener;

    /**
     * Constructor for creating a PhotosController associated with a specific item.
     *
     * @param itemId The ID of the item to which the photos belong.
     */
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

    /**
     * Uploads a single photo to Firebase Storage and stores its download link in Firestore.
     *
     * @param photoUri The URI of the photo to be uploaded.
     */
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
    /**
     * Stores the download link and photo ID in Firestore after a successful photo upload.
     *
     * @param photoId  The unique ID associated with the uploaded photo.
     * @param photoUrl The download URL of the uploaded photo.
     */
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
    /**
     * Uploads multiple photos to Firebase Storage and stores their download links in Firestore.
     *
     * @param photoUris A list of URIs for the photos to be uploaded.
     */
    public void uploadAllPhotos(List<Uri> photoUris) {
        for(Uri uri : photoUris){
            uploadPhoto(uri);
        }
    }

    /**
     * Deletes a photo from Firebase Storage and its reference from Firestore.
     *
     * @param photoId The ID of the photo to be deleted.
     */
    public void deletePhoto(String photoId) {
        // Create references to the photo in Firebase Storage and Firestore
        StorageReference photoStorageRef = storageRef.child(photoId + ".jpg");
        DocumentReference photoDocRef = photosRef.document(photoId);

        photoDocRef.delete()
                .addOnSuccessListener(aVoid -> {
                    // Photo reference deleted from Firestore, now delete the photo from storage
                    photoStorageRef.delete()
                            .addOnSuccessListener(aVoid1 -> {
                                // Photo deleted from storage
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure to delete photo from storage
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle failure to delete photo reference from Firestore
                });
    }

    /**
     * Retrieves the download URL of a photo from Firestore.
     *
     * @param callback Callback to handle success or failure of the download URL retrieval.
     */
    public void getDownloadUrl(DownloadUrlCallback callback) {
        if(photosRef != null) {
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
    }

    /**
     * Interface for handling success or failure of the download URL retrieval.
     */
    public interface DownloadUrlCallback {
        void onSuccess(String downloadUrl);

        void onFailure(Exception e);
    }

    /**
     * @param listener
     */
    public void setOnPhotoListUpdateListener(OnPhotoListUpdateListener listener) {
        photoListUpdateListener = listener;
    }
    /**
     * Interface for handling updates to the photo list.
     */
    public interface OnPhotoListUpdateListener {
        void onPhotoListUpdated(List<ItemPhoto> updatedPhotoList);
    }

}
