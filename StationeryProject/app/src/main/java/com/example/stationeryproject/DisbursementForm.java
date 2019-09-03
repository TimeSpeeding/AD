package com.example.stationeryproject;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DisbursementForm extends AppCompatActivity implements AsyncToServer.IServerResponse {
    ArrayList<Disbursement> disbursements = new ArrayList<>();
    Command cmd;
    ListView showDisbursement;
    Spinner spinner;
    String dept;
    Button mBtnRefresh;
    DisbursementAdapter disbursementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement_form);

        //add header frag
        HeaderFrag headerFrag = new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag, headerFrag).commit();

        //set data for dep spinner
        String[] departments = {"English Dept", "Computer Science", "Commerce Dept","Registrar Dept","Zoology Dept"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.sp_dept, R.id.tv_dept, departments);
        spinner = findViewById(R.id.sp_department);
        spinner.setAdapter(adapter);
        //set listener for spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dept = (String) spinner.getItemAtPosition(i);
                cmd = new Command(DisbursementForm.this, "GET", "http://10.0.2.2:64960/Android/GetDisbursement", null);
                new AsyncToServer().execute(cmd);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Refresh Button
        mBtnRefresh = findViewById(R.id.refresh);
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray = new JSONArray();
                for(Disbursement d : disbursementAdapter.list){
                    JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put("id",d.id);
                        jsonObj.put("description",d.description);
                        jsonObj.put("dep",d.dep);
                        jsonObj.put("scheduledQty",d.scheduledQty);
                        jsonObj.put("actualQty",d.actualQty);
                        jsonObj.put("deliveryQty",d.deliveryQty);
                        jsonArray.put(jsonObj);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                cmd = new Command(DisbursementForm.this, "GET", "http://10.0.2.2:64960/Android/UpdateDisbursement", jsonArray);
                new AsyncToServer().execute(cmd);
            }
        });
    }
    //add date to Arraylist
    @Override
    public void onServerResponse(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                System.out.println("start async");
                Disbursement disbursement =new Disbursement(jsonObject.getInt("DisbursementId"),jsonObject.getString("Name"),jsonObject.getString("Description"),
                        jsonObject.getInt("NeededQty"),jsonObject.getInt("RetrievalQty"),jsonObject.getInt("DeliveryQty"));
                System.out.println("id:"+disbursement.getId());
                System.out.print(disbursement.getId());
                if(disbursement.dep.equals(dept))
                {
                    disbursements.add(disbursement);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //find list view
        showDisbursement=findViewById(R.id.lv_disbursement);
        //set list adapter

        disbursementAdapter=new DisbursementAdapter(this, disbursements);
        showDisbursement.setAdapter(disbursementAdapter);

        disbursements =new ArrayList<Disbursement>();

    }
}
