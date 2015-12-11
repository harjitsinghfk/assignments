package com.flipkart.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by harjit.singh on 27/11/15.
 */
public class SQLDatabaseHelper extends SQLiteOpenHelper {


    public static final String TABLE_NAME = "todo";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_MONTH = "month";
    public static final String COLUMN_NAME_YEAR = "year";
    public static final String COLUMN_NAME_TIME = "time";
    public static final String COLUMN_NAME_PRIORITY = "priority";
    public static final String COLUMN_NAME_COMPLETED = "completed";
    public static final String COLUMN_NAME_RECYCLEBIN = "recyclebin";


    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " DATETIME";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TODO =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_DATE + DATE_TYPE + COMMA_SEP +
                    COLUMN_NAME_MONTH + INT_TYPE + COMMA_SEP +
                    COLUMN_NAME_YEAR + INT_TYPE + COMMA_SEP +
                    COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_PRIORITY + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_COMPLETED + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_RECYCLEBIN + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "todo.db";


    public SQLDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TODO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

    }


    public static void moveItemToRecyclebin(Context context, String title) {
        SQLDatabaseHelper sqlDatabaseHelper = new SQLDatabaseHelper(context);
        SQLiteDatabase db = sqlDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(sqlDatabaseHelper.COLUMN_NAME_RECYCLEBIN, "true");

        db.update(
                sqlDatabaseHelper.TABLE_NAME,
                values,
                sqlDatabaseHelper.COLUMN_NAME_TITLE + "=\"" + title + "\"",
                null);
    }

    public static void updateItem(Context context, ContentValues values, String title) {
        SQLDatabaseHelper sqlDatabaseHelper = new SQLDatabaseHelper(context);
        SQLiteDatabase db = sqlDatabaseHelper.getWritableDatabase();
        db.update(
                sqlDatabaseHelper.TABLE_NAME,
                values,
                sqlDatabaseHelper.COLUMN_NAME_TITLE + "=\"" + title + "\"",
                null);
    }

}
