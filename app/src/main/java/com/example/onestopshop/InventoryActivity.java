package com.example.onestopshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import android.widget.Button;
import android.widget.CheckBox;

import android.widget.ImageButton;

import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * InventoryActivity represents the main activity for managing and displaying the inventory of items.
 * It provides functionality for adding, sorting, filtering, and deleting items.
 */
public class InventoryActivity extends AppCompatActivity implements InventoryController.OnInventoryUpdateListener {


    private ArrayList<Item> dataList;

    private ArrayList<Item> originalDataList;

    private ArrayList<Item> filteredData;
    private RecyclerView recyclerView;
    private CustomList itemAdapter;
    private boolean isFiltered;
    private ImageButton filterButton;
    CustomList filteredItemsAdapter;
    private InventoryController inventoryController;
    private TextView totalValueTextView;
    private double totalEstimatedValue;
    private Button selectButton;
    private Button deleteMultipleButton;
    private boolean checkboxVisible;
    private LinearLayout totalValueLayout, multipleSelectButtons;
    private ImageButton addButton;
    private ImageButton profileButton;
    private Button addMultipleTags;

    private Spinner sortSpinner;
    private ImageButton switchSortButton; // Separate button for ascending/descending
    private boolean isAscending = true;

    private String startDate, endDate, makeFilter;
    private ArrayList<String> tagsFilter;

