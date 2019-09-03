package com.example.stationeryproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.example.stationeryproject.adapter.InventoriesOfHistoryAdapter;
import com.example.stationeryproject.model.History;
import com.example.stationeryproject.model.Inventory;
import com.example.stationeryproject.rest_discard.RetrofitClient;
import com.example.stationeryproject.rest_discard.StationeryService;
import com.example.stationeryproject.ui.datepicker.DatePickerController;
import com.example.stationeryproject.ui.datepicker.SublimePickerFragment;
import com.example.stationeryproject.util.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHistory extends Fragment implements AsyncToServer.IServerResponse{

    // views
    private Button mBtnPickDateRange;
    private TextView mTxtStartDate;
    private TextView mTxtEndDate;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;


    // rest service
    private StationeryService mStationeryService;

    // data
    private List<History> mHistoryList = new ArrayList<>();
    private int UserID;

    private List<Inventory> mInventoryList = new ArrayList<>();

    // date time picker
    private DatePickerController mDatePickerController;
    private Date mStartDate;
    private Date mEndDate;

    // progress bar control
    private int mProgressingCount = 0;

    public FragmentHistory(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EmployeeDashboard activity = (EmployeeDashboard) getActivity();
        UserID = activity.getUserID();

        View rootView = inflater.inflate(R.layout.history_fragment,container,false);

        // views
        mBtnPickDateRange = rootView.findViewById(R.id.btnPickDateRange);
        mTxtStartDate = rootView.findViewById(R.id.txtStartDate);
        mTxtEndDate = rootView.findViewById(R.id.txtEndDate);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mProgressBar = rootView.findViewById(R.id.progressBar);

        // recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // today
        mStartDate = DateUtils.atStartOfDay(new Date());
        mEndDate = DateUtils.atEndOfDay(new Date());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy MMM dd");
        mTxtStartDate.setText(simpleDateFormat.format(mStartDate));
        mTxtEndDate.setText(simpleDateFormat.format(mEndDate));

        // date time picker, when you select a date, it initiates a callback used at 138, passes date result back.
        mDatePickerController = DatePickerController.getInstance(getActivity(), mFragmentCallback);

        // Pick Date Range button
        mBtnPickDateRange.setOnClickListener(view -> {
            mDatePickerController.showRangeSelectPicker();  //taken from DatePickerController, loads open calendar
        });

        // retrofit client
        mStationeryService = RetrofitClient.getInstance().create(StationeryService.class);

        // fetch data
        updateHistory();

        return rootView;
    }

    private void updatedDateRange() {




    }

    // The callback for date picker pass the result.
    private SublimePickerFragment.Callback mFragmentCallback =
            new SublimePickerFragment.Callback() {
                @Override
                public void onCancelled() {
                }

                @Override
                public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                        int hourOfDay, int minute,
                        SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                        String recurrenceRule) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy MMM dd");

                    // save the start, end date
                    mStartDate = DateUtils.atStartOfDay(selectedDate.getStartDate().getTime());
                    mEndDate = DateUtils.atEndOfDay(selectedDate.getEndDate().getTime());

                    // update the start, end date textview
                    mTxtStartDate.setText(simpleDateFormat.format(mStartDate));
                    mTxtEndDate.setText(simpleDateFormat.format(mEndDate));

                    // update the recycler view
                    updatedDateRange();
                }
            };

    private void showProgressBar() {
        mProgressingCount++;
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressingCount--;
        if (mProgressingCount == 0)
            mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void updateHistory() {
        showProgressBar();
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObj.put("UserId", UserID);
            jsonObj.put("StartDate",mStartDate.toString());
            jsonObj.put("EndDate",mEndDate.toString());
            jsonArray.put(jsonObj);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Command cmd = new Command(FragmentHistory.this, "GetRequisition", "http://10.0.2.2:64960/Android/GetRequisition", jsonArray);
        new AsyncToServer().execute(cmd);
        // another command maybe
        getInventories();
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
                case "GetRequisition":
                    mHistoryList = new ArrayList<>();
                    for(int i=1;i<jsonArray.length();i++)
                    {
                        jsonObject= (JSONObject) jsonArray.get(i);
                        History history = new History();
                        Log.d("LOG",Integer.toString(jsonObject.getInt("RequisitionId")));
                        Log.d("LOG",jsonObject.getString("Status"));
                        history.setId(jsonObject.getInt("RequisitionId"));
                        history.setTimestamp(jsonObject.getString("Status"));
                        mHistoryList.add(history);
                    }


                    hideProgressBar();
                    break;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //fetch all the transaction masterlist
    private void getInventories() {
        showProgressBar();

        Call<List<Inventory>> call = mStationeryService.getAllInventories();
        call.enqueue(new Callback<List<Inventory>>() {
            @Override
            public void onResponse(Call<List<Inventory>> call, Response<List<Inventory>> response) {
                hideProgressBar();

                List<Inventory> data = response.body();
                if (data == null) {
                    Toast.makeText(getContext(), "The response is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                mInventoryList.clear();
                mInventoryList.addAll(data);
            }

            @Override
            public void onFailure(Call<List<Inventory>> call, Throwable t) {
                hideProgressBar();

            }
        });
    }


    private void onClickHistory(History history) {
        int transactionId = history.getId();
        List<Inventory> inventories = new ArrayList<>();

        // filter the mInventoryList according to the transaction id
        // Use the transactionId derived above to match with inventory list and draw out the exact contents upon hit
        for (Inventory inventory : mInventoryList) {
            if (inventory.getTransactionId() == transactionId) {
                inventories.add(inventory); //subsequently load this for display through the dialog and adapters below
            }
        }

        // show inventories dialog
        View subView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_inventories, null);
        ListView listView = subView.findViewById(R.id.listView); //retrieve as table layout
        InventoriesOfHistoryAdapter adapter = new InventoriesOfHistoryAdapter(getContext(), inventories); //load using InventoriesofHistoryAdapter
        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); //display as popup
        builder.setTitle("Inventories")
                .setView(subView)
                .create();
        builder.setPositiveButton("Close", (dialogInterface, i) -> {});
        builder.show();
    }
}
