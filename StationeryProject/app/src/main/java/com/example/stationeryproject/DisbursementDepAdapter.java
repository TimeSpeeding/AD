package com.example.stationeryproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DisbursementDepAdapter extends BaseAdapter {
    private List<DisbursementDepEntity> list;
    private LayoutInflater inflater;

    public DisbursementDepAdapter(Context context, List<DisbursementDepEntity> list){
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

        DisbursementDepEntity disbursement= (DisbursementDepEntity) this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.list_disbursement_dep, null);

            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.text_des);
            viewHolder.tvQtyS = (TextView) convertView.findViewById(R.id.text_qty_s);
            viewHolder.tvQtyR = (TextView) convertView.findViewById(R.id.text_qty_r);
            viewHolder.tvQtyD = (TextView) convertView.findViewById(R.id.text_qty_d);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvDescription.setText(disbursement.getDescription());
        viewHolder.tvDescription.setTextSize(13);
        viewHolder.tvQtyS.setText(disbursement.getScheduledQty()+"");
        viewHolder.tvQtyS.setTextSize(13);
        viewHolder.tvQtyR.setText(disbursement.getActualQty()+"");
        viewHolder.tvQtyR.setTextSize(13);
        viewHolder.tvQtyD.setText(disbursement.getDeliveryQty()+"");
        viewHolder.tvQtyD.setTextSize(13);

        return convertView;
    }

    public static class ViewHolder{
        TextView tvDescription;
        TextView tvQtyS;
        TextView tvQtyR;
        TextView tvQtyD;

    }

}




