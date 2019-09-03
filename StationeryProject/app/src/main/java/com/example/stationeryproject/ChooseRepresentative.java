package com.example.stationeryproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChooseRepresentative extends AppCompatActivity implements AsyncToServer.IServerResponse {

    SharedPreferences shared;
    String newRepresentative;
    String dep;
    Command cmd;
    List<String> emp = new ArrayList<>();
    ListView lvEmp;
    TextView currentRepre;
    String repreChoosed;
    Button mBtnSetRepre;
    EmpByDepAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_representative);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();


        //get dep by shared preference
        shared = getSharedPreferences("data", MODE_PRIVATE);
        dep = shared.getString("dep", null);

        //find widgets
        currentRepre = findViewById(R.id.tv_show_current_repre);
        lvEmp = findViewById(R.id.lv_emp_by_dep);

        mBtnSetRepre = findViewById(R.id.btn_change_repre);
        mBtnSetRepre.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                for(Map.Entry<String, Boolean> x :adapter.states.entrySet()){
                    if(x.getValue() == true){
                        String newRepresentative =  (String)adapter.getItem(Integer.parseInt(x.getKey()));
                        JSONObject jsonObj = new JSONObject();
                        JSONArray jsonArray = null;
                        try {
                            jsonObj.put("Username", newRepresentative);
                            jsonObj.put("Email", dep);
                            jsonArray = new JSONArray();
                            jsonArray.put(jsonObj);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        cmd = new Command(ChooseRepresentative.this, "GET", "http://10.0.2.2:64960/Android/setRepresentative", jsonArray);
                        new AsyncToServer().execute(cmd);
                        Intent intent = null;
                        intent=new Intent(ChooseRepresentative.this, StoreDashBoard.class);
                        intent.putExtra("role","HEAD");
                        startActivity(intent);
                    }
                }
            }
        });

        //start async task to get current collection point,find dep by user name
        cmd = new Command(ChooseRepresentative.this, "GET", "http://10.0.2.2:64960/Android/getRepresentative", null);
        new AsyncToServer().execute(cmd);
    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {
        //to set text for current point
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.getString("depName").equals(dep)) {
                    currentRepre.setText(jsonObject.getString("repreName"));
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //get emp list by dep
        for (int j = 0; j < jsonArray.length(); j++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                if (jsonObject.getString("depName").equals(dep)) {
                    System.out.println(jsonObject.getString("empName"));
                    emp.add(jsonObject.getString("empName"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //find list view and set adapter
        adapter=new EmpByDepAdapter(this,emp);
        lvEmp.setAdapter(adapter);


    }
}


