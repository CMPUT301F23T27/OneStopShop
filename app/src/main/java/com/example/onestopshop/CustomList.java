package com.example.onestopshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class CustomList extends RecyclerView.Adapter<CustomList.ViewHolder> {
    private final Context context;
    private ArrayList<Item> inventory;


    public CustomList(Context context, ArrayList<Item> inventory) {
        this.inventory = inventory;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, purchaseDate, tags, estimatedValue;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            purchaseDate = itemView.findViewById(R.id.purchaseDate);
            estimatedValue = itemView.findViewById(R.id.estimatedValue);
            tags = itemView.findViewById(R.id.tags);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //set text fields for each item in recyclerView
        Item item = inventory.get(position);

        holder.itemName.setText(item.getItemName());


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

    @Override
    public int getItemCount() {
        return inventory.size();
    }



}
