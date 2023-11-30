package com.example.onestopshop;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * CustomList is a RecyclerView adapter for displaying a list of items in the inventory.
 */
public class CustomList extends RecyclerView.Adapter<CustomList.ViewHolder> {
    private final Context context;
    private ArrayList<Item> inventory;
    private boolean checkboxVisible;

    /**
     * Constructs a new CustomList.
     *
     * @param context   The context in which the RecyclerView will be displayed.
     * @param inventory The list of items to be displayed.
     */
    public CustomList(Context context, ArrayList<Item> inventory, boolean checkBoxVisible) {
        this.inventory = inventory;
        this.context = context;
        this.checkboxVisible = checkBoxVisible;
    }

    public void setCheckboxVisible(boolean checkboxVisible) {
        this.checkboxVisible = checkboxVisible;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class representing each item view in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, purchaseDate, tags, estimatedValue;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            purchaseDate = itemView.findViewById(R.id.purchaseDate);
            estimatedValue = itemView.findViewById(R.id.estimatedValue);
            tags = itemView.findViewById(R.id.tags);
            checkBox = itemView.findViewById(R.id.itemCheckBox);

            // Set a click listener for the checkbox
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        inventory.get(adapterPosition).setSelected(checkBox.isChecked());
                    }
                }
            });
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View representing an item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item,
                parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder that should be updated to represent the contents
     *                 of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //set text fields for each item in recyclerView
        Item item = inventory.get(position);

        holder.itemName.setText(item.getItemName());

        holder.checkBox.setVisibility(this.checkboxVisible ? View.VISIBLE : View.INVISIBLE);
        holder.purchaseDate.setText(item.getPurchaseDate());
        // Format the estimated value to 2 decimal places
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.estimatedValue.setText("$" + decimalFormat.format(item.getEstimatedValue()));
        holder.tags.setText(item.getTags().toString());
        // Set an OnClickListener for the item at this position
        holder.itemView.setOnClickListener(view -> {
            // Retrieve the item at the clicked position
            Item selectedItem = inventory.get(position);

            // Create an Intent to start ViewActivity
            Intent intent = new Intent(view.getContext(), ViewItemActivity.class);


            intent.putExtra("itemId", selectedItem.getItemId());
            // Include other item details as needed

            // Start the ViewActivity
            view.getContext().startActivity(intent);
        });


    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return inventory.size();
    }


}
