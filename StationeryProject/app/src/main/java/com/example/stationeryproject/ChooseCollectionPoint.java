package com.example.stationeryproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChooseCollectionPoint extends AppCompatActivity implements AsyncToServer.IServerResponse{
    SharedPreferences shared;
    String newCollectPoint;
    String dep;
    Command cmd;
    TextView currentPoint;
    Button mBtnSetPoint;
    RadioGroup mRgPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_collection_point);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();


        //get dep by shared preference
        shared=getSharedPreferences("data",MODE_PRIVATE);
        dep=shared.getString("dep",null);

        //find widgets
        currentPoint=findViewById(R.id.tv_show_current);

        //start async task to get current collection point,find dep by user name
        cmd = new Command(ChooseCollectionPoint.this, "GET", "http://10.0.2.2:64960/Android/getCollectionPoint", null);
        new AsyncToServer().execute(cmd);

        //set listener for radio button
        mRgPoint=findViewById(R.id.rg_collections);
        mRgPoint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb=findViewById(mRgPoint.getCheckedRadioButtonId());
                newCollectPoint=rb.getText().toString();
            }
        });

        //submit to change point
        mBtnSetPoint=findViewById(R.id.btn_submit_collection);
        mBtnSetPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONArray jsonArray=new JSONArray();
                JSONObject object=new JSONObject();
                try {
                    object.put("dep",dep);
                    object.put("collectionPoint",newCollectPoint);
                    jsonArray.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cmd = new Command(ChooseCollectionPoint.this, "GET", "http://10.0.2.2:64960/Android/setCollectionPoint", jsonArray);
                new AsyncToServer().execute(cmd);

                Toast.makeText(getApplicationContext(),"The collection point has been changed",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ChooseCollectionPoint.this,StoreDashBoard.class);
                intent.putExtra("role","HEAD");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {
        //to set text for current point
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if(jsonObject.getString("depName").equals(dep))
                {
                    currentPoint.setText(jsonObject.getString("collectionName"));
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
