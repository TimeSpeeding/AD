package com.example.stationeryproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stationeryproject.R;
import com.example.stationeryproject.model.Inventory;


import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    private Context mContext;
    private List<Inventory> mInventoryList;

    public InventoryAdapter(Context context, List<Inventory> inventoryList) {
        mContext = context;
        mInventoryList = inventoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Inventory item = mInventoryList.get(position);

        holder.mTxtCategory.setText(item.getCategory());
        holder.mTxtDescription.setText(item.getDescription());
        holder.mTxtQuantity.setText(String.valueOf(item.getQuantity()));
        holder.mImgEdit.setOnClickListener(view -> {
            editInventory(item);
        });
        holder.mImgDelete.setOnClickListener(view -> {
            deleteInventory(item);
        });
    }

    @Override
    public int getItemCount() {
        return mInventoryList.size();
    }

    private void editInventory(Inventory inventory){
        // display edit dialog
        View subView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_quantity, null);
        final EditText edtQuantity = subView.findViewById(R.id.edtQuantity);
        edtQuantity.setText(String.valueOf(inventory.getQuantity()));

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Edit Quantity")
                .setView(subView)
                .create();

        builder.setPositiveButton("Update", (dialogInterface, i) -> {
            Long quantity = 0L;
            try {
                quantity = Long.parseLong(edtQuantity.getText().toString());
            } catch (NumberFormatException e) {}
            inventory.setQuantity(quantity);

            notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {});
        builder.show();
    }

    private void deleteInventory(Inventory inventory) {
        mInventoryList.remove(inventory);

        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTxtCategory;
        TextView mTxtDescription;
        TextView mTxtQuantity;
        ImageView mImgEdit;
        ImageView mImgDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTxtCategory = itemView.findViewById(R.id.txtCategory);
            mTxtDescription = itemView.findViewById(R.id.txtDescription);
            mTxtQuantity = itemView.findViewById(R.id.txtQuantity);
            mImgEdit = itemView.findViewById(R.id.imgEdit);
            mImgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
