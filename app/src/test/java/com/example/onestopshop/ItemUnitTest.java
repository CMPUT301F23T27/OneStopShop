package com.example.onestopshop;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemUnitTest {
    @Test
    public void testMakeItem(){
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
                model, estimatedValue, serialNumber, comments , tags);
        assertEquals("1234567", newItem.getItemId());
    }

}
