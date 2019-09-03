package com.example.stationeryproject;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequisitionHistoryList extends AppCompatActivity implements SearchView.OnQueryTextListener,AsyncToServer.IServerResponse{
    ArrayList<Request> histories=new ArrayList<>();
    ListView showHistory;
    Command cmd;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_history);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();


        //get all request histories
        cmd = new Command(RequisitionHistoryList.this, "GET", "http://10.0.2.2:64960/Android/GetRequestDetail", null);
        new AsyncToServer().execute(cmd);

        //find search view
        searchView = findViewById(R.id.search_view_history);

        //set listener for search view
//        lvItems.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<Request> list = searchHistory(s);
        updateLayout(list);
        return false;
    }

    public ArrayList<Request> searchHistory(String name) {
        ArrayList<Request> mSearchList = new ArrayList<>();
        for (int i = 0; i < histories.size(); i++) {
            int index = histories.get(i).getApplicant().indexOf(name);
            if (index != -1) {
                mSearchList.add(histories.get(i));
            }
        }

        return mSearchList;
    }

    //update list when searched
    public void updateLayout(ArrayList<Request> list) {
        RequisitionHistoryAdapter adapter=new RequisitionHistoryAdapter(RequisitionHistoryList.this,list);
        showHistory.setAdapter(adapter);
    }


    @Override
    public void onServerResponse(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Request history = new Request(jsonObject.getInt("RequisitionId"),jsonObject.getString("empName"),jsonObject.getString("Date"),jsonObject.getString("Description"),
                        jsonObject.getInt("Quantity"),jsonObject.getString("Status"),jsonObject.getString("depName"));
                histories.add(history);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //find list view
        showHistory = findViewById(R.id.lv_request_history);
        //set list adapter

        RequisitionHistoryAdapter adapter = new RequisitionHistoryAdapter(RequisitionHistoryList.this, histories);
        showHistory.setAdapter(adapter);

    }

}
