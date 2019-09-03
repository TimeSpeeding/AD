package com.example.stationeryproject;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetrievalAdapter extends BaseAdapter {
    public List<Retrieval> list;
    private LayoutInflater inflater;
    public Map<String,String> editorValue=new HashMap<>();
    int index;

    public RetrievalAdapter(Context context, List<Retrieval> list){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    //initialization
    private void init()
    {
        editorValue.clear();
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

        Retrieval retrieval = (Retrieval) this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.list_retrieval, null);
            viewHolder.tvDep = (TextView) convertView.findViewById(R.id.text_dep);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.text_des);
            viewHolder.tvScheduleQty = (TextView) convertView.findViewById(R.id.text_qty_s);
            viewHolder.edActualQty = (EditText) convertView.findViewById(R.id.text_qty_a);

            //set tag for edit text
            viewHolder.edActualQty.setTag(position);

            //get the position when edit text touched
            viewHolder.edActualQty.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    {
                        index= (int) view.getTag();
                    }
                    return false;
                }
            });

            viewHolder.edActualQty.addTextChangedListener(new MyTextWatcher(viewHolder));
            convertView.setTag(viewHolder);


        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.edActualQty.setTag(position);
        }


        viewHolder.tvDep.setText(retrieval.getDep());
        viewHolder.tvDep.setTextSize(13);
        viewHolder.tvDescription.setText(retrieval.getDescription());
        viewHolder.tvDescription.setTextSize(13);
        viewHolder.tvScheduleQty.setText(retrieval.getScheduledQty()+"");
        viewHolder.tvScheduleQty.setTextSize(13);
        viewHolder.edActualQty.setText(retrieval.getActualQty()+"");
        viewHolder.edActualQty.setTextSize(13);
        return convertView;
    }

    public static class ViewHolder{
        public TextView tvDep;
        public TextView tvDescription;
        public TextView tvScheduleQty;
        public EditText edActualQty;
    }

    //set text watcher for editText
    class MyTextWatcher implements TextWatcher
    {
        private ViewHolder mHolder;
        public MyTextWatcher(ViewHolder holder)
        {
            mHolder=holder;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            try
            {
                //reset data
                int position= (int) mHolder.edActualQty.getTag();
                if(Integer.parseInt(editable.toString())>=0)
                {
                    list.get(position).setActualQty(Integer.parseInt(editable.toString()));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}




