package com.example.stationeryproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApplyAdjustment extends AppCompatActivity implements AsyncToServer.IServerResponse{
    TextView tvItemId;
    TextView tvCategory;
    TextView tvDescription;
    EditText edReason;
    EditText edQty;
    Button mBtnSubmit;
    Adjustment adjustment=new Adjustment();
    Command cmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_adjustment);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();

        //get item
        final Bundle bundle=getIntent().getExtras();

        //get text view and set text
        adjustment.setItemId(bundle.getInt("itemId"));
        adjustment.setCategory(bundle.get("itemCategory").toString());
        adjustment.setDescription(bundle.get("itemDescription").toString());
        tvItemId=findViewById(R.id.tv_show_id);
        tvCategory=findViewById(R.id.tv_show_category);
        tvDescription=findViewById(R.id.tv_show_des);
        tvItemId.setText(adjustment.getItemId()+"");
        tvCategory.setText(adjustment.getCategory());
        tvDescription.setText(adjustment.getDescription());

        edReason=findViewById(R.id.ed_reason);
        edQty=findViewById(R.id.et_qty);

        //on submit
        mBtnSubmit=findViewById(R.id.submit_adjustment);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get input of edit text
                adjustment.setReason(edReason.getText().toString());
                adjustment.setQty(Integer.parseInt(edQty.getText().toString()));
                //send application to json
                JSONArray jsonArray=new JSONArray();
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("itemId",adjustment.getItemId());
                    jsonObject.put("category",adjustment.getCategory());
                    jsonObject.put("description",adjustment.getDescription());
                    jsonObject.put("quantity",adjustment.getQty());
                    jsonObject.put("reason",adjustment.getReason());

                    jsonArray.put(0,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                address need to be rewritten
                cmd = new Command(ApplyAdjustment.this, "set",
                        "http://10.0.2.2:64960/Android/CreateAdjustment", jsonArray);
                new AsyncToServer().execute(cmd);
            }
        });

    }
    @Override
    public void onServerResponse(JSONArray jsonArray) {
        //set alert dialog when task is completed
        AlertDialog.Builder builder=new AlertDialog.Builder(ApplyAdjustment.this);
        builder.setTitle("Successful");
        builder.setMessage("The request has been sent for approval");
        //click ok
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(ApplyAdjustment.this,StoreDashBoard.class);
                startActivity(intent);
            }
        });
        //click create another
        builder.setNegativeButton("create new", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(ApplyAdjustment.this,ChooseItem.class);
                startActivity(intent);

            }
        });
    }
}
