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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisbursementAdapter extends BaseAdapter implements AsyncToServer.IServerResponse{
    public List<Disbursement> list;
    private LayoutInflater inflater;
    public Map<String,String> editorValue=new HashMap<>();
    int index;
    Command cmd;


    public DisbursementAdapter(Context context, List<Disbursement> list){
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

        Disbursement disbursement = (Disbursement) this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.list_disbursement, null);
            viewHolder.tvDep = (TextView) convertView.findViewById(R.id.text_dep);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.text_des);
            viewHolder.tvScheduleQty = (TextView) convertView.findViewById(R.id.text_qty_s);
            viewHolder.edDeliveryQty = (EditText) convertView.findViewById(R.id.text_qty_a);
            viewHolder.tvActualQty=(TextView)convertView.findViewById(R.id.text_qty_d) ;

            //set tag for edit text
            viewHolder.edDeliveryQty.setTag(position);

            //get the position when edit text touched
            viewHolder.edDeliveryQty.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    {
                        index= (int) view.getTag();
                    }
                    return false;
                }
            });

            viewHolder.edDeliveryQty.addTextChangedListener(new MyTextWatcher(viewHolder));
            convertView.setTag(viewHolder);


        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.edDeliveryQty.setTag(position);
        }


        viewHolder.tvDep.setText(disbursement.getDep());
        viewHolder.tvDep.setTextSize(13);
        viewHolder.tvDescription.setText(disbursement.getDescription());
        viewHolder.tvDescription.setTextSize(13);
        viewHolder.tvScheduleQty.setText(disbursement.getScheduledQty()+"");
        viewHolder.tvScheduleQty.setTextSize(13);
        viewHolder.edDeliveryQty.setText(disbursement.getDeliveryQty()+"");
        viewHolder.edDeliveryQty.setTextSize(13);
        viewHolder.tvActualQty.setText(disbursement.getActualQty()+"");
        viewHolder.tvActualQty.setTextSize(13);
        return convertView;
    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {

    }

    public static class ViewHolder{
        public TextView tvDep;
        public TextView tvDescription;
        public TextView tvScheduleQty;
        public EditText edDeliveryQty;
        public TextView tvActualQty;
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
                int position= (int) mHolder.edDeliveryQty.getTag();
                if(Integer.parseInt(editable.toString())>=0)
                {
                    list.get(position).setDeliveryQty(Integer.parseInt(editable.toString()));

//                    //start async task to send updated data to web
//                    JSONArray jsonArray=new JSONArray();
//                    JSONObject object=new JSONObject();
//                    object.put("id",list.get(position).getId());
//                    object.put("deliveryQty",list.get(position).getDeliveryQty());
//                    jsonArray.put(0,object);
//
//                    cmd = new Command(myActivity, "POST", "http://10.0.2.2:64960/Android/UpdateDisbursement", jsonArray);
//                    new AsyncToServer().execute(cmd);
                }
//
//                else
//                    Toast.makeText(context,"Quantity can not be negative",Toast.LENGTH_SHORT).show();
//
//
            }
            catch (Exception e)
            {
//                //show error msg
//                Toast.makeText(context,"you have entered invalid value",Toast.LENGTH_SHORT).show();
//

            }
        }
    }

}




