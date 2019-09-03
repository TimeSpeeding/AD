package com.example.stationeryproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisbursementDep extends AppCompatActivity implements AsyncToServer.IServerResponse{

    ArrayList<DisbursementDepEntity> disbursements = new ArrayList<>();
    Command cmd;
    ListView showDisbursement;
    Button mBtnRefresh;
    Button mBtnConfirm;
    String headDep;
    SharedPreferences mShared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement_dep);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();


        cmd = new Command(DisbursementDep.this, "GET", "http://10.0.2.2:64960/Android/GetDisbursement", null);
        new AsyncToServer().execute(cmd);

        //get data from shared preference
        mShared=getSharedPreferences("data",MODE_PRIVATE);
        headDep=mShared.getString("dep",null);
        System.out.println("dep:"+headDep);

        //start async task again when click refresh
        mBtnRefresh=findViewById(R.id.btn_refresh);
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disbursements=new ArrayList<>();
                cmd = new Command(DisbursementDep.this, "GET", "http://10.0.2.2:64960/Android/GetDisbursement", null);
                new AsyncToServer().execute(cmd);
            }
        });

        //send json and update status when click confirm
        mBtnConfirm=findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray=new JSONArray();
                for(int i=0;i<disbursements.size();i++)
                {


                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("id",disbursements.get(i).getId());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //start async
                cmd = new Command(DisbursementDep.this, "GET", "http://10.0.2.2:64960/Android/UpdateDisbursementStatus", jsonArray);
                new AsyncToServer().execute(cmd);
            }
        });

    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {

        for (int i = 0; i < 10; i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                DisbursementDepEntity disbursement = new DisbursementDepEntity(jsonObject.getInt("DisbursementId"),jsonObject.getString("Description"),
                        jsonObject.getInt("NeededQty"), jsonObject.getInt("RetrievalQty"), jsonObject.getInt("DeliveryQty"));
                if(jsonObject.getString("Name").equals(headDep))
                {
                    disbursements.add(disbursement);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //find list view
        showDisbursement = findViewById(R.id.lv_disbursement_dep);
        //set list adapter

        DisbursementDepAdapter adapter = new DisbursementDepAdapter(DisbursementDep.this, disbursements);
        showDisbursement.setAdapter(adapter);

    }
}
