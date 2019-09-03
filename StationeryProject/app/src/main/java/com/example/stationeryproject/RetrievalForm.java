package com.example.stationeryproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RetrievalForm extends AppCompatActivity implements AsyncToServer.IServerResponse{

    ArrayList<Retrieval> retrievals= new ArrayList<>();
    Command cmd;
    ListView showRetrieval;
    Button mBtnToSummary;
    Button mBtnSubmit;
    RetrievalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieval_form);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();

        cmd = new Command(RetrievalForm.this, "GET", "http://10.0.2.2:64960/Android/GetRetrievalByDept", null);
        new AsyncToServer().execute(cmd);

        mBtnToSummary=findViewById(R.id.btn_to_summary);
        mBtnToSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RetrievalForm.this,RetrievalTotal.class);
                startActivity(intent);
            }
        });
        mBtnSubmit = findViewById(R.id.submit);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray = new JSONArray();
                try{
                    for(Retrieval r :adapter.list){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("dep",r.dep);
                        jsonObject.put("description",r.description);
                        jsonObject.put("scheduledQty",r.scheduledQty);
                        jsonObject.put("actualQty",r.actualQty);
                        jsonArray.put(jsonObject);
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

                cmd = new Command(RetrievalForm.this, "GET", "http://10.0.2.2:64960/Android/SetDisbursement", jsonArray);
                new AsyncToServer().execute(cmd);
            }
        });
    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Retrieval retrieval =new Retrieval(jsonObject.getInt("DisbursementId"),jsonObject.getString("Name"),jsonObject.getString("Description"),
                        jsonObject.getInt("NeededQty"),jsonObject.getInt("RetrievalQty"));
                retrievals.add(retrieval);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //find list view
        showRetrieval = findViewById(R.id.lv_retrieval);
        //set list adapter

        adapter = new RetrievalAdapter(RetrievalForm.this, retrievals);
        showRetrieval.setAdapter(adapter);
    }
}
