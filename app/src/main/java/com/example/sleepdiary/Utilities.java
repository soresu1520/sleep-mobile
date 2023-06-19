package com.example.sleepdiary;

import android.util.Log;

import com.example.sleepdiary.smartwatch.Saturation;
import com.google.firebase.Timestamp;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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

    public static int getStageDuration(String startDate, String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime sDate = LocalDateTime.parse(startDate, formatter);
        LocalDateTime eDate = LocalDateTime.parse(endDate, formatter);
        Duration dur = Duration.between(sDate, eDate);
        return (int) dur.toMinutes();
    }

    public static Date parseSamsungDate(String date) throws ParseException {
        Date parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        return calendar.getTime();
    }

    public static String parseComparisionDate(Timestamp date) throws ParseException {
        SimpleDateFormat DateFormat = new SimpleDateFormat("dd-MM-YYYY");
        String parsedDate = DateFormat.format(date.toDate());
        return parsedDate;
    }

    public static Saturation findSaturationRecord(ArrayList<Saturation> list, Timestamp date) throws ParseException {
       for(Saturation item : list){
           if(parseComparisionDate(item.getEntryDate()).equals(parseComparisionDate(date))){
               return item;
           }
       }
       return new Saturation(0,0,0,0,new Timestamp(new Date()));
    }

    public static String generateUUID(){
        UUID uuid = UUID.randomUUID();
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16])
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());

        String encodedUUID = Base64.getUrlEncoder().withoutPadding().encodeToString(byteBuffer.array());
        return encodedUUID;
    }

}
