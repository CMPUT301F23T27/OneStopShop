package com.example.onestopshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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

        Item item = inventory.get(position);

        holder.itemName.setText(item.getItemName());
        // Format the date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        holder.purchaseDate.setText(sdf.format(item.getPurchaseDate()));

        // Format the estimated value to 2 decimal places
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.estimatedValue.setText("$" + decimalFormat.format(item.getEstimatedValue()));
        holder.tags.setText(item.getTags());

    }

    @Override
    public int getItemCount() {
        return inventory.size();
    }



}
