package com.example.stationeryproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewItems extends AppCompatActivity implements AsyncToServer.IServerResponse,SearchView.OnQueryTextListener{
    Command cmd;
    List<Item> items=new ArrayList<>();
    ListView lvItem;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();

        cmd = new Command(ViewItems.this, "GET", "http://10.0.2.2:64960/Android/GetStationery", null);
        new AsyncToServer().execute(cmd);

        //find search view
        searchView = findViewById(R.id.search_item);

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
        ArrayList<Item> list = searchHistory(s);
        updateLayout(list);
        return false;
    }

    public ArrayList<Item> searchHistory(String name) {
        ArrayList<Item> mSearchList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            int index = items.get(i).getItemName().indexOf(name);
            if (index != -1) {
                mSearchList.add(items.get(i));
            }
        }

        return mSearchList;
    }

    //update list when searched
    public void updateLayout(ArrayList<Item> list) {
        ItemAdapter adapter=new ItemAdapter(ViewItems.this,list);
        lvItem.setAdapter(adapter);
    }



    @Override
    public void onServerResponse(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Item item=new Item(jsonObject.getInt("StationeryId"),jsonObject.getString("Description"),jsonObject.getString("Category"),
                        jsonObject.getInt("Qty"),jsonObject.getInt("ReorderLevel"),jsonObject.getString("UOM"));
                //all item list
                items.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            lvItem=findViewById(R.id.lv_item);
            ItemAdapter adapter=new ItemAdapter(ViewItems.this,items);
            lvItem.setAdapter(adapter);

        }
    }

