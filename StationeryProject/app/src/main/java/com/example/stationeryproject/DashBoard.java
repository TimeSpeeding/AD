package com.example.stationeryproject;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DashBoard extends AppCompatActivity  {

    String role;
    SharedPreferences mShared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();

        //get data from shared preference
        mShared=getSharedPreferences("data",MODE_PRIVATE);
        role=mShared.getString("role",null);

        //send role to frag if role is repre
        Bundle bundle=new Bundle();
        bundle.putString("role",role);
        bundle.putInt("UserID",mShared.getInt("userId",0));

       if(role.equals("HEAD"))
       {
           DptHeadFrag dptHeadFrag=new DptHeadFrag();
           getSupportFragmentManager().beginTransaction().add(R.id.db_container,dptHeadFrag).commit();
       }
       else if(role.equals("CLERK"))
       {
           StoreClerkFrag storeClerkFrag=new StoreClerkFrag();
           getSupportFragmentManager().beginTransaction().add(R.id.db_container,storeClerkFrag).commit();

       }

       else if(role.equals("EMPLOYEE"))
       {
           EmployeeFrag frag=new EmployeeFrag();
           frag.setArguments(bundle);
           getSupportFragmentManager().beginTransaction().add(R.id.db_container,frag).commit();

       }
       else if(role.equals("REPRESENTATIVE"))
       {
           EmployeeFrag frag=new EmployeeFrag();
           frag.setArguments(bundle);
           getSupportFragmentManager().beginTransaction().add(R.id.db_container,frag).commit();

       }

    }
}
