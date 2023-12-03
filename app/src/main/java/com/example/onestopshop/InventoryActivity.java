package com.example.onestopshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.CheckBox;

import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InventoryActivity extends AppCompatActivity implements InventoryController.OnInventoryUpdateListener {

    private ArrayList<Item> dataList;
    private RecyclerView recyclerView;
    private CustomList itemAdapter;
    private InventoryController inventoryController;
    private TextView totalValueTextView;
    private double totalEstimatedValue;
    private Button selectButton;
    private Button deleteMultipleButton;
    private boolean checkboxVisible;
    private LinearLayout totalValueLayout, multipleSelectButtons;
    private ImageButton addButton;
    private ImageButton profileButton;
    private Spinner sortSpinner;
    private ImageButton switchSortButton; // Separate button for ascending/descending
    private boolean isAscending = true;

    private ImageView sortButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);



        inventoryController = new InventoryController();
        inventoryController.setListener(this);

        dataList = new ArrayList<>();
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
        sortButton = findViewById(R.id.sort_button);
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

        deleteMultipleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First get an array of selected item ids
                ArrayList<String> selectedItemIds = new ArrayList<>();
                for (Item item: dataList) {
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
                sortItemList(selectedSortCriteria);
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
                sortItemList(sortSpinner.getSelectedItem().toString());
                updateSwitchSortButtonAppearance();
            }
        });



    }

    @Override
    public void onInventoryDataChanged(ArrayList<Item> updatedData) {
        dataList.clear();
        dataList.addAll(updatedData);
        totalValueTextView.setText("$" + String.format("%.2f", calculateTotalEstimatedValue()));
        itemAdapter.notifyDataSetChanged();
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
    private double calculateTotalEstimatedValue() {
        double totalEstimatedValue = 0;
        for (Item item : dataList) {
            totalEstimatedValue += item.getEstimatedValue();
        }
        return totalEstimatedValue;
    }

    private void sortItemList(String selectedSortCriteria) {
        // Extract sorting details
        String sortField = selectedSortCriteria.split(" ")[0];

        // Use the appropriate comparator based on the selected criteria
        switch (sortField) {
            case "Date":
                sortItemsByDate();
                break;
            case "Description":
                sortItemsByDescription();
                break;
            case "Make":
                sortItemsByMake();
                break;
            case "Estimated":
                sortItemsByEstimatedValue();
                break;
            // Handle additional sorting criteria if needed
        }

        // Notify the adapter of the data change
        itemAdapter.notifyDataSetChanged();

    }

    private void updateSwitchSortButtonAppearance() {
        if (isAscending) {
            switchSortButton.setImageResource(R.drawable.rsz_arrow_copy);
        } else {
            switchSortButton.setImageResource(R.drawable.rsz_arrow);
        }
    }

    private void sortItemsByDate() {
        Collections.sort(dataList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return item1.getPurchaseDate().compareTo(item2.getPurchaseDate());
            }
        });
        if (!isAscending) {
            Collections.reverse(dataList);
        }
    }

    private void sortItemsByDescription() {
        Collections.sort(dataList, new Comparator<Item>() {
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
        Collections.reverse(dataList);
    }





    private void sortItemsByMake() {
        Collections.sort(dataList, new Comparator<Item>() {
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
        Collections.reverse(dataList);
    }





    private void sortItemsByEstimatedValue() {
        Collections.sort(dataList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return Double.compare(item1.getEstimatedValue(), item2.getEstimatedValue());
            }
        });
        if (!isAscending) {
            Collections.reverse(dataList);
        }
    }


}
