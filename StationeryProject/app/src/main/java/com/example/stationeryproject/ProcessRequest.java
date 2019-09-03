package com.example.stationeryproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProcessRequest extends AppCompatActivity implements AsyncToServer.IServerResponse {
    int requestId;
    String applicant;
    String date;
    List<Request> currentRequest=new ArrayList<>();
    List<Request> historiesByEmp=new ArrayList<>();
    ListView showCurrentRequest;
    ListView showHistoryByEmp;
    Button mBtnApprove;
    Button mBtnReject;
    TextView tvApplicant;
    EditText edReason;
    TextView tvDate;
    Command cmd;
    Command cmdProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_request);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();


        //get all request and requestId from bundle
        Bundle bundle=getIntent().getExtras();
        requestId=bundle.getInt("requestId");
        applicant=bundle.getString("applicant");
        date=bundle.getString("date");

        //set text for applicant and date
        tvApplicant=findViewById(R.id.tv_show_applicant);
        tvDate=findViewById(R.id.tv_show_date);
        tvApplicant.setText(applicant);
        tvDate.setText(date);


        //get all requests(approved&pending)
        cmd = new Command(ProcessRequest.this, "GET", "http://10.0.2.2:64960/Android/GetRequestDetail", null);
        new AsyncToServer().execute(cmd);

        //set listener for approve and reject button
        mBtnReject=findViewById(R.id.btn_request_reject);
        mBtnApprove=findViewById(R.id.btn_request_approve);
        mBtnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),currentRequest.get(0).getRequestId()+"",Toast.LENGTH_SHORT).show();
                JSONArray jsonArrayUpdate=new JSONArray();
                JSONObject jsonObjectUpdate=new JSONObject();
                try {
                    jsonObjectUpdate.put("Status","Approved");
                    jsonObjectUpdate.put("RequisitionId",requestId);
                    jsonArrayUpdate.put(jsonObjectUpdate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                currentRequest=new ArrayList<>();
                historiesByEmp=new ArrayList<>();
                cmdProcess= new Command(ProcessRequest.this, "SET", "http://10.0.2.2:64960/Android/UpdateRequestStatus", jsonArrayUpdate);
                new AsyncToServer().execute(cmdProcess);

                Intent intent=new Intent(ProcessRequest.this,RequestList.class);
                startActivity(intent);
            }
        });

        mBtnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONArray jsonArrayUpdate=new JSONArray();
                JSONObject jsonObjectUpdate=new JSONObject();
                try {
                    jsonObjectUpdate.put("Status","Rejected");
                    jsonObjectUpdate.put("RequisitionId",currentRequest.get(0).getRequestId());
                    jsonArrayUpdate.put(jsonObjectUpdate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                currentRequest=new ArrayList<>();
                historiesByEmp=new ArrayList<>();
                cmdProcess= new Command(ProcessRequest.this, "SET", "http://10.0.2.2:64960/Android/UpdateRequestStatus", jsonArrayUpdate);
                new AsyncToServer().execute(cmdProcess);

                Intent intent=new Intent(ProcessRequest.this,RequestList.class);
                startActivity(intent);
            }

        });

    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Request history = new Request(jsonObject.getInt("RequisitionId"),jsonObject.getString("empName"),jsonObject.getString("Date"),jsonObject.getString("Description"),
                        jsonObject.getInt("Quantity"),jsonObject.getString("Status"),jsonObject.getString("depName"));
               if(history.applicant.equals(applicant))
               {
                   historiesByEmp.add(history);
               }

                if(history.requestId==requestId)
                {
                    currentRequest.add(history);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //find list view for current request
        showCurrentRequest = findViewById(R.id.lv_current_request);
        //set list adapter

        CurrentRequestAdapter adapter = new CurrentRequestAdapter(ProcessRequest.this, currentRequest);
        showCurrentRequest.setAdapter(adapter);

        //set for history
        showHistoryByEmp=findViewById(R.id.lv_request_history_by_emp);
        RequisitionHistoryByEmpAdapter adapter1=new RequisitionHistoryByEmpAdapter(ProcessRequest.this,historiesByEmp);
        showHistoryByEmp.setAdapter(adapter1);


    }
}
