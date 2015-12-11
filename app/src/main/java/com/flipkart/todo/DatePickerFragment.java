package com.flipkart.todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;


/**
 * Created by harjit.singh on 25/11/15.
 */
public class DatePickerFragment extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {


    EditText dateEditText1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(c.YEAR);
        int month = c.get(c.MONTH);
        int day = c.get(c.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, Integer.valueOf(Common.checkDigit(month)), Integer.valueOf(Common.checkDigit(day)));
    }
    public void setEditText(EditText dateEditText) {
        dateEditText1 = dateEditText;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateEditText1.setText(year+"/"+Common.checkDigit(monthOfYear)+"/"+Common.checkDigit(dayOfMonth));
    }

}
