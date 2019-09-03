package com.example.stationeryproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class EmpByDepAdapter extends BaseAdapter {
    private List<String> list;
    private LayoutInflater inflater;
    public HashMap<String,Boolean> states=new HashMap<String,Boolean>();

    public EmpByDepAdapter(Context context, List<String> list){
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

        String name = (String) this.getItem(position);

        ItemViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ItemViewHolder();

            convertView = inflater.inflate(R.layout.list_emp_by_dep, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_show_emp_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ItemViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(name);
        viewHolder.tvName.setTextSize(13);

        final RadioButton radio=(RadioButton) convertView.findViewById(R.id.rb_name);
        viewHolder.rdBtn=radio;

        //set listener when radio button clicked and ensure others can not be clicked
        viewHolder.rdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(String key:states.keySet()){
                    states.put(key, false);
                }
                states.put(String.valueOf(position), radio.isChecked());
                EmpByDepAdapter.this.notifyDataSetChanged();
            }
        });

        boolean res=false;
        if(states.get(String.valueOf(position)) == null || states.get(String.valueOf(position))== false){
            res=false;
            states.put(String.valueOf(position), false);
        }
        else
            res = true;

        viewHolder.rdBtn.setChecked(res);

        return convertView;
    }

    public static class ItemViewHolder{
         TextView tvName;
        RadioButton rdBtn;
    }

}




