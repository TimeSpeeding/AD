package com.example.stationeryproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.stationeryproject.adapter.ViewPageAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//This is the first page upon sign in of employee. It is split into 2 fragment views and can be accessed in a tab format.
//Adapter for this display and tab handling is inside ViewPage Adapter

public class EmployeeDashboard extends AppCompatActivity implements AsyncToServer.IServerResponse {
    private Button logout;
    private TabLayout tablayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private int UserID;

    private FragmentRequisition mFragmentRequisition;
    private FragmentHistory mFragmentHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        logout = (Button) findViewById(R.id.logout);
        appBarLayout = (AppBarLayout) findViewById(R.id.tabbar);
        tablayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpage);
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        Bundle bundle=getIntent().getExtras();
        UserID = bundle.getInt("UserID");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }

            public void Logout() {
                Command cmd = new Command(EmployeeDashboard.this, "Logout", "http://10.0.2.2:64960/Android/Logout", null);
                new AsyncToServer().execute(cmd);
                Intent intent = new Intent(EmployeeDashboard.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //add all fragments
        mFragmentRequisition = new FragmentRequisition();
        mFragmentHistory = new FragmentHistory();

        adapter.AddFragment(mFragmentRequisition, "Stationery Request");
        adapter.AddFragment(mFragmentHistory, "Request History");
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onServerResponse(JSONArray jsonArray){
        for(int i=1;i<jsonArray.length();i++)
        {
            try{
                JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                if(jsonObject.getString("status").equals("OK")){
                    Intent intent = new Intent(EmployeeDashboard.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getBaseContext(),"Failed to Log out",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateHistory() {
        mFragmentHistory.updateHistory();
    }

    public int getUserID() {
        return UserID;
    }
}