    private final ActivityResultLauncher<Intent> filterActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == this.RESULT_OK) {
                    // Retrieve data from the intent returned by ScanActivity
                    Intent filtersIntent = result.getData();
                    boolean clear = filtersIntent.getBooleanExtra("clear", false);
                    if(clear) {
                        recyclerView.setAdapter(itemAdapter);
                        totalEstimatedValue = 0;
                        for(Item item : dataList) {
                            totalEstimatedValue += item.getEstimatedValue();
                        }
                        totalValueTextView.setText("$" + String.format("%.2f", totalEstimatedValue));
                        startDate = "";
                        endDate = "";
                        makeFilter = "";
                        tagsFilter = new ArrayList<>();
                        isFiltered = false;
                        return;
                    }
                    startDate = filtersIntent.getStringExtra("startDate");
                    endDate = filtersIntent.getStringExtra("endDate");
                    makeFilter = filtersIntent.getStringExtra("make");
                    tagsFilter = filtersIntent.getStringArrayListExtra("tags");
                    filteredData = filterData(dataList, startDate, endDate, makeFilter, tagsFilter);
                    filteredItemsAdapter = new CustomList(this, filteredData);
                    recyclerView.setAdapter(filteredItemsAdapter);
                    totalEstimatedValue = 0;
                    for(Item item : filteredData) {
                        totalEstimatedValue += item.getEstimatedValue();
                    }
                    totalValueTextView.setText("$" + String.format("%.2f", totalEstimatedValue));
                    isFiltered = true;
                    filteredItemsAdapter.notifyDataSetChanged();

                    // Handle the received data
                } else {
                    // Handle when the result is not OK
                }
            });






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        isFiltered = false;
        startDate = "";
        endDate = "";
        makeFilter = "";
        tagsFilter = new ArrayList<>();
        inventoryController = new InventoryController(); // Initialize the Inventory Controller
        inventoryController.setListener(this);
        dataList = new ArrayList<>();
        originalDataList = new ArrayList<>();

        filterButton = findViewById(R.id.filter_button);
        addMultipleTags = findViewById(R.id.addTagsMultipleBtn);
        totalValueTextView = findViewById(R.id.total_estimated_value);
        recyclerView = findViewById(R.id.item_list);
        addButton = findViewById(R.id.add_button);
        profileButton = findViewById(R.id.profile_button);
        selectButton = findViewById(R.id.select_button);
        deleteMultipleButton = findViewById(R.id.deleteMultipleBtn);
        totalValueLayout = findViewById(R.id.total_value_layout);
        multipleSelectButtons = findViewById(R.id.multipleSelectBtns);
        checkboxVisible = false;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new CustomList(this, dataList);
        recyclerView.setAdapter(itemAdapter);
        deselectAll();
        sortSpinner = findViewById(R.id.sort_spinner);
        switchSortButton = findViewById(R.id.switch_sort); // Separate button for ascending/descending

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new CustomList(this, dataList);
        recyclerView.setAdapter(itemAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, AddItemActivity.class));
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, UserProfileActivity.class));
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventoryActivity.this, FilterActivity.class);
                //give filter activity previous filters
                intent.putExtra("endDateFilter", endDate);
                intent.putExtra("startDateFilter", startDate);
                intent.putExtra("makeFilter", makeFilter);
                intent.putStringArrayListExtra("tagsFilter", tagsFilter);
                filterActivityLauncher.launch(intent);
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxVisible = !checkboxVisible;
                // Deselect all items
                deselectAll();
                itemAdapter.setCheckboxVisible(checkboxVisible);
                selectButton.setText(checkboxVisible ? "DESELECT" : "SELECT");
                setupMultipleSelect();
                updateCheckboxVisibility(recyclerView);
            }
        });
        addMultipleTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First get an array of selected item ids
                ArrayList<String> selectedItemIds = new ArrayList<>();
                for (Item item : dataList) {
                    if (item.isSelected()) {
                        if (item.getItemId() != null && item.getItemId().isEmpty() == false) {
                            selectedItemIds.add(item.getItemId());
                        }
                    }
                }
                MultipleTagsDialog multipleTagsDialog = new MultipleTagsDialog(InventoryActivity.this, selectedItemIds);
                multipleTagsDialog.show();
            }
        });


        deleteMultipleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First get an array of selected item ids
                ArrayList<String> selectedItemIds = new ArrayList<>();
                for (Item item : dataList) {
                    if (item.isSelected()) {
                        if (item.getItemId() != null && item.getItemId().isEmpty() == false) {
                            selectedItemIds.add(item.getItemId());
                        }
                    }
                }
                // Call Inventory Controller for multiple delete
                inventoryController.deleteMultipleItems(selectedItemIds);
                itemAdapter.notifyDataSetChanged();

            }
        });

        // Create an ArrayAdapter for the sort Spinner
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.sort_criteria,
                android.R.layout.simple_spinner_item
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the sorting based on the selected item
                String selectedSortCriteria = parentView.getItemAtPosition(position).toString();
                if (isFiltered) {
                    sortItemList(selectedSortCriteria, filteredData);
                } else {
                    sortItemList(selectedSortCriteria, dataList);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here for now
            }
        });

        switchSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle between ascending and descending
                isAscending = !isAscending;

                if (isFiltered) {
                    sortItemList(sortSpinner.getSelectedItem().toString(), filteredData);
                } else {
                    sortItemList(sortSpinner.getSelectedItem().toString(), dataList);
                }
                updateSwitchSortButtonAppearance();
            }
        });

    }



    @Override
    public void onInventoryDataChanged(ArrayList<Item> updatedData) {
        dataList.clear();
        dataList.addAll(updatedData);
        originalDataList.clear();
        originalDataList.addAll(updatedData);
        totalValueTextView.setText("$" + String.format("%.2f", calculateTotalEstimatedValue()));
        itemAdapter.notifyDataSetChanged();
    }

    /**
     * Filters the data based on the provided filters.
     *
     * @param updatedData List of items to be filtered.
     * @param startDate   Start date filter.
     * @param endDate     End date filter.
     * @param makeFilter  Make filter.
     * @param tagsFilter  Tags filter.
     * @return Filtered list of items.
     */
    public ArrayList<Item> filterData(ArrayList<Item> updatedData, String startDate, String endDate, String makeFilter, ArrayList<String> tagsFilter) {
        ArrayList<Item> filteredList = new ArrayList<>();
        boolean passFilter;
        Log.d("Debug", makeFilter);
        for (Item item : updatedData) {
            Log.d("Debug", item.getItemName());

            passFilter = true;

            // Check startDate filter
            if (!TextUtils.isEmpty(startDate)) {
                Log.d("Inside", "dB");
                passFilter = passFilter && item.getPurchaseDate().compareTo(startDate) >= 0;
            }

            // Check endDate filter
            if (!TextUtils.isEmpty(endDate)) {
                Log.d("Inside", "dA");
                passFilter = passFilter && item.getPurchaseDate().compareTo(endDate) <= 0;
            }

            // Check makeFilter
            if (!TextUtils.isEmpty(makeFilter)) {
                Log.d("Inside", "make");
                passFilter = passFilter && item.getMake().equals(makeFilter);

            }

            // Check tagsFilter
            if (tagsFilter != null && tagsFilter.size()>0) {
                Log.d("Inside", "tags:" + tagsFilter.size());
                passFilter = passFilter && itemContainsAnyTags(item, tagsFilter);
            }

            // If all conditions are met, add the item to the filtered list
            if (passFilter) {
                filteredList.add(item);
            }
        }
        Log.d("Debug", "" + filteredList.size());
        return filteredList;
    }
    /**
     * Checks if an item contains any of the specified tags.
     *
     * @param item       The item to check.
     * @param tagsFilter List of tags to check against.
     * @return True if the item contains any of the specified tags, false otherwise.
     */
    private boolean itemContainsAnyTags(Item item, ArrayList<String> tagsFilter) {
        for (String tag : tagsFilter) {
            if (item.getTags().contains(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the visibility of checkboxes based on value of global boolean checkboxVisible
     *
     * @param recycler recyclerview containing checkbox
     */
    private void updateCheckboxVisibility(RecyclerView recycler) {
        for (int i = 0; i < recycler.getChildCount(); i++) {
            View itemView = recycler.getChildAt(i);
            CheckBox checkBox = itemView.findViewById(R.id.itemCheckBox);

            if (checkBox != null) {
                if (checkboxVisible) {
                    checkBox.setVisibility(View.VISIBLE);
                } else {
                    checkBox.setVisibility(View.INVISIBLE);
                }
                // In both situations we want all items to default as unselected
            }
        }
        // Notify the adapter
        int itemCount = recycler.getAdapter().getItemCount();
        recycler.getAdapter().notifyItemRangeChanged(0,itemCount);
    }


    /**
     * Enables/Disables the layout for select mode
     */
    private void setupMultipleSelect() {
        if (checkboxVisible) {
            totalValueLayout.setVisibility(View.INVISIBLE);
            multipleSelectButtons.setVisibility(View.VISIBLE);

        } else {
            totalValueLayout.setVisibility(View.VISIBLE);
            multipleSelectButtons.setVisibility(View.GONE);
        }
    }

    /**
     * Sets isSelected of each item in the list to false
     */
    private void deselectAll() {
        for (Item item : dataList) {
            item.setSelected(false);
        }
    }

    public double calculateTotalEstimatedValue() {
        double totalEstimatedValue = 0;
        for (Item item : dataList) {
            totalEstimatedValue += item.getEstimatedValue();
        }
        return totalEstimatedValue;
    }

    /**
     * Sorts the item list based on the selected sorting criteria.
     *
     * @param selectedSortCriteria The selected sorting criteria.
     * @param unsortedList         The unsorted list of items.
     */
    private void sortItemList(String selectedSortCriteria, ArrayList<Item> unsortedList) {
        // Extract sorting details
        String sortField = selectedSortCriteria.split(" ")[0];
        if ("None".equals(sortField)) {
            sortItemsByNone();
            return;
        }

        // Use the appropriate comparator based on the selected criteria
        switch (sortField) {
            case "Date":
                sortItemsByDate(unsortedList);
                break;
            case "Description":
                sortItemsByDescription(unsortedList);
                break;
            case "Make":
                sortItemsByMake(unsortedList);
                break;
            case "Estimated":
                sortItemsByEstimatedValue(unsortedList);
                break;
            case "Tag":
                sortItemsByTag(unsortedList);
                break;

        }

        // Notify the adapter of the data change
        if(isFiltered) {
            filteredItemsAdapter.notifyDataSetChanged();
        }
        else {
            itemAdapter.notifyDataSetChanged();
        }


    }

    /**
     * Updates the appearance of the switchSortButton based on the sorting order.
     */
    private void updateSwitchSortButtonAppearance() {
        if (isAscending) {
            switchSortButton.setImageResource(R.drawable.rsz_arrow_copy);
        } else {
            switchSortButton.setImageResource(R.drawable.rsz_arrow);
        }
    }

    /**
     * Sorts the list of items by their purchase date.
     *
     * @param unsortedList The unsorted list of items.
     */
    private void sortItemsByDate(ArrayList<Item> unsortedList) {
        Collections.sort(unsortedList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return item1.getPurchaseDate().compareTo(item2.getPurchaseDate());
            }
        });
        if (!isAscending) {
            Collections.reverse(unsortedList);
        }
    }


    /**
     * Sorts the list of items by "None" (original order).
     */
    private void sortItemsByNone() {
        dataList.clear();
        dataList.addAll(originalDataList);

        // Notify the adapter of the data change
        if (isFiltered) {
            filteredItemsAdapter.notifyDataSetChanged();
        } else {
            itemAdapter.notifyDataSetChanged();
        }
    }



    /**
     * Sorts the list of items by their tags.
     *
     * @param unsortedList The unsorted list of items.
     */
    public void sortItemsByTag(List<Item> unsortedList) {
        Collections.sort(unsortedList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                // Get the first tag of each item for comparison
                String tag1 = item1.getTags().isEmpty() ? "" : item1.getTags().get(0);
                String tag2 = item2.getTags().isEmpty() ? "" : item2.getTags().get(0);

                int result = tag1.compareTo(tag2);

                // Return the comparison result
                return result;
            }

        });
        if (!isAscending) {
            Collections.reverse(unsortedList);
        }
    }



    /**
     * Sorts the list of items by their description.
     *
     * @param unsortedList The unsorted list of items.
     */
    private void sortItemsByDescription(ArrayList<Item> unsortedList) {
        Collections.sort(unsortedList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                String description1 = item1.getDescription();
                String description2 = item2.getDescription();

                // Handle null descriptions
                if (description1 == null && description2 == null) {
                    return 0;
                } else if (description1 == null) {
                    return -1;
                } else if (description2 == null) {
                    return 1;
                }

                int result = description1.compareTo(description2);

                // Adjust result based on the ascending order
                return isAscending ? result : -result;
            }
        });

        // Reverse the list
        Collections.reverse(unsortedList);
    }




    /**
     * Sorts the list of items by their make.
     *
     * @param unsortedList The unsorted list of items.
     */
    private void sortItemsByMake(ArrayList<Item> unsortedList) {
        Collections.sort(unsortedList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                // Check if the make contains a number

                String make1 = item1.getMake();
                String make2 = item2.getMake();

                if (make1 == null && make2 == null) {
                    return 0;
                } else if (make1 == null) {
                    return -1;
                } else if (make2 == null) {
                    return 1;
                }

                int result = make1.compareTo(make2);

                // Adjust result based on the ascending order
                return isAscending ? result : -result;
            }
        });

        // Reverse the list
        Collections.reverse(unsortedList);
    }

    /**
     * Sorts the list of items by their estimated value.
     *
     * @param unsortedList The unsorted list of items.
     */
    private void sortItemsByEstimatedValue(ArrayList<Item> unsortedList) {
        Collections.sort(unsortedList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return Double.compare(item1.getEstimatedValue(), item2.getEstimatedValue());
            }
        });
        if (!isAscending) {
            Collections.reverse(unsortedList);
        }
    }

    /**
     * Navigates to the SearchActivity to perform a search operation.
     *
     * @param view The view that triggered the search operation.
     */
    public void search(View view) {

        Intent intent = new Intent(InventoryActivity.this, SearchActivity.class);

        startActivity(intent);

    }

}