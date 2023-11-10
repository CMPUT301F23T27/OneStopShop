package com.example.onestopshop;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Item {


    private String itemId;
    private String itemName;
    private String description;
    private String purchaseDate;
    private String make;
    private String model;
    private double estimatedValue;
    private String comment;
    private String serialNumber;
    private List<String> tags;

    public Item() {

    }

    public Item(String itemId, String itemName, String description, String purchaseDate, String make, String model, double estimatedValue, String comment, String serialNumber, List<String> tags) {
        this.itemId = itemId;
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
    public Item(String itemName, String description, String purchaseDate, String make, String model, double estimatedValue, String comment, String serialNumber, List<String> tags) {

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
    public Item(String itemName, String description, String purchaseDate, String make, String model, double estimatedValue, String serialNumber, List<String> tags) {

        this.itemName = itemName;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.make = make;
        this.model = model;
        this.estimatedValue = estimatedValue;
        this.serialNumber = serialNumber;
        this.tags = tags;
    }
    public Item(String itemId, String itemName, String description, String purchaseDate, String make, String model, double estimatedValue, String serialNumber, List<String> tags) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.make = make;
        this.model = model;
        this.estimatedValue = estimatedValue;
        this.serialNumber = serialNumber;
        this.tags = tags;
    }

    //These are for testing
    public Item(String itemId, String itemName, String purchaseDate, double estimatedValue, List<String> tags) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.purchaseDate = purchaseDate;
        this.estimatedValue = estimatedValue;
        this.tags = tags;
    }
    public Item( String itemName, String purchaseDate, double estimatedValue, List<String> tags) {
        this.itemName = itemName;
        this.purchaseDate = purchaseDate;
        this.estimatedValue = estimatedValue;
        this.tags = tags;
    }

    // Getters and Setters for the Item attributes

    public String getItemId() {
        return itemId;
    }


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

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
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

    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

