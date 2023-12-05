package com.example.onestopshop;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUnitTest {
    @Test
    public void testCreateItem(){
        String itemId = "1234567";
        String itemName = "Phone";
        String description = "this is a phone";
        String purchaseDate = "2023-03-01";
        String make = "apple";
        String model = "14";
        Double estimatedValue = 200.0;
        String serialNumber = "12345";
        String comments = "this is a comment";
        ArrayList<String> tags = new ArrayList<>(Arrays.asList("test", "device"));
        Item newItem = new Item(itemId, itemName, description, purchaseDate, make,
                model, estimatedValue, comments, serialNumber, tags);
        assertEquals("1234567", newItem.getItemId());
        assertEquals("Phone", newItem.getItemName());
        assertEquals("this is a phone", newItem.getDescription());
        assertEquals("2023-03-01", newItem.getPurchaseDate());
        assertEquals("apple", newItem.getMake());
        assertEquals("14", newItem.getModel());
        assertEquals(200.0, newItem.getEstimatedValue(), 0.1);
        assertEquals("12345", newItem.getSerialNumber());
        assertEquals("this is a comment", newItem.getComments());
        assertEquals("apple", newItem.getMake());
        List<String> expectedTags = Arrays.asList("test", "device");
        List<String> actualTags = newItem.getTags();
        assertEquals(expectedTags, actualTags);
    }

}
