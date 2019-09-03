package com.example.stationeryproject;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// not in usage
public class StoreDashBoard extends AppCompatActivity  {
    String role="HEAD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();


//       Bundle bundle=getIntent().getExtras();
//       role=bundle.getString("role");
       if(role.equals("HEAD"))
       {
           DptHeadFrag dptHeadFrag=new DptHeadFrag();
           getSupportFragmentManager().beginTransaction().add(R.id.db_container,dptHeadFrag).commit();
       }
       else
       {
           StoreClerkFrag storeClerkFrag=new StoreClerkFrag();
           getSupportFragmentManager().beginTransaction().add(R.id.db_container,storeClerkFrag).commit();
       }
    }
}
