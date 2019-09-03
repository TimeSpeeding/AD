package com.example.stationeryproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements AsyncToServer.IServerResponse{
    private EditText username;
    private EditText password;
    private Button login;
    private String usernameS;
    private String passwordS;
    SharedPreferences mShared;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Test
        Log.d("LOG",new Date(System.currentTimeMillis()).toString());

        setContentView(R.layout.activity_main);
        username = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);

        login = (Button)findViewById(R.id.button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    public void validate(){
        usernameS =  username.getText().toString();
        passwordS =  password.getText().toString();

        if(username.equals("") || password.equals("")){
            Toast.makeText(getBaseContext(),"Please do not leave any fields blank",Toast.LENGTH_SHORT).show();
        }else{
            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArray = null;
            try {
                jsonObj.put("Username", usernameS);
                jsonObj.put("Password", passwordS);
                jsonArray = new JSONArray();
                jsonArray.put(jsonObj);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            Command cmd = new Command(MainActivity.this, "Login", "http://10.0.2.2:64960/Android/Login", jsonArray);
            new AsyncToServer().execute(cmd);
        }
    }

    @Override
    public void onServerResponse(JSONArray jsonArray){
        for(int i=1;i<jsonArray.length();i++)
        {
            try{
                JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                if(jsonObject.getString("status").equals("OK")){
                    String role = jsonObject.getString("role");
                    String dep=jsonObject.getString("depName");
                    int UserId = jsonObject.getInt("UserId");
                    String Username = jsonObject.getString("UserName");
                    Intent intent = new Intent(MainActivity.this, DashBoard.class);

                    //save user info by sharedPreferences
                    mShared=getSharedPreferences("data",MODE_PRIVATE);
                    mEditor=mShared.edit();
                    mEditor.putString("dep",dep);
                    mEditor.putString("role",role);
                    mEditor.putInt("userId",UserId);
                    mEditor.putString("userName",Username);
                    mEditor.commit();

                    intent.putExtra("UserID",UserId);
                    startActivity(intent);
                }else{
                    Toast.makeText(getBaseContext(),"Invalid User Name or Password",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
