package com.flipkart.todo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by harjit.singh on 27/11/15.
 */
public class TodoCursorAdapter extends CursorAdapter {

    private static final int STATE_UNKNOWN = 0;

    private static final int STATE_SECTIONED_CELL = 1;

    private static final int STATE_REGULAR_CELL = 2;

    private int[] mCellStates;

    private int cursorPrevDate;

    private String cursorPrevPriority = "P0";

    String sortBy = "date";

    private final int currentDate = Common.getCurrentDateInt();

    private static class ViewHolder {
        TextView dayTextView;
        TextView titleTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView priorityTextView;
        String day;
    }

    void setSortBy(String sortbyOption) {
        sortBy = sortbyOption;
    }


    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        mCellStates = cursor == null ? null : new int[cursor.getCount()];
    }

    public TodoCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mCellStates = c == null ? null : new int[c.getCount()];
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.dayTextView = (TextView) view.findViewById(R.id.dayTextView);
        holder.titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        holder.dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        holder.timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        holder.priorityTextView = (TextView) view.findViewById(R.id.priorityTextView);

        view.setTag(holder);

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final ViewHolder holder = (ViewHolder) view.getTag();

        boolean needSeparator = false;

        final int position = cursor.getPosition();

        holder.day = cursor.getString(cursor.getColumnIndexOrThrow("date")).replace("/", "");

        if (sortBy.equals("title"))
            holder.dayTextView.setVisibility(View.GONE);
        else {
            switch (mCellStates[position]) {
                case STATE_SECTIONED_CELL:
                    needSeparator = true;
                    break;

                case STATE_REGULAR_CELL:
                    needSeparator = false;
                    break;

                case STATE_UNKNOWN:
                default:

                    if (position == 0) {
                        needSeparator = true;
                    } else {
                        cursor.moveToPosition(position - 1);
                        if (sortBy.equals("priority"))
                            cursorPrevPriority = cursor.getString(cursor.getColumnIndexOrThrow("priority"));
                        else
                            cursorPrevDate = Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("date")).replace("/", ""));
                        cursor.moveToPosition(position);

                        if (sortBy.equals("date") && cursorPrevDate != Integer.valueOf(holder.day))
                            needSeparator = true;
                        else if (sortBy.equals("priority") && !cursorPrevPriority.equals(cursor.getString(cursor.getColumnIndexOrThrow("priority"))))
                            needSeparator = true;

                    }

                    // Cache the result
                    mCellStates[position] = needSeparator ? STATE_SECTIONED_CELL : STATE_REGULAR_CELL;
                    break;
            }

            if (needSeparator && sortBy.equals("date")) {
                String text = getDayName(Integer.valueOf(holder.day), currentDate);
                if (text == null) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, cursorPrevDate - currentDate + 1);
                    text = String.valueOf(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
                }
                holder.dayTextView.setText(text);
                holder.dayTextView.setVisibility(View.VISIBLE);
            } else if (needSeparator && sortBy.equals("priority")) {
                holder.dayTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("priority")));
                holder.dayTextView.setVisibility(View.VISIBLE);
                holder.dateTextView.setVisibility(View.GONE);
            } else {
                holder.dayTextView.setVisibility(View.GONE);
            }
        }

        holder.titleTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        holder.dateTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        holder.timeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("time")));
        holder.priorityTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("priority")));
        if (cursor.getString(cursor.getColumnIndexOrThrow("completed")).equals("true"))
            holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
    }

    String getDayName(int cursorDate, int currentDate) {
        if (cursorDate == currentDate)
            return "Today";
        else if (cursorDate == currentDate + 1)
            return "Tomorrow";
        else
            return null;
    }
}
