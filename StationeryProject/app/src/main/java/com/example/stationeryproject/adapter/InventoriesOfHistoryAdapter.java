package com.example.stationeryproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stationeryproject.R;
import com.example.stationeryproject.model.Inventory;

import java.util.List;

public class InventoriesOfHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<Inventory> mInventoryList;

    public InventoriesOfHistoryAdapter(Context context,
            List<Inventory> inventoryList) {
        mContext = context;
        mInventoryList = inventoryList;
    }

    @Override
    public int getCount() {
        return mInventoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return mInventoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(mContext).inflate(R.layout.item_inventory_of_history, viewGroup, false);

        Inventory item = mInventoryList.get(i);

        TextView txtCategory = view.findViewById(R.id.txtCategory);
        TextView txtDescription = view.findViewById(R.id.txtDescription);
        TextView txtQuantity = view.findViewById(R.id.txtQuantity);

        txtCategory.setText(item.getCategory());
        txtDescription.setText(item.getDescription());
        txtQuantity.setText(String.valueOf(item.getQuantity()));

        return view;
    }
}
