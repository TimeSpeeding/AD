package com.example.stationeryproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azim.spinner.SearchableSpinner;
import com.example.stationeryproject.adapter.InventoryAdapter;
import com.example.stationeryproject.model.Inventory;
import com.example.stationeryproject.rest_discard.RetrofitClient;
import com.example.stationeryproject.rest_discard.StationeryService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FragmentRequisition extends Fragment implements AsyncToServer.IServerResponse {

    // views
    private RecyclerView mRecyclerView;
    private SearchableSpinner mSpnCategory;
    private SearchableSpinner mSpnDescription;
    private SearchableSpinner mSpnQuantity;
    private Button mBtnAdd;
    private Button mBtnReset;
    private Button mBtnSubmit;

    // adapter
    private InventoryAdapter mAdapter;

    // rest service
    private StationeryService mStationeryService;

    //hashmap created to house the category : description as key value pairs in spinner
    Map<String, ArrayList<String>> mSpinDataMap = new HashMap<>();

    // recycler view data
    private List<Inventory> mInventoryList = new ArrayList<>();

    //data
    private int UserID;
    private List<Stationery> Stationeries;

    public FragmentRequisition(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EmployeeDashboard activity = (EmployeeDashboard) getActivity();
        UserID = activity.getUserID();

        View rootView = inflater.inflate(R.layout.requisition_fragment,container,false);

        // views. Ensure all assets in that fragment are instantiated and displayed.
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mSpnCategory = rootView.findViewById(R.id.spnCategory);
        mSpnDescription = rootView.findViewById(R.id.spnDescription);
        mSpnQuantity = rootView.findViewById(R.id.spnQuantity);
        mBtnAdd = rootView.findViewById(R.id.btnAdd);
        mBtnReset = rootView.findViewById(R.id.btnReset);
        mBtnSubmit = rootView.findViewById(R.id.btnSubmit);

        Command cmd = new Command(FragmentRequisition.this, "GetStationery", "http://10.0.2.2:64960/Android/GetStationery", null);
        new AsyncToServer().execute(cmd);

        // recycler view. Complete instantiation of a mrecyclerview(45) and madapter(54) from earlier and pair them up for a view
        // this will display the inventory list in a recycle view of requisition_fragment xml. Basically white portion list box.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new InventoryAdapter(getContext(), mInventoryList);
        mRecyclerView.setAdapter(mAdapter); //note InventoryAdapter is being used

        // retrofit client
        mStationeryService = RetrofitClient.getInstance().create(StationeryService.class);

        // Set the Add-To-List button to listen and invoke addInventory if clicked
        mBtnAdd.setOnClickListener(view -> {
            addInventory();
        });
        // Set the Reset button to listen and invoke resetInventory if clicked
        mBtnReset.setOnClickListener(view -> {
            resetInventory();
        });
        // Set the Submit button to listen and invoke submitInventories if clicked
        mBtnSubmit.setOnClickListener(view -> {
            if (mInventoryList.isEmpty()) {
                Toast.makeText(getActivity(), "Nothing to submit", Toast.LENGTH_SHORT).show();
                return;
            }
            submitInventories();
        });

        return rootView;
    }
    // load category and description from the resource. Key value pairs are hardcoded inside the resource as per spinner rules
    // create a hashmap for use in the spinner later on
    private void getCategoryDescriptions() {

        for (Stationery stationery : Stationeries) {
            String key = stationery.Category;
            String value = stationery.StationeryId + " " + stationery.Description;

            if (mSpinDataMap.containsKey(key)) {  //by right there is no prexisting value
                mSpinDataMap.get(key).add(value);
            } else {
                ArrayList<String> list = new ArrayList<>();  //load values into mSpin hash map
                list.add(value);
                mSpinDataMap.put(key, list);
            }
        }
    }

    @Override
    public void onServerResponse(JSONArray jsonArray){
        if(jsonArray == null)
            return;
        String context = "";
        try{
            JSONObject jsonObject= (JSONObject) jsonArray.get(0);
            context = jsonObject.getString("context");
            switch(context){
                case "GetStationery":
                    Stationeries = new ArrayList<>();
                    for(int i=1;i<jsonArray.length();i++)
                    {
                        jsonObject= (JSONObject) jsonArray.get(i);
                        Stationery stationery = new Stationery();
                        stationery.StationeryId = jsonObject.getInt("StationeryId");
                        stationery.Category = jsonObject.getString("Category");
                        stationery.Description = jsonObject.getString("Description");
                        Stationeries.add(stationery);
                    }
                    getCategoryDescriptions();
                    initSpinner();
                    break;
                case "PostRequisition":
                    if(jsonObject.getString("status").equals("OK")){
                        Toast.makeText(getContext(), "Submit Successfully!", Toast.LENGTH_SHORT).show();
                        resetInventory();
                    }

                    else {
                        Toast.makeText(getContext(), "Submit failed!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }

    //Now load hashmap values into spinner
    // pre-fill each spinner category from XML with a starting default text
    private void initSpinner() {

        mSpnCategory.setDefaultText("Select Category");
        mSpnCategory.setInvalidTextColor(getResources().getColor(R.color.colorAccent));

        mSpnDescription.setDefaultText("Select Description");
        mSpnDescription.setInvalidTextColor(getResources().getColor(R.color.colorAccent));

        mSpnQuantity.setDefaultText("Select Quantity");
        mSpnQuantity.setInvalidTextColor(getResources().getColor(R.color.colorAccent));

        //User mSpinDataMap hashmap values to load into spinner
        //Delivers all key values from hashmap and store in a set
        Set<String> keySet = mSpinDataMap.keySet();
        String[] categories = keySet.toArray(new String[keySet.size()]); //Converts hashmap object keys to arrays, creating length based on size of hashmap
        mSpnCategory.setData(categories); //add into category spinner

        //set number range of quantity spinner for later use
        String[] quantities = new String[100];
        for (int i = 0; i < 100; i++) {
            quantities[i] = String.valueOf(i + 1);
        }

        // Set selection listener for category spinner
        // Based on the key selected, pull the corresponding value which is description
        // Then load it into the description spinner.
        // This is the true logical basis for description spinner to get values based on category spinner
        mSpnCategory.setSelectionListener((spinnerId, position, value) -> {
            ArrayList<String> descriptionArray = mSpinDataMap.get(value);
            String[] descriptions = descriptionArray.toArray(new String[descriptionArray.size()]);
            mSpnDescription.setData(descriptions);
            mSpnDescription.setText("");
            mSpnQuantity.setText("");
        });

        // Set selection listener for description spinner
        // Only then can you unlock the use of Quantity spinnner. First 2 conditions above must be fulfilled first
        mSpnDescription.setSelectionListener((spinnerId, position, value) -> {
            mSpnQuantity.setData(quantities);
            mSpnQuantity.setText("");
        });

        mSpnQuantity.setSelectionListener((spinnerId, position, value) -> {

        });
    }

    // On pressing the Add to List Button, invoke addInventory() and pushes info to the recycle view list area
    private void addInventory() {
        // validate
        if (mSpnCategory.getValue() == null || mSpnCategory.getValue().isEmpty() ||
                mSpnDescription.getValue() == null || mSpnDescription.getValue().isEmpty() ||
                mSpnQuantity.getValue() == null || mSpnQuantity.getValue().isEmpty()) {
            Toast.makeText(getContext(), "Invalid input value", Toast.LENGTH_SHORT).show();
            return;
        }

        // If the validation null test passes, then transfer data from the spinners to place in list
        Inventory item = new Inventory();
        item.setCategory(mSpnCategory.getValue());    //transfer category detail to list
        item.setDescription(mSpnDescription.getValue()); //transfer description detail to list

        Long quantity = 0L;
        try {
            quantity = Long.parseLong(mSpnQuantity.getValue()); //transfer quantity detail to list
        } catch (NumberFormatException e) {}
        item.setQuantity(quantity);

        // add and display the inventory list shown in recycle view
        mInventoryList.add(item);        // add() is recognized function to work in conjunction with notifyDataSet below
        mAdapter.notifyDataSetChanged(); //alert of change to data and refresh the view
                                         //only works with add(), insert(), remove(), and clear()

        // reset values, initialize
        mSpnDescription.setData(new String[0]);
        mSpnQuantity.setData(new String[0]);
        mSpnCategory.setText("");
        mSpnDescription.setText("");
        mSpnQuantity.setText("");
    }

    // On pressing the Reset Button, invoke resetInventory()
    private void resetInventory() {
        mInventoryList.clear();  // clear() is recognized function to work in conjunction with notifyDataSet below
        mAdapter.notifyDataSetChanged();//alert of change to data and refresh the view //only works with add(), insert(), remove(), and clear()
    }

    // On pressing the Submit Button, invoke submitInventories()
    private void submitInventories() {
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObj.put("UserId", UserID);
            jsonArray.put(jsonObj);
            for (Inventory inventory : mInventoryList) {
                int StationeryId = Integer.parseInt(inventory.getDescription().substring(0,inventory.getDescription().indexOf(" ")));
                int Qty = inventory.getQuantity().intValue();
                jsonObj = new JSONObject();
                jsonObj.put("StationeryId",StationeryId);
                jsonObj.put("Qty",Qty);
                jsonArray.put(jsonObj);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Command cmd = new Command(FragmentRequisition.this, "PostRequisition", "http://10.0.2.2:64960/Android/PostRequisition", jsonArray);
        new AsyncToServer().execute(cmd);
    }
}
