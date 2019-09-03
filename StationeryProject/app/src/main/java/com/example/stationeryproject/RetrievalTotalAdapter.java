package com.example.stationeryproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RetrievalTotalAdapter extends BaseAdapter {
    private List<StationeryNeeded> list;
    private LayoutInflater inflater;

    public RetrievalTotalAdapter(Context context, List<StationeryNeeded> list){
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

        StationeryNeeded stationeryNeeded = (StationeryNeeded) this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.list_retrieval_total, null);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.text_des);
            viewHolder.tvScheduleQty = (TextView) convertView.findViewById(R.id.text_qty_s);
            viewHolder.tvActualQty = (TextView) convertView.findViewById(R.id.text_qty_a);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvDescription.setText(stationeryNeeded.getDescription());
        viewHolder.tvDescription.setTextSize(13);
        viewHolder.tvScheduleQty.setText(stationeryNeeded.getScheduledQty()+"");
        viewHolder.tvScheduleQty.setTextSize(13);
        viewHolder.tvActualQty.setText(stationeryNeeded.getActualQty()+"");
        viewHolder.tvActualQty.setTextSize(13);


        return convertView;
    }

    public static class ViewHolder{
        public TextView tvDescription;
        public TextView tvScheduleQty;
        public TextView tvActualQty;

    }

}




