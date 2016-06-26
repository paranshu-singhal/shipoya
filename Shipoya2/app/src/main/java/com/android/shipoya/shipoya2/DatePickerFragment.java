package com.android.shipoya.shipoya2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    giveDate mCallback;
    private static final String logTag = "logTag";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //Log.d(logTag, String.format("%d, %d, %d", year, monthOfYear, dayOfMonth));
        mCallback.onGiveDate(year, monthOfYear, dayOfMonth);

    }

    public interface giveDate{
        void onGiveDate(int year, int monthOfYear, int dayOfMonth);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (giveDate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
