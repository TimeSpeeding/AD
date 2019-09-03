package com.example.stationeryproject;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Report2Activity extends AppCompatActivity implements AsyncToServer.IServerResponse{
    private ArrayList<String> departments = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();

        // adapter for period

        // command for department String List
        Command cmd = new Command(Report2Activity.this, "GetDepartment", "http://10.0.2.2:64960/Android/GetDepartment", null);
        new AsyncToServer().execute(cmd);
    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {
        for(int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                departments.add(jsonObject.getString("Name"));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departments);
        Spinner spinner = (Spinner) findViewById(R.id.sp_report_dep);
        spinner.setAdapter(adapter);
    }
}
