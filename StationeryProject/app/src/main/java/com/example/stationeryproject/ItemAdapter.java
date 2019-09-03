package com.example.stationeryproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends BaseAdapter {
    private List<Item> list;
    private LayoutInflater inflater;

    public ItemAdapter(Context context, List<Item> list){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(list!=null){
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item item = (Item) this.getItem(position);

        ItemViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ItemViewHolder();

            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.text_name);
            viewHolder.tvQty = (TextView) convertView.findViewById(R.id.text_qty);
            viewHolder.tvSafetyQty = (TextView) convertView.findViewById(R.id.text_safety_qty);
            viewHolder.tvUnit = (TextView) convertView.findViewById(R.id.text_item_unit);
            viewHolder.tvCategory=convertView.findViewById(R.id.text_category);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ItemViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(item.getItemName());
        viewHolder.tvName.setTextSize(13);
        viewHolder.tvCategory.setText(item.getCategory());
        viewHolder.tvCategory.setTextSize(13);
        viewHolder.tvQty.setText(item.getQty()+"");
        viewHolder.tvQty.setTextSize(13);
        viewHolder.tvSafetyQty.setText(item.getSafetyQty()+"");
        viewHolder.tvSafetyQty.setTextSize(13);
        viewHolder.tvUnit.setText(item.getUnit());
        viewHolder.tvUnit.setTextSize(13);


        return convertView;
    }

    public static class ItemViewHolder{
         TextView tvName;
         TextView tvQty;
         TextView tvSafetyQty;
         TextView tvUnit;
         TextView tvCategory;

    }

}




