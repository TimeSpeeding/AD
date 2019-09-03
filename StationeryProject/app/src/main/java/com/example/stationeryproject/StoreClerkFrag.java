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

public class StoreClerkFrag extends Fragment {
    Button mBtnDisbursement;
    Button mBtnItem;
    Button mBtnRetrieval;
    Button mBtnHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.store_clerk_frag,container,false);
        mBtnDisbursement=view.findViewById(R.id.btn_disbursement_form);
        mBtnItem=view.findViewById(R.id.btn_view_item);
        mBtnRetrieval=view.findViewById(R.id.btn_retrieval_form);
        mBtnHistory=view.findViewById(R.id.btn_request_history);

        mBtnDisbursement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DisbursementForm.class);
                startActivity(intent);

            }
        });

        mBtnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewItems.class);
                startActivity(intent);
            }
        });

        mBtnRetrieval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),RetrievalTotal.class);
                startActivity(intent);
            }
        });

        mBtnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequisitionHistoryList.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
