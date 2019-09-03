package com.example.stationeryproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestByPersonForm extends AppCompatActivity implements AsyncToServer.IServerResponse{

    ArrayList<Request> requests=new ArrayList<>();
    ListView showRequest;
    Command cmd;
    String emp;
    SharedPreferences mShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_by_person_form);

        //get data from shared preference
        mShared=getSharedPreferences("data",MODE_PRIVATE);
        emp=mShared.getString("userName",null);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();

        //get all requests(approved&pending)
        cmd = new Command(RequestByPersonForm.this, "GET", "http://10.0.2.2:64960/Android/GetRequest", null);
        new AsyncToServer().execute(cmd);
    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Request history = new Request(jsonObject.getInt("RequisitionId"),jsonObject.getString("empName"),jsonObject.getString("Date"),
                        jsonObject.getString("Status"),jsonObject.getString("depName"));

                if(history.applicant.equals(emp))
                {

                    requests.add(history);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //find list view
        showRequest = findViewById(R.id.lv_request_history_by_person);
        //set list adapter

        RequestListByPersonAdapter adapter = new RequestListByPersonAdapter(RequestByPersonForm.this, requests);
        showRequest.setAdapter(adapter);

        //set click listener for list view
        showRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get the request been clicked
                Request request= (Request) adapterView.getItemAtPosition(i);

                Intent intent=new Intent(RequestByPersonForm.this,RequestDetailByPerson.class);
                intent.putExtra("requestId",request.getRequestId());
                intent.putExtra("date",request.getRequestDate());
                startActivity(intent);
            }
        });
    }
}
