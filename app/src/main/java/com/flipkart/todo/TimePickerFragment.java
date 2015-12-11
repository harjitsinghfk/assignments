package com.flipkart.todo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by harjit.singh on 25/11/15.
 */
public class TimePickerFragment extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    EditText timeEditText;

    public TimePickerFragment() {

    }

    public void passEditText(EditText dateEditText) {
        timeEditText = dateEditText;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new android.app.TimePickerDialog(getActivity(),this,hour,minute, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String AM_PM = "PM";
        if(hourOfDay <12)
            AM_PM = "AM";
        else
            hourOfDay = hourOfDay -12;
            timeEditText.setText(Common.checkDigit(hourOfDay)+":"+Common.checkDigit(minute)+" "+AM_PM);

    }
}
