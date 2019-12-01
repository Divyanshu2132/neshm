package com.androhome.neshm.model;

import java.util.Calendar;

public class DateModel {

    public int getPresentDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public int getPresentMonth() {
        Calendar c = Calendar.getInstance();
        return (c.get(Calendar.MONTH) + 1);
    }

    public int getPresentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public int getPresentMinute() {
        final Calendar c = Calendar.getInstance();
        return c.get(Calendar.MINUTE);
    }

    public int getPresentHour() {
        final Calendar c = Calendar.getInstance();
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public long getPresentTimestamp() {
        long s = System.currentTimeMillis();
        String a = getPresentDay() + "/" + getPresentMonth() + "/" + getPresentYear();
        return s;
    }

}
