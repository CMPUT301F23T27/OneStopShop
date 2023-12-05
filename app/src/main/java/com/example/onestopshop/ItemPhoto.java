package com.example.onestopshop;

/**
 * Represents a photo associated with an item, containing a photo URL and an optional photo ID.
 */
public class ItemPhoto {
    private String photoUrl;
    private String photoId;

    /**
     * Constructs an ItemPhoto with both a photo ID and a photo URL.
     *
     * @param photoId   The ID associated with the photo.
     * @param photoUrl  The URL of the photo.
     */
    ItemPhoto(String photoId, String photoUrl) {
        this.photoId = photoId;
        this.photoUrl = photoUrl;
    }

    /**
     * Constructs an ItemPhoto with only a photo URL.
     *
     * @param photoUrl  The URL of the photo.
     */
    ItemPhoto(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }
}
