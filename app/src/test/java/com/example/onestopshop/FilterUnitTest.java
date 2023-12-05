package com.example.onestopshop;



import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;


public class FilterUnitTest {
    //tests the filterData function from InventoryActivity
    // Note: had to move it here as it was part of InventoryActivity
    @Test
    public void testFilterData(){
        ArrayList<Item> inventory = new ArrayList<>();
        ArrayList<Item> filteredList = new ArrayList<>();
        //add items to test filter
        addTestData(inventory);
        filteredList = filterData(inventory, "2021-01-01", "2022-01-01", "apple", new ArrayList<>(Arrays.asList("device")));
        // there should only be 3 items in list and make should be apple
        assertEquals(3, filteredList.size());
        assertEquals("apple", filteredList.get(0).getMake());
    }
    public void addTestData(ArrayList<Item> inventory) {
        inventory.add(new Item(
                "Headphones",
                "2021-04-01",
                "apple",
                200.0,
                new ArrayList<>(Arrays.asList("device"))
        ));
        inventory.add(new Item(
                "Headphones",
                "2021-04-01",
                "Sony",
                200.0,
                new ArrayList<>(Arrays.asList("device"))
        ));
        inventory.add(new Item(
                "Iphone X",
                "2021-04-01",
                "apple",
                200.0,
                new ArrayList<>(Arrays.asList("device"))
        ));
        inventory.add(new Item(
                "Iphone 8",
                "2020-04-01",
                "apple",
                200.0,
                new ArrayList<>(Arrays.asList("device"))
        ));
        inventory.add(new Item(
                "Mac",
                "2021-10-01",
                "apple",
                200.0,
                new ArrayList<>(Arrays.asList("device"))
        ));
        inventory.add(new Item(
                "Mac",
                "2021-10-01",
                "notApple",
                200.0,
                new ArrayList<>(Arrays.asList("device"))
        ));
        inventory.add(new Item(
                "Mac",
                "2021-10-01",
                "apple",
                200.0,
                new ArrayList<>(Arrays.asList("new"))
        ));
    }

    // filterData from InventoryActivity that we will test
    public ArrayList<Item> filterData(ArrayList<Item> updatedData, String startDate, String endDate, String makeFilter, ArrayList<String> tagsFilter) {
        ArrayList<Item> filteredList = new ArrayList<>();
        boolean passFilter;

        for (Item item : updatedData) {


            passFilter = true;

            // Check startDate filter
            if (startDate != null && !startDate.isEmpty()) {

                passFilter = passFilter && item.getPurchaseDate().compareTo(startDate) >= 0;
            }

            // Check endDate filter
            if (endDate != null && !endDate.isEmpty()) {

                passFilter = passFilter && item.getPurchaseDate().compareTo(endDate) <= 0;
            }

            // Check makeFilter
            if (makeFilter != null && !makeFilter.isEmpty()) {

                passFilter = passFilter && item.getMake().equals(makeFilter);

            }

            // Check tagsFilter
            if (tagsFilter != null && tagsFilter.size()>0) {

                passFilter = passFilter && itemContainsAnyTags(item, tagsFilter);
            }

            // If all conditions are met, add the item to the filtered list
            if (passFilter) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }
    // Also from InventoryActivity
    public boolean itemContainsAnyTags(Item item, ArrayList<String> tagsFilter) {
        for (String tag : tagsFilter) {
            if (item.getTags().contains(tag)) {
                return true;
            }
        }
        return false;
    }
}
