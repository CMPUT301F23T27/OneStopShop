package com.example.onestopshop;

public class ItemPhoto {
    private String photoUrl;
    private String photoId;
    ItemPhoto(String photoId, String photoUrl) {
        this.photoId = photoId;
        this.photoUrl = photoUrl;
    }
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
