package com.example.stationeryproject;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestDetailByPerson extends AppCompatActivity implements AsyncToServer.IServerResponse{
    int requestId;
    String date;
    List<Request> currentRequest=new ArrayList<>();
    ListView showCurrentRequest;
    TextView tvDate;
    Command cmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail_by_person);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();

        //get all request and requestId from bundle
        Bundle bundle=getIntent().getExtras();
        requestId=bundle.getInt("requestId");
        date=bundle.getString("date");

        //set text for applicant and date
        tvDate=findViewById(R.id.tv_show_date);
        tvDate.setText(date);

        //get all requests(approved&pending)
        cmd = new Command(RequestDetailByPerson.this, "GET", "http://10.0.2.2:64960/Android/GetRequestDetail", null);
        new AsyncToServer().execute(cmd);

    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Request history = new Request(jsonObject.getInt("RequisitionId"),jsonObject.getString("empName"),jsonObject.getString("Date"),jsonObject.getString("Description"),
                        jsonObject.getInt("Quantity"),jsonObject.getString("Status"),jsonObject.getString("depName"));

                if(history.requestId==requestId)
                {
                    currentRequest.add(history);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //find list view for current request
        showCurrentRequest = findViewById(R.id.lv_request_detail_by_person);
        //set list adapter
        CurrentRequestAdapter adapter = new CurrentRequestAdapter(RequestDetailByPerson.this, currentRequest);
        showCurrentRequest.setAdapter(adapter);
    }
}
