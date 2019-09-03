package com.example.stationeryproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeFrag extends Fragment implements View.OnClickListener, AsyncToServer.IServerResponse {
    Button mBtnApply;
    Button mBtnViewHistory;
    public Button mBtnConfirm;
    SharedPreferences mShared;
    String role;
    private Boolean ifShow = false;
    private int UserId = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //get role
        Bundle bundle = getArguments();
        if (bundle != null) {
            role = bundle.getString("role");
            UserId = bundle.getInt("UserID");
        }

        View view = inflater.inflate(R.layout.employee_frag, container, false);
        mBtnApply = view.findViewById(R.id.btn_apply);
        mBtnViewHistory = view.findViewById(R.id.btn_view_request_histories);
        mBtnConfirm = view.findViewById(R.id.btn_confirm);

        mBtnApply.setOnClickListener(this);
        mBtnViewHistory.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray = null;
        try {
            jsonObj.put("UserId", UserId);
            jsonArray = new JSONArray();
            jsonArray.put(jsonObj);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        Command cmd = new Command(EmployeeFrag.this, "GET", "http://10.0.2.2:64960/Android/GetRep", jsonArray);
        new AsyncToServer().execute(cmd);

        // later move this


        return view;
    }


    @Override
    public void onClick(View view) {

        Intent intent;
        int id = view.getId();
        switch (id) {
            case R.id.btn_apply:
                //to apply page
                    intent = new Intent(getActivity(), EmployeeDashboard.class);
                    intent.putExtra("UserID",UserId);
                    startActivity(intent);
                break;

            case R.id.btn_view_request_histories:
                intent = new Intent(getActivity(), RequestByPersonForm.class);
                startActivity(intent);
                break;

            case R.id.btn_confirm:
                intent = new Intent(getActivity(), DisbursementDep.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onServerResponse(JSONArray jsonArray) {
        for(int i=0;i<jsonArray.length();i++)
        {
            try{
                JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                ifShow = jsonObject.getBoolean("ifShow");
            }catch(JSONException e) {
                e.printStackTrace();
            }
            if (ifShow) {
                mBtnConfirm.setVisibility(View.VISIBLE);
            }
        }
    }
}


