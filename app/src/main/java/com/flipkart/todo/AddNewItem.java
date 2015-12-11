package com.flipkart.todo;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewItem extends Fragment {

    EditText titleEditText, descriptionEditText, dateEditText, timeEditText;
    ImageButton titleVoiceImageButton,descriptionVoiceImageButton;
    Button saveButton, cancelButton;
    protected static final int REQUEST_OK = 1;
    protected static final int REQUEST_OK1 = 2;


    public AddNewItem() {
        // Required empty public constructor
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_OK && resultCode==Activity.RESULT_OK) {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String title  = Character.toUpperCase(thingsYouSaid.get(0).charAt(0)) + thingsYouSaid.get(0).substring(1);
            ((TextView)getView().findViewById(R.id.titleEditText)).setText(title);
        }
        if (requestCode==REQUEST_OK1 && resultCode==Activity.RESULT_OK) {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String description  = Character.toUpperCase(thingsYouSaid.get(0).charAt(0)) + thingsYouSaid.get(0).substring(1);
            ((TextView)getView().findViewById(R.id.descriptionEditText)).setText(description);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_new_item, container, false);


        titleEditText = (EditText) view.findViewById(R.id.titleEditText);

        titleVoiceImageButton = (ImageButton) view.findViewById(R.id.titleVoiceImageButton);
        titleVoiceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try {
                    startActivityForResult(i, REQUEST_OK);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });


        descriptionVoiceImageButton = (ImageButton) view.findViewById(R.id.descriptionVoiceImageButton);
        descriptionVoiceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try {
                    startActivityForResult(i, REQUEST_OK1);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
                }
            }
        });

        dateEditText = (EditText) view.findViewById(R.id.dateEditText);

        dateEditText.setText(Common.getCurrentDate());

        timeEditText = (EditText) view.findViewById(R.id.timeEditText);
        timeEditText.setText(Common.getCurrentTime());
        saveButton = (Button) view.findViewById(R.id.saveButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if(inputMethodManager.isAcceptingText())
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setEditText(dateEditText);
                datePickerFragment.show(getFragmentManager(), "datepicker");
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if(inputMethodManager.isAcceptingText())
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);


                TimePickerFragment dialogFragment = new TimePickerFragment();
                dialogFragment.passEditText(timeEditText);
                dialogFragment.show(getFragmentManager(), "timepicker");

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleEditText = (EditText) view.findViewById(R.id.titleEditText);
                if (titleEditText.getText().toString().equals("")) {
                    Toast.makeText(getContext(),
                            "Title can't be null!", Toast.LENGTH_SHORT).show();
                } else {

                    descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);
                    Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
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

                    long newRowId;
                    newRowId = db.insert(
                            sqlDatabaseHelper.TABLE_NAME,
                            null,
                            values);
                    FragmentManager manager = getFragmentManager();
                    manager.popBackStack();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                manager.popBackStack();
            }
        });

        return view;
    }

}
