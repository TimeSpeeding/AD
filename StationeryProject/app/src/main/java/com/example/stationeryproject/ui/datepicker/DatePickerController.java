package com.example.stationeryproject.ui.datepicker;

import android.os.Bundle;
import android.util.Pair;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;

public class DatePickerController {
    
    private static FragmentActivity mActivity;
    
    private static DatePickerController mDatePickerController;
    
    private static SublimePickerFragment.Callback mFragmentCallback;
    
    private static SublimePickerFragment mSublimePickerFragment;
    
    private DatePickerController() {
    }
    
    public synchronized static DatePickerController getInstance(FragmentActivity activity,
                                                   SublimePickerFragment.Callback fragmentCallback) {
        if (mDatePickerController == null) {
            mDatePickerController = new DatePickerController();
        }
    
        mActivity = activity;
        mFragmentCallback = fragmentCallback;
        
        return mDatePickerController;
    }
    
    public void showPicker() {
        mSublimePickerFragment = initPickerFragment(false);
        mSublimePickerFragment.show(
                mActivity.getSupportFragmentManager(), "DATE_PICKER");
    }
    
    public void showRangeSelectPicker() {
        mSublimePickerFragment = initPickerFragment(true);
        mSublimePickerFragment.show(
                mActivity.getSupportFragmentManager(), "DATE_PICKER");
    }
    
    // Initialize date range picker.
    private SublimePickerFragment initPickerFragment(boolean isRangeSelect) {
        SublimePickerFragment pickerFragment = new SublimePickerFragment();
        pickerFragment.setCallback(mFragmentCallback);
        
        Pair<Boolean, SublimeOptions> optionsPair = getSetupOptions(isRangeSelect);
        
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
        pickerFragment.setArguments(bundle);
        
        pickerFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        
        return pickerFragment;
    }
    
    private Pair<Boolean, SublimeOptions> getSetupOptions(boolean isRangeSelect) {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;
        
        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        options.setDisplayOptions(displayOptions);
        options.setCanPickDateRange(isRangeSelect);
        
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }
}
