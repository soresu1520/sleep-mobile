package com.example.sleepdiary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utilities {

    public static int getSleepTimeInMinutes(String startTime, String endTime, String addTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        Date startDate = simpleDateFormat.parse(startTime);
        Date endDate = simpleDateFormat.parse(endTime);

        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.MINUTE, Integer.parseInt(addTime));
        startDate = c.getTime();

        int sleepMinutes = getDifferenceInMinutes(startDate, endDate);

        return sleepMinutes;
    }

    public static int getBedTimeInMinutes(String startTime, String endTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date startDate = simpleDateFormat.parse(startTime);
        Date endDate = simpleDateFormat.parse(endTime);

        int bedMinutes = getDifferenceInMinutes(startDate, endDate);

        return bedMinutes;
    }

    private static int getDifferenceInMinutes(Date startTime, Date endTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        long difference = endTime.getTime() - startTime.getTime();

        if(difference<0) {
            Date dateMax = simpleDateFormat.parse("24:00");
            Date dateMin = simpleDateFormat.parse("00:00");
            difference=(dateMax.getTime() -startTime.getTime() )+(endTime.getTime()-dateMin.getTime());
        }

        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24* days) - (1000*60*60*hours)) / (1000*60);

        int differenceInMinutes = hours * 60 + min;

        return differenceInMinutes;
    }


}
