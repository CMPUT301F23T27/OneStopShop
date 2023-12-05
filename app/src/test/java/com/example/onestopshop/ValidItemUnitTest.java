package com.example.onestopshop;

import static org.junit.Assert.assertEquals;

import android.text.TextUtils;
import android.widget.Toast;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class ValidItemUnitTest {
    // test the valid item function from AddItemFragment
    @Test
    public void testValidItem() {
        ArrayList<Item> inventory = new ArrayList<>();
        //add items to test filter
        addTestData(inventory);
        //first six items are invalid, last one should be valid
        for(int i = 0; i < inventory.size() - 1; i++) {
            assertEquals(false, validItem(inventory.get(i)));
        }
        assertEquals(true, validItem(inventory.get(5)));

    }
    public void addTestData(ArrayList<Item> inventory) {
        inventory.add(new Item(
                "",
                "2021-04-01",
                "apple",
                200.0,
                new ArrayList<>(Arrays.asList("device"))
        ));
        inventory.add(new Item(
                "Headphones",
                "",
                "Sony",
                200.0,
                new ArrayList<>(Arrays.asList("device"))
        ));
        inventory.add(new Item(
                "Iphone X",
                "2021-04-01",
                "",
                200.0,
                new ArrayList<>(Arrays.asList("device"))
        ));
        inventory.add(new Item(
                "Iphone 8",
                "2020-04-01",
                "",
                200.0,
                new ArrayList<>(Arrays.asList("device"))
        ));
        inventory.add(new Item(
                "Iphone 8",
                "2020-04-01",
                "apple",
                -1,
                new ArrayList<>(Arrays.asList("device"))
        ));
        inventory.add(new Item(
                "Iphone 8",
                "2020-04-01",
                "apple",
                200.0,
                new ArrayList<>(Arrays.asList("device"))
        ));
    }
    public boolean validItem(Item item) {
        boolean valid = true;
        if(item.getItemName() == null || item.getItemName().isEmpty()) {
            valid = false;
        }
        else if(item.getPurchaseDate() == null || item.getPurchaseDate().isEmpty()){
            valid = false;
        }
        else if(item.getMake() == null || item.getMake().isEmpty()) {
            valid = false;
        }
        else if(item.getEstimatedValue() < 0) {
            valid = false;
        }
        else if(item.getTags().size() == 0) {
            valid = false;
        }

        return valid;
    }
}
