package com.example.stationeryproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChooseItem extends AppCompatActivity implements SearchView.OnQueryTextListener, AsyncToServer.IServerResponse {

    ListView lvItems;
    SearchView searchView;
    ArrayList<Item> items = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    Item itemSelected;
    Command cmd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment_choose_item);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();

        //get all items
        cmd = new Command(ChooseItem.this, "GET", "https://api.myjson.com/bins/getef", null);
        new AsyncToServer().execute(cmd);

        //find search view
        searchView = findViewById(R.id.search_view);


        //set listener for search view
//        lvItems.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);

    }

    //extract item name as a list
    public void loadData() {
        for(int i=0;i<items.size();i++)
        {
            names.add(items.get(i).getItemName());
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<String> list = searchItem(s);
        updateLayout(list);
        return false;
    }

    public ArrayList<String> searchItem(String name) {
        ArrayList<String> mSearchList = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            int index = names.get(i).indexOf(name);
            if (index != -1) {
                mSearchList.add(names.get(i));
            }
        }

        return mSearchList;
    }

    //update list when searched
    public void updateLayout(ArrayList<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_description, R.id.tv_list_item_description, list);
        lvItems.setAdapter(adapter);

    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Item item = new Item(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getString("category"),
                        jsonObject.getInt("quantity"), jsonObject.getInt("safety quantity"), jsonObject.getString("unit"));
                items.add(item);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        //get item name list
        loadData();

        //find list view widget and set adapter
        lvItems = findViewById(R.id.lv_items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_description, R.id.tv_list_item_description, names);
        lvItems.setAdapter(adapter);

        //set click listener for list view and go to apply page
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get the item name clicked
                String itemName = (String) adapterView.getItemAtPosition(i);

                //get item
                for(int index=0;index<items.size();index++)
                {
                    if(itemName.equals(items.get(index).getItemName()))
                    {
                        itemSelected=items.get(index);
                        break;
                    }
                }
                Intent intent = new Intent(ChooseItem.this, ApplyAdjustment.class);
                intent.putExtra("itemDescription", itemSelected.getItemName());
                intent.putExtra("itemCategory",itemSelected.getCategory());
                intent.putExtra("itemId",itemSelected.getId());
                startActivity(intent);
            }
        });

    }
}
