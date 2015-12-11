package com.flipkart.todo;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Homepage extends Fragment {


    SQLiteDatabase db;

    ImageView addButton;

    String sortType="Due Date";

    public Homepage() {

    }

    public void setSortType(String sortOptionSeleted) {
        sortType = sortOptionSeleted;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View homepageView = inflater.inflate(R.layout.fragment_homepage, container, false);
        SQLDatabaseHelper handler = new SQLDatabaseHelper(getContext());
        db = handler.getWritableDatabase();
        String dbQuery = getDbQuery(sortType);
        final Cursor todoCursor = db.rawQuery(dbQuery, null);
        if(todoCursor.getCount() == 0) {
            firstTimeLaunch(homepageView);

        }

        final ListView listView = (ListView) homepageView.findViewById(R.id.listView);
        final TodoCursorAdapter todoAdapter = new TodoCursorAdapter(getContext(), todoCursor,1);
        todoAdapter.setSortBy(sortType);
        listView.setAdapter(todoAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) todoAdapter.getItem(position);
                switchToUpdateItemFragment(item);

            }
        });

//        registerForContextMenu(listView);

        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        getContext());
                alert.setTitle("Alert");
                alert.setMessage("Mark it done or deleted?");
                alert.setPositiveButton("DONE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor item = (Cursor) todoAdapter.getItem(position);
                        SQLDatabaseHelper sqlDatabaseHelper = new SQLDatabaseHelper(getContext());
                        SQLiteDatabase db = sqlDatabaseHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(sqlDatabaseHelper.COLUMN_NAME_COMPLETED, "true");

                        SQLDatabaseHelper.updateItem(getContext(), values,  item.getString(item.getColumnIndexOrThrow("title")));
                        final Cursor todoCursor1=db.rawQuery("SELECT  rowid _id,* FROM todo where recyclebin=\"false\" ORDER BY date", null);
                        if(todoCursor1.getCount() == 0) {
                            firstTimeLaunch(homepageView);
                        }
                        todoAdapter.changeCursor(todoCursor1);
                        todoAdapter.notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton("DELETED", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Cursor item = (Cursor) todoAdapter.getItem(position);
                        SQLDatabaseHelper.moveItemToRecyclebin(getContext(), item.getString(item.getColumnIndexOrThrow("title")));
                        dialog.dismiss();
                        final Cursor todoCursor1=db.rawQuery("SELECT  rowid _id,* FROM todo where recyclebin=\"false\" ORDER BY date", null);
                        if(todoCursor1.getCount() == 0) {
                            TextView firstTextView = (TextView) homepageView.findViewById(R.id.firstTextView);
                            firstTextView.setVisibility(homepageView.VISIBLE);
                        }
//                        todoAdapter.swapCursor(todoCursor1);
                        todoAdapter.changeCursor(todoCursor1);
                        todoAdapter.notifyDataSetChanged();
                    }
                });

                alert.show();

                return true;
            }
        } );


//        todoAdapter.changeCursor(new);

        addButton = (ImageView) homepageView.findViewById(R.id.plus_image_view);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToAddItemFragment();

            }
        });

        return homepageView;
    }

    void switchToAddItemFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.add_tem_fade_in, R.anim.add_tem_fade_out, R.anim.add_tem_fade_in, R.anim.add_tem_fade_out);
        AddNewItem newItem = new AddNewItem();
        fragmentTransaction.replace(R.id.main_layout, newItem, "NIF");
        fragmentTransaction.addToBackStack("NIF");
        fragmentTransaction.commit();

    }

    void switchToUpdateItemFragment(Cursor c) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.add_tem_fade_in, R.anim.add_tem_fade_out, R.anim.add_tem_fade_in, R.anim.add_tem_fade_out);
        UpdateItem updateItem = UpdateItem.newInstance(c.getString(c.getColumnIndex("title")),
                c.getString(c.getColumnIndex("description")), c.getString(c.getColumnIndex("date")),
                c.getString(c.getColumnIndex("time")),c.getString(c.getColumnIndexOrThrow("priority")));
//        UpdateItem updateItem = new UpdateItem(c.getString(c.getColumnIndex("title")));
        fragmentTransaction.replace(R.id.main_layout,updateItem,"UPI");
        fragmentTransaction.addToBackStack("NIF");
        fragmentTransaction.commit();
    }
    String getDbQuery(String sortType) {
        if(sortType.equals("title"))
            return "SELECT  rowid _id,* FROM todo where recyclebin=\"false\" ORDER BY title";
        else if(sortType.equals("priority"))
            return "SELECT  rowid _id,* FROM todo where recyclebin=\"false\" ORDER BY priority";
        else
            return "SELECT  rowid _id,* FROM todo where recyclebin=\"false\" ORDER BY date";
    }
    void firstTimeLaunch(View homepageView) {
        TextView firstTextView = (TextView) homepageView.findViewById(R.id.firstTextView);
        firstTextView.setVisibility(View.VISIBLE);
        ImageView todoImageView = (ImageView) homepageView.findViewById(R.id.todoImageView);
        todoImageView.setVisibility(View.VISIBLE);
        todoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Dude, Don't bother me, Instead press + button!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
