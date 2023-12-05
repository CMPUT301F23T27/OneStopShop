package com.example.onestopshop;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is a class that represents an item and its details
 */

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
    private boolean isSelected;

    public Item() {

    }

    /**
     * Create and Item object with ID and comment
     * @param itemId
     *      ID of the Item
     * @param itemName
     *      Name of the item
     * @param description
     *      Brief Description of the item
     * @param purchaseDate
     *      date the item was purchased/acquired
     * @param make
     *      make of the item
     * @param model
     *      model of the item
     * @param estimatedValue
     *      estimated worth of the item
     * @param comment
     *      comment regarding the item
     * @param serialNumber
     *      serial number for the item
     * @param tags
     *      set of tags applied to the item
     */
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
        this.isSelected = false;
    }

    /**
     * Create and Item object without ID and with comment
     * @param itemName
     *      Name of the item
     * @param description
     *      Brief Description of the item
     * @param purchaseDate
     *      date the item was purchased/acquired
     * @param make
     *      make of the item
     * @param model
     *      model of the item
     * @param estimatedValue
     *      estimated worth of the item
     * @param comment
     *      comment regarding the item
     * @param serialNumber
     *      serial number for the item
     * @param tags
     *      set of tags applied to the item
     */
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
        this.isSelected = false;
    }

    /**
     * Create and Item object without ID and comment
     * @param itemName
     *      Name of the item
     * @param description
     *      Brief Description of the item
     * @param purchaseDate
     *      date the item was purchased/acquired
     * @param make
     *      make of the item
     * @param model
     *      model of the item
     * @param estimatedValue
     *      estimated worth of the item
     * @param serialNumber
     *      serial number for the item
     * @param tags
     *      set of tags applied to the item
     */
    public Item(String itemName, String description, String purchaseDate, String make, String model, double estimatedValue, String serialNumber, List<String> tags) {
        this.itemName = itemName;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.make = make;
        this.model = model;
        this.estimatedValue = estimatedValue;
        this.serialNumber = serialNumber;
        this.tags = tags;
        this.isSelected = false;
    }

    /**
     * Create and Item object with ID and without comment
     * @param itemId
     *      ID of the Item
     * @param itemName
     *      Name of the item
     * @param description
     *      Brief Description of the item
     * @param purchaseDate
     *      date the item was purchased/acquired
     * @param make
     *      make of the item
     * @param model
     *      model of the item
     * @param estimatedValue
     *      estimated worth of the item
     * @param serialNumber
     *      serial number for the item
     * @param tags
     *      set of tags applied to the item
     */
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
        this.isSelected = false;
    }

    //These are for testing
    public Item(String itemId, String itemName, String purchaseDate, String make, String model, double estimatedValue, List<String> tags) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.make = make;
        this.model = model;
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
    public Item( String itemName, String purchaseDate, String make, double estimatedValue, List<String> tags) {
        this.itemName = itemName;
        this.purchaseDate = purchaseDate;
        this.estimatedValue = estimatedValue;
        this.make = make;
        this.tags = tags;
    }

    // Getters and Setters for the Item attributes

    /**
     * This returns the ID of the item
     * @return
     *      Return item ID
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * This returns the name of the item
     * @return
     *      Return the item name
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * This sets the name of the item
     * @param itemName
     *      New item name
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * This returns the description for the item
     * @return
     *      Return item description
     */
    public String getDescription() {
        return description;
    }

    /**
     * This sets the description of the item
     * @param description
     *      New item description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This returns the purchaseDate of the item
     * @return
     *      Return item purchase date
     */
    public String getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * This sets the purchaseDate of the item
     * @param purchaseDate
     *      New purchaseDate
     */
    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * This returns the make of the item
     * @return
     *      Return item make
     */
    public String getMake() {
        return make;
    }

    /**
     * This sets the make of the item
     * @param make
     *      New make of the item
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * This returns the model of the item
     * @return
     *      Return item model
     */
    public String getModel() {
        return model;
    }

    /**
     * This sets the model of the item
     * @param model
     *      New item model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * This returns the estimatedValue of the item
     * @return
     *      Return estimatedValue
     */
    public double getEstimatedValue() {
        return estimatedValue;
    }

    /**
     * This sets the estimatedValue of the item
     * @param estimatedValue
     *      New estimatedValue
     */
    public void setEstimatedValue(double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    /**
     * This returns the comment of the item
     * @return
     *      Return comment
     */
    public String getComments() {
        return comment;
    }

    /**
     * This sets the comment of the item
     * @param comments
     *      New comment
     */
    public void setComments(String comments) {
        this.comment = comments;
    }

    /**
     * This returns the serialNumber of the item
     * @return
     *      Return serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * This sets the serialNumber of the item
     * @param serialNumber
     *      Return serialNumber
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * This returns an array of the items tags
     * @return
     *      Return tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * This sets the tags of the item
     * @param tags
     *      New array of tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * This returns the selection state of the item
     * @return
     *      Return isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * This sets the selected state of the item
     * @param selected
     *      New selection state
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

