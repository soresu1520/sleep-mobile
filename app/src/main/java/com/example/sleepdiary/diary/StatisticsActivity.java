package com.example.sleepdiary.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.sleepdiary.PatientIdSingleton;
import com.example.sleepdiary.R;
import com.example.sleepdiary.Utilities;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay;
    private TextView q, a1tv, a6tv, a2tv, a3tv, a4tv, a5tv;
    private ArrayList<SleepDiary> sleeps = new ArrayList<>();
    private String patId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        PatientIdSingleton patientIdSingleton = com.example.sleepdiary.PatientIdSingleton.getInstance();
        patId = patientIdSingleton.getId();

        Log.d("sleeps4", String.valueOf(sleeps.size()));

        q = (TextView) findViewById(R.id.q);
        a6tv = (TextView) findViewById(R.id.a6tv);
        a2tv = (TextView) findViewById(R.id.a2tv);
        a3tv = (TextView) findViewById(R.id.a3tv);
        a4tv = (TextView) findViewById(R.id.a4tv);
        a5tv = (TextView) findViewById(R.id.a5tv);
        a1tv = (TextView) findViewById(R.id.a1tv);

        a2tv.setText("");
        a3tv.setText("");
        a4tv.setText("");
        a5tv.setText("");
        a6tv.setText("");
        a1tv.setText("");

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        q.setText(date.format(formatter));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String  email = user.getEmail();

        db.collection("sleepdiary").whereEqualTo("patientId", patId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("sleeps", "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            List<SleepDiary> types = documentSnapshots.toObjects(SleepDiary.class);
                            //Log.d("sleeps", "onSuccess: " + sleeps);
                            sleeps.addAll(types);
//                            for(int i = 0; i < sleeps.size();  i++){
//                                Log.d("sleepsFirebase", sleeps.get(i).getDateEntry());
//                            }
                        }
                    }

                });
    }

    public void onQ2(View view) {
        q = (TextView) findViewById(R.id.q);
        showDatePicker(q);

    }


    public void onStatsClick2(View view) throws ParseException {

        q = (TextView) findViewById(R.id.q);
        a6tv = (TextView) findViewById(R.id.a6tv);
        a2tv = (TextView) findViewById(R.id.a2tv);
        a3tv = (TextView) findViewById(R.id.a3tv);
        a4tv = (TextView) findViewById(R.id.a4tv);
        a5tv = (TextView) findViewById(R.id.a5tv);
        a1tv = (TextView) findViewById(R.id.a1tv);

        int fallAsleep=0;
        double sleepMinutes=0;
        double bedMinutes=1;
        long sleepHour, bedHour = 0;
        long sleepMinute, bedMinute = 0;
        double percent=0;
        int quality=0;
        String qualityDesc="";

        DecimalFormat df = new DecimalFormat("0.00");

        Collections.sort(sleeps, new Comparator() {
            @Override
            public int compare(Object sleep1, Object sleep2) {
                //use instanceof to verify the references are indeed of the type in question
                return ((SleepDiary)sleep2).getTimestamp()
                        .compareTo(((SleepDiary)sleep1).getTimestamp());
            }
        });

//        Log.d("sleeps", String.valueOf(sleeps.size()));

//        for(int i = 0; i < sleeps.size();  i++){
//            Log.d("sleeps3", sleeps.get(i).getDateEntry());
//        }

        boolean find = false;
        boolean find2 = false;
        int i=0;
        int i2=0;
        int findIndex=0;
        ArrayList<SleepDiary> sleepsFound = new ArrayList<>();

        while(find==false && i<sleeps.size()){
            if (sleeps.get(i).getEntryDate().equals(q.getText())){
                find = true;
                findIndex=i;
            }
            i++;
        }
        Log.d("sleeps3", String.valueOf(findIndex));
        Log.d("sleeps3", String.valueOf(find));

        if(find==false){
            a1tv.setText("Nie ma wpisu z tą datą");
            a2tv.setText("");
            a3tv.setText("");
            a4tv.setText("");
            a5tv.setText("");
            a6tv.setText("");
        }

        i--;

        while((i+i2)<sleeps.size() && i2<7){
            sleepsFound.add(sleeps.get(i+i2));
            i2++;
            if(i2==7)
                find2=true;
        }

        for(int j = 0; j < sleepsFound.size();  j++){
            Log.d("sleeps3", sleeps.get(j).getEntryDate());
        }

        Log.d("sleeps3", String.valueOf(find2));
        Log.d("sleeps3", String.valueOf(i2));
        Log.d("sleeps3", String.valueOf(sleepsFound.size()));

        if(!find2 && find){
            a1tv.setText("Nie ma wpisów z 7 dni");
            a2tv.setText("");
            a3tv.setText("");
            a4tv.setText("");
            a5tv.setText("");
            a6tv.setText("");
        }

        if(find2 && find) {
            a1tv.setText("");

            for (int j = 0; j < sleepsFound.size(); j++) {
                fallAsleep = fallAsleep + Integer.valueOf(sleepsFound.get(j).getQ4());

                String startDateSleep = sleepsFound.get(j).getQ3();
                String endDateSleep = sleepsFound.get(j).getQ5();
                String addMinutesSleep = sleepsFound.get(j).getQ4();

                sleepMinutes = sleepMinutes + Utilities.getSleepTimeInMinutes(startDateSleep, endDateSleep, addMinutesSleep);

                String startDateBed = sleepsFound.get(j).getQ2();
                String endDateBed = sleepsFound.get(j).getQ7();

                bedMinutes = bedMinutes + Utilities.getBedTimeInMinutes(startDateBed, endDateBed);

//                Log.d("testSleep", "date: " + sleepsFound.get(j).getDateEntry() +
//                        " sleepMinutes: " + Utilities.getSleepTimeInMinutes(startDateSleep, endDateSleep, addMinutesSleep)
//                + " bedMinutes: " + Utilities.getBedTimeInMinutes(startDateBed, endDateBed));

                if (sleepsFound.get(j).getQ8().equals("Bardzo zła")) {
                    quality = quality + 1;
                } else if (sleepsFound.get(j).getQ8().equals("Zła")) {
                    quality = quality + 2;
                } else if (sleepsFound.get(j).getQ8().equals("Przeciętna")) {
                    quality = quality + 3;
                } else if (sleepsFound.get(j).getQ8().equals("Dobra")) {
                    quality = quality + 4;
                } else if (sleepsFound.get(j).getQ8().equals("Bardzo dobra")) {
                    quality = quality + 5;
                }

            }
            fallAsleep = fallAsleep / 7;
            quality = quality / 7;
            sleepMinutes = sleepMinutes / 7;
            sleepHour = (int) (sleepMinutes / (60));
            sleepMinute = (int) (sleepMinutes - sleepHour * 60);
            bedMinutes = bedMinutes / 7;
            bedHour = (int) (bedMinutes / (60));
            bedMinute = (int) (bedMinutes - bedHour * 60);
            percent = sleepMinutes / bedMinutes * 100;

            //Log.d("sleepTest", sleepMinutes*7 + " " + bedMinutes*7 + " " + percent);

            if (quality == 1) {
                qualityDesc = "Bardzo zła";
            } else if (quality == 2) {
                qualityDesc = "Zła";
            } else if (quality == 3) {
                qualityDesc = "Przeciętna";
            } else if (quality == 4) {
                qualityDesc = "Dobra";
            } else if (quality == 5) {
                qualityDesc = "Bardzo dobra";
            }

            a2tv.setText(fallAsleep + " minut");
            a3tv.setText(qualityDesc);
            a4tv.setText(sleepHour + " godzin " +
                    sleepMinute + " minut");
            a5tv.setText(bedHour + " godzin " +
                    bedMinute + " minut");
            a6tv.setText(df.format(percent) + "%");

        }

    }

    private void showDatePicker(TextView q) {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        monthOfYear = monthOfYear+1;
                        q.setText(LocalDate.of(year, monthOfYear, dayOfMonth).format(formatter));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}