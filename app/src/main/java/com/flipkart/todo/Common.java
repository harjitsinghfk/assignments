package com.flipkart.todo;

import java.util.Calendar;

/**
 * Created by harjit.singh on 11/12/15.
 */
public class Common {
    public static String checkDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }

    public static int getCurrentDateInt() {
        Calendar cal = Calendar.getInstance();
        return Integer.valueOf(Common.checkDigit(cal.get(Calendar.YEAR))+Common.checkDigit(cal.get(Calendar.MONTH))+Common.checkDigit(cal.get(Calendar.DATE)));
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) +
                "/" + Common.checkDigit(cal.get(Calendar.MONTH)) +
                "/" + Common.checkDigit(cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String getCurrentTime() {
        String currentTime = "PM";
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.AM_PM) == Calendar.AM)
            currentTime = "AM";
        return Common.checkDigit(cal.get(Calendar.HOUR)) + ":" + Common.checkDigit(cal.get(Calendar.MINUTE)) + " " + currentTime;
    }
}

