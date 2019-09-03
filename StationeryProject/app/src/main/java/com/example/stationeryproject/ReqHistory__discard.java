package com.example.stationeryproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

////Not using Cher Wah method for fetching of JSON. Kept for reference

/*public class ReqHistory extends AppCompatActivity implements View.OnClickListener, AsyncToServer.IServerResponse {

    Button btnGetHis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reqhistory);

        btnGetHis = findViewById(R.id.btnGetHistory);
        btnGetHis.setOnClickListener(this);
    }
        @Override
        public void onClick(View v) {
            Command cmd;
            cmd = new Command(this, "get",
                    "http://10.0.2.2:44361/home/ReqList", null);
            new AsyncToServer().execute(cmd);
            };

    @Override
    public void onServerResponse(JSONObject jsonObj) {
        if (jsonObj == null)
            return;

        try {
            String context = (String) jsonObj.get("context");

            if (context.compareTo("get") == 0)
            {
                String name = (String)jsonObj.get("name");
                int age = (int) jsonObj.get("age");
                System.out.println("name: " + name + ", age: " + age);
            }
            else if (context.compareTo("set") == 0)
            {
                String status = (String)jsonObj.get("status");
                System.out.println("status:" + status);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/













