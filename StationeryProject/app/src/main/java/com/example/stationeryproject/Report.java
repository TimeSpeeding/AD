package com.example.stationeryproject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Report extends AppCompatActivity {
    EditText mEditFrom;
    EditText mEditTo;
    Spinner spinner;
    String dateFrom;
    String dateTo;
    String dep;
    CheckBox cb;
    List<String> categories=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //add header frag
        HeaderFrag headerFrag=new HeaderFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.header_frag,headerFrag).commit();

        //set for spinner
        String[] departments={"English Dept","Computer Science","Commerce Dept","Register Dept","Zoology Dept"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.list_employee,R.id.tv_employee,departments);
        spinner=findViewById(R.id.sp_report_dep);
        spinner.setAdapter(adapter);
        //set listener for spinner--delegated person
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dep = (String)spinner.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //show calendar for from date and get input
        mEditFrom=findViewById(R.id.ed_report_from);
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

        //show and get input
        mEditTo=findViewById(R.id.ed_report_to);
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

        //set for category checkbox
        for(int i=1;i<15;i++)
        {
            int id=getResources().getIdentifier("cb"+i,"id",this.getPackageName());
            cb=findViewById(id);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String ss="";
                    //if choosed
                    if(b)
                    {
                        categories.add(cb.getText().toString());
                        for(String s:categories)
                        {
                            ss+=s;
                        }
                        System.out.println(ss);
                    }
                    if(!b)
                    {
                        categories.remove(cb.getText().toString());
                        for(String s:categories)
                        {
                            ss+=s;
                        }
                        System.out.println(ss);
                    }
                }
            });
        }


    }

    protected void showDatePickDlgFrom() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Report.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Report.this.mEditFrom.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                dateFrom=mEditFrom.getText().toString();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    protected void showDatePickDlgTo() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Report.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Report.this.mEditTo.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                dateTo=mEditTo.getText().toString();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
}
