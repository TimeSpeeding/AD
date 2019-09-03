package com.example.stationeryproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DptHeadFrag extends Fragment  {
    Button mBtnCollection;
    Button mBtnRepresentative;
    Button mBtnProcessRequest;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.head_dashboard_frag,container,false);
       mBtnCollection=view.findViewById(R.id.btn_choose_collection_point);
       mBtnRepresentative=view.findViewById(R.id.btn_choose_representative);
       mBtnProcessRequest=view.findViewById(R.id.btn_process_request);

       Click click=new Click();
      mBtnCollection.setOnClickListener(click);
      mBtnRepresentative.setOnClickListener(click);
        mBtnProcessRequest.setOnClickListener(click);

      return view;
    }

    public class Click implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            Intent intent;
            int id = view.getId();
            switch (id) {
                case R.id.btn_choose_collection_point:
                    intent=new Intent(getActivity(),ChooseCollectionPoint.class);
                    startActivity(intent);
                    break;

                case R.id.btn_choose_representative:
                    intent=new Intent(getActivity(),ChooseRepresentative.class);
                    startActivity(intent);
                    break;

                case R.id.btn_process_request:
                    intent=new Intent(getActivity(), RequestList.class);
                    startActivity(intent);
                    break;
            }

        }
    }
}


