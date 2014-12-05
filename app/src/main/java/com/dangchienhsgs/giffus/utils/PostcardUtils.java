package com.dangchienhsgs.giffus.utils;

import com.dangchienhsgs.giffus.postcard.Postcard;

import java.util.Calendar;

/**
 * Created by dangchienbn on 28/11/2014.
 */
public class PostcardUtils {
    public static boolean checkPostcardTime(Postcard postcard) {
        Calendar calendar = Calendar.getInstance();
        return (compareDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                postcard.getYear(),
                postcard.getMonth(),
                postcard.getDay(),
                postcard.getHour(),
                postcard.getMinute()));

    }

    public static boolean compareDate(int year1, int month1, int day1, int hour1, int minute1, int year2, int month2, int day2, int hour2, int minute2) {
        if (year1 != year2) {
            return year1 > year2;
        } else {
            if (month1 != month2) {
                return month1 > month2;
            } else {
                if (day1 != day2) {
                    return day1 > day2;
                } else {
                    if (hour1 != hour2) {
                        return hour1 > hour2;
                    } else {
                        return minute1 > minute2;
                    }
                }
            }
        }
    }


}
