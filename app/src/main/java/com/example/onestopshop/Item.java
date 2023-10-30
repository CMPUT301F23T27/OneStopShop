package com.example.onestopshop;



import java.util.Date;

public class Item {
    private String itemName;
    private String description;
    private Date purchaseDate;
    private String make;
    private String model;
    private double estimatedValue;
    private String comment;
    private String serialNumber;
    private String tags;

    public Item(String itemName, String description, Date purchaseDate, String make, String model, double estimatedValue, String comment, String serialNumber, String tags) {
        this.itemName = itemName;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.make = make;
        this.model = model;
        this.estimatedValue = estimatedValue;
        this.comment = comment;
        this.serialNumber = serialNumber;
        this.tags = tags;
    }
    public Item(String itemName, Date purchaseDate, double estimatedValue, String tags) {
        this.itemName = itemName;
        this.purchaseDate = purchaseDate;
        this.estimatedValue = estimatedValue;
        this.tags = tags;
    }

    // Getters and Setters for the Item attributes

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getComments() {
        return comment;
    }

    public void setComments(String comments) {
        this.comment = comments;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
}

