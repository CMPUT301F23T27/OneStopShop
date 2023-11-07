package com.example.onestopshop;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {

    private String id;
    private String itemName;
    private String description;
    private String purchaseDate;
    private String make;
    private String model;
    private double estimatedValue;
    private String comment;
    private String serialNumber;
    private ArrayList<String> tags;


    public Item(String id, String itemName, String description, String purchaseDate, String make, String model, double estimatedValue, String comment, String serialNumber, ArrayList<String> tags) {
        this.id = id; // Set the id
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
    public Item(String id, String itemName, String description, String purchaseDate, String make, String model, double estimatedValue, String serialNumber, ArrayList<String> tags) {
        this.id = id; // Set the id
        this.itemName = itemName;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.make = make;
        this.model = model;
        this.estimatedValue = estimatedValue;
        this.serialNumber = serialNumber;
        this.tags = tags;
    }
    public Item(String name, String description, String id, String itemName, String purchaseDate, double estimatedValue, String comment, String serialNumber, ArrayList<String> tags) {
        this.id = id; // Set the id
        this.itemName = itemName;
        this.purchaseDate = purchaseDate;
        this.estimatedValue = estimatedValue;
        this.tags = tags;
    }

    // Getters and Setters for the Item attributes
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public ArrayList<String> getTags() {
        return tags;
    }
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}

