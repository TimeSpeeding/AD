package com.example.stationeryproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RetrievalTotal extends AppCompatActivity implements AsyncToServer.IServerResponse{
    Command cmd;
    ArrayList<StationeryNeeded> list=new ArrayList<>();
    ListView lvRetrievalTotal;
    Button mBtnBreakdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieval_total);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();


        cmd = new Command(RetrievalTotal.this, "GET", "http://10.0.2.2:64960/Android/GetRetrievalTotal", null);
        new AsyncToServer().execute(cmd);

        mBtnBreakdown=findViewById(R.id.bnt_to_breakdown);
        mBtnBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RetrievalTotal.this,RetrievalForm.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                StationeryNeeded stationeryNeeded =new StationeryNeeded(jsonObject.getString("stationeryName"),
                        jsonObject.getInt("totalNeededQty"),jsonObject.getInt("totalRetrievalQty"));
                list.add(stationeryNeeded);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //find list view
        lvRetrievalTotal=findViewById(R.id.lv_retrieval_total);
        //set list adapter

        RetrievalTotalAdapter adapter=new RetrievalTotalAdapter(this,list);
        lvRetrievalTotal.setAdapter(adapter);
    }
}
