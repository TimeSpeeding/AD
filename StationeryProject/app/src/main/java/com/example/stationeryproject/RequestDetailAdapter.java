package com.example.stationeryproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RequestDetailAdapter extends BaseAdapter {
    private List<Request> list;
    private LayoutInflater inflater;

    public RequestDetailAdapter(Context context, List<Request> list){
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

        Request history= (Request)this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.list_requisition_history, null);

            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.text_des);
            viewHolder.tvApplicant = (TextView) convertView.findViewById(R.id.text_applicant);
            viewHolder.tvQty = (TextView) convertView.findViewById(R.id.text_qty);
            viewHolder.tvRequestDate = (TextView) convertView.findViewById(R.id.text_date);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.text_status);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvDescription.setText(history.getDescription());
        viewHolder.tvDescription.setTextSize(13);
        viewHolder.tvApplicant.setText(history.getApplicant());
        viewHolder.tvApplicant.setTextSize(13);
        viewHolder.tvRequestDate.setText(history.getRequestDate());
        viewHolder.tvRequestDate.setTextSize(13);
        viewHolder.tvStatus.setText(history.getStatus());
        viewHolder.tvStatus.setTextSize(13);
        viewHolder.tvQty.setText(history.getQty()+"");
        viewHolder.tvQty.setTextSize(13);


        return convertView;
    }

    public static class ViewHolder{
        TextView tvApplicant;
        TextView tvRequestDate;
        TextView tvDescription;
        TextView tvQty;
        TextView tvStatus;

    }

}




