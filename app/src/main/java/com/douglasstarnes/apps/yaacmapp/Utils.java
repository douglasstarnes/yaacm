package com.douglasstarnes.apps.yaacmapp;

import java.util.Calendar;

/**
 * Created by douglasstarnes on 8/31/16.
 */
public class Utils {
    public static boolean validateDateValues(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false); // sanity check
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        try {
            calendar.getTime();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
