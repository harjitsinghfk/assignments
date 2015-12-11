package com.flipkart.todo;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateItem extends Fragment {

    EditText titleEditText ,descriptionEditText, dateEditText,timeEditText;
    Spinner spinner;
    Button updateButton,deleteButton;
    Calendar c = Calendar.getInstance();
    Cursor cursor;
    String title,descripton,date,time,priority;
    private static final String TITLE_KEY = "title";
    private static final String DESCRIPTION_KEY = "description";
    private static final String DATE_KEY = "date";
    private static final String TIME_KEY = "time";
    private static final String PRIORITY_KEY = "priority";

    public UpdateItem(){};
    public UpdateItem(String title) {
        this.title = title;
        this.descripton = descripton;
        this.date = date;
        this.time = time;
        this.priority = priority;

    }
    public static UpdateItem newInstance(String title, String description , String date, String time, String priority){
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEY, title);
        bundle.putString(DESCRIPTION_KEY,description);
        bundle.putString(DATE_KEY,date);
        bundle.putString(TIME_KEY,time);
        bundle.putString(PRIORITY_KEY,priority);

        UpdateItem updateItem = new UpdateItem();
        updateItem.setArguments(bundle);
        return updateItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_update_item, container, false);
        title = getArguments().getString(TITLE_KEY);
        descripton = getArguments().getString(DESCRIPTION_KEY);
        date = getArguments().getString(DATE_KEY);
        time = getArguments().getString(TIME_KEY);
        priority = getArguments().getString(PRIORITY_KEY);

        titleEditText = (EditText) view.findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);
        dateEditText = (EditText) view.findViewById(R.id.dateEditText);
        timeEditText = (EditText) view.findViewById(R.id.timeEditText);
        spinner = (Spinner) view.findViewById(R.id.spinner);


        titleEditText.setText(title);

        descriptionEditText.setText(descripton);
        dateEditText.setText(date);
        timeEditText.setText(time);
        spinner.setSelection(Integer.parseInt(priority.substring(1, 2)));

        updateButton = (Button) view.findViewById(R.id.updateButton);
        deleteButton = (Button) view.findViewById(R.id.deleteButton);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setEditText(dateEditText);
                datePickerFragment.show(getFragmentManager(), "datepicker");
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment dialogFragment = new TimePickerFragment();
                dialogFragment.passEditText(timeEditText);
                dialogFragment.show(getFragmentManager(),"timepicker");

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(titleEditText.getText().toString().equals("")) {
                    Toast.makeText(getContext(),
                            "Title can't be null!", Toast.LENGTH_SHORT).show();
                }
                else {
                    SQLDatabaseHelper sqlDatabaseHelper = new SQLDatabaseHelper(getContext());
                    SQLiteDatabase db = sqlDatabaseHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(sqlDatabaseHelper.COLUMN_NAME_TITLE, titleEditText.getText().toString());
                    values.put(sqlDatabaseHelper.COLUMN_NAME_DESCRIPTION, descriptionEditText.getText().toString());
                    values.put(sqlDatabaseHelper.COLUMN_NAME_DATE, String.valueOf(dateEditText.getText()));
                    values.put(sqlDatabaseHelper.COLUMN_NAME_MONTH, String.valueOf(dateEditText.getText()));
                    values.put(sqlDatabaseHelper.COLUMN_NAME_YEAR, String.valueOf(dateEditText.getText()));
                    values.put(sqlDatabaseHelper.COLUMN_NAME_TIME, timeEditText.getText().toString());
                    values.put(sqlDatabaseHelper.COLUMN_NAME_PRIORITY, spinner.getSelectedItem().toString());
                    values.put(sqlDatabaseHelper.COLUMN_NAME_COMPLETED, "false");
                    values.put(sqlDatabaseHelper.COLUMN_NAME_RECYCLEBIN, "false");

                    SQLDatabaseHelper.updateItem(getContext(), values, title);
                    FragmentManager manager = getFragmentManager();
                    manager.popBackStack();
                }

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLDatabaseHelper.moveItemToRecyclebin(getContext(), title);
                FragmentManager manager = getFragmentManager();
                manager.popBackStack();
            }
        });

        return view;
    }

}
