package com.douglasstarnes.apps.yaacmapp;

import java.util.Calendar;
import java.util.InputMismatchException;

public class Utils {
    // given values for a day, month and year, return if they represent a valid date
    public static boolean validateDateValues(String rawDay, String rawMonth, String rawYear) {
        try {
            int day = Integer.parseInt(rawDay);
            int month = Integer.parseInt(rawMonth) - 1;
            int year = Integer.parseInt(rawYear);

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
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
