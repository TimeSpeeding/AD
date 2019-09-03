package com.example.stationeryproject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Delegate extends AppCompatActivity implements AsyncToServer.IServerResponse {

    EditText mEditFrom;
    EditText mEditTo;
    String delegated;
    Spinner spinner;
    String delegatedFrom;
    String delegatedTo;
    Button mBtnSubmitDelegate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate);

        String[] employees={"hans","esther","venkat"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.list_employee,R.id.tv_employee,employees);
        spinner=findViewById(R.id.sp_delegate_list);
        spinner.setAdapter(adapter);
        //set listener for spinner--delegated person
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                delegated = (String)spinner.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //show calendar for from date
        mEditFrom=findViewById(R.id.ed_from);
        mEditFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    showDatePickDlgFrom();
                    return true;
                }
                return false;
            }
        });

        mEditFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    showDatePickDlgFrom();
            }
        });

        mEditTo=findViewById(R.id.ed_to);
        mEditTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    showDatePickDlgTo();
                    return true;
                }
                return false;
            }
        });

        mEditTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    showDatePickDlgTo();
            }
        });

        //set submit onCliskListener

        mBtnSubmitDelegate=findViewById(R.id.btn_set_delegate);
        mBtnSubmitDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send data to json
                JSONArray jsonArray=new JSONArray();
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("delegated",delegated);
                    jsonObject.put("delegateFrom",delegatedFrom);
                    jsonObject.put("delegateTo",delegatedTo);
                    jsonArray.put(0,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Command cmd=new Command(Delegate.this,"set","xxx",jsonArray);
                new AsyncToServer().execute(cmd);


            }
        });
    }
    @Override
    public void onServerResponse(JSONArray jsonArray) {

    }

    protected void showDatePickDlgFrom() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Delegate.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Delegate.this.mEditFrom.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                delegatedFrom=mEditFrom.getText().toString();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    protected void showDatePickDlgTo() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Delegate.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Delegate.this.mEditTo.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                delegatedTo=mEditTo.getText().toString();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }



}
