package com.example.onestopshop;

import android.content.Context;
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
    private OnItemClickListener onItemClickListener; // Listener for item clicks
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public CustomList(Context context, ArrayList<Item> inventory, OnItemClickListener onItemClickListener) {
        this.inventory = inventory;
        this.context = context;
        this.onItemClickListener = onItemClickListener; // Initialize the listener
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, purchaseDate, tags, estimatedValue;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            purchaseDate = itemView.findViewById(R.id.purchaseDate);
            estimatedValue = itemView.findViewById(R.id.estimatedValue);
            tags = itemView.findViewById(R.id.tags);

//          Set the click listener for the item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        onItemClickListener.onItemClick(inventory.get(position)); // Pass the clicked item
                    }
                }
            });
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

    }

    @Override
    public int getItemCount() {
        return inventory.size();
    }

}
