package com.example.sleepdiary.smartwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleepdiary.BuildConfig;
import com.example.sleepdiary.MainActivity;
import com.example.sleepdiary.PatientIdSingleton;
import com.example.sleepdiary.R;
import com.example.sleepdiary.Utilities;
import com.example.sleepdiary.diary.SleepDiary;
import com.example.sleepdiary.reminders.ReminderDiaryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class SmartwatchActivity extends AppCompatActivity {

    private TextView lastImportTV, successTV;
    private final String TAG = "sgw4files";
    ArrayList<SleepStage> stagesList = new ArrayList<SleepStage>();
    ArrayList<Saturation> saturationList = new ArrayList<Saturation>();
    File sleep_stages = null;
    File oxygen_saturation = null;
    String patId;
    Timestamp compareTimestamp;
    Timestamp lastEntry;
    private FirebaseFirestore db;
    ViewPager mViewPager;
    int[] images = {R.drawable.instruc, R.drawable.instruc2, R.drawable.instruc3};
    ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartwatch);
        lastImportTV = (TextView) findViewById(R.id.lastImportTV);
        successTV = (TextView) findViewById(R.id.successTV);
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);
        mViewPagerAdapter = new ViewPagerAdapter(SmartwatchActivity.this, images);
        mViewPager.setAdapter(mViewPagerAdapter);

        PatientIdSingleton patientIdSingleton = com.example.sleepdiary.PatientIdSingleton.getInstance();
        patId = patientIdSingleton.getId();
        db = FirebaseFirestore.getInstance();
        getLastEntry();

//        this will be commented out
        Calendar myCalendar = new GregorianCalendar(2023, 3, 28);
        Date myDate = myCalendar.getTime();
        compareTimestamp = new Timestamp(myDate);
    }

    public void onImportClick(View view) {
        successTV.setText("Czekaj...");
        getFiles();
        readStagesFile();
        readSaturationFile();

        WriteBatch batch = db.batch();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        ArrayList<SleepStage> studyStages = new ArrayList<SleepStage>();
        Saturation studySaturation = new Saturation(0, 0, 0, 0, new Timestamp(new Date()));

        try {
            for (int i = 0; i < stagesList.size(); i++) {
                if (i != stagesList.size() - 1) {
                    if (stagesList.get(i).startDate.getSeconds() > compareTimestamp.getSeconds()) {
                        long diff = stagesList.get(i + 1).startDate.getSeconds()-(stagesList).get(i).endDate.getSeconds();
                        if (diff <= 3600) {
                            if(studyStages.size() == 0) {
                                studySaturation = Utilities.findSaturationRecord(saturationList, stagesList.get(i).startDate);
                            }
                            studyStages.add(stagesList.get(i));
                        } else {
                            String id = UUID.randomUUID().toString();
                            String entryDateString = Utilities.parseComparisionDate(studyStages.get(studyStages.size()-1).endDate);
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            Date entryDate = formatter.parse(entryDateString);

                            Log.d(TAG, entryDate.toString());

                            SmartwatchStudy smartwatchStudy = new SmartwatchStudy(id, patId,
                                    email, studyStages, studySaturation.max, studySaturation.min,
                                    studySaturation.mean, studySaturation.desaturationTime,
                                    new Timestamp(entryDate));

                            DocumentReference nycRef = db.collection("smartwatch").document(id);
                            batch.set(nycRef, smartwatchStudy);

                            studySaturation = new Saturation(0, 0, 0, 0, new Timestamp(new Date()));
                            studyStages.clear();
                        }
                    }
                    else {
                        successTV.setText("Wszystkie badania są już w bazie danych");
                        break;
                    }
                } else {
                    studyStages.add(stagesList.get(i));

                    String id = UUID.randomUUID().toString();
                    SmartwatchStudy smartwatchStudy = new SmartwatchStudy(id, patId,
                            email, studyStages, studySaturation.max, studySaturation.min,
                            studySaturation.mean, studySaturation.desaturationTime, studyStages.get(studyStages.size()-1).endDate);

                    DocumentReference nycRef = db.collection("smartwatch").document(id);
                    batch.set(nycRef, smartwatchStudy);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                successTV.setText("Zaimportowano dane!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successTV.setText("Wystąpił błąd");
            }
        });
    }

    private void getFiles(){
        try {
            String[] requiredPermissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
            ActivityCompat.requestPermissions(this, requiredPermissions, 0);

            String pathSH = Environment.getExternalStorageDirectory() + "/Download/Samsung Health";
            File shFolder = new File(pathSH);
            File[] filesSH = shFolder.listFiles();
            String pathData = filesSH[filesSH.length-1].toString();
            File dataFolder = new File(pathData);
            File[] filesData = dataFolder.listFiles();

            for(File file: filesData){
                if(file.toString().contains("sleep_stage")){
                    sleep_stages = file;
                }
                if(file.toString().contains("oxygen_saturation")){
                    oxygen_saturation = file;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Nie znaleziono plików", Toast.LENGTH_LONG).show();
        }
    }

    private void readStagesFile(){
        try {
            File stages_csv = new File(sleep_stages.toString());
            FileReader filereader = new FileReader(stages_csv);
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(2)
                    .build();
            List<String[]> allData = csvReader.readAll();
            for(String[] data: allData){
                SleepStage newStage = new SleepStage(new Timestamp(Utilities.parseSamsungDate(data[0])),
                        new Timestamp(Utilities.parseSamsungDate(data[9])), data[5],
                        Utilities.getStageDuration(data[0], data[9]));
                stagesList.add(newStage);
            }

            stagesList.sort(Comparator.comparing(o -> o.startDate));

        } catch (Exception e) {
            Toast.makeText(this, "Nie znaleziono plików", Toast.LENGTH_LONG).show();
        }
    }

    private void readSaturationFile(){
        try {
            File saturation_csv = new File(oxygen_saturation.toString());
            FileReader filereader = new FileReader(saturation_csv);
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(2)
                    .build();
            List<String[]> allData = csvReader.readAll();
            for(String[] data: allData){
               Saturation saturation = new Saturation(Float.parseFloat(data[8]),
                       Float.parseFloat(data[9]), Float.parseFloat(data[10]),
                       Float.parseFloat(data[6]), new Timestamp(Utilities.parseSamsungDate(data[2])));
                saturationList.add(saturation);
            }

            saturationList.sort(Comparator.comparing(o -> o.entryDate));

        } catch (Exception e) {
            Toast.makeText(this, "Nie znaleziono plików", Toast.LENGTH_LONG).show();
        }
    }

    public void onOpenClick(View view) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.sec.android.app.shealth");
        if (launchIntent != null) {
            startActivity(launchIntent);
        } else {
            Toast.makeText(this, "Aplikacja Samsung Health nie jest zainstalowana na tym telefonie", Toast.LENGTH_LONG).show();
        }
    }

    private void getLastEntry(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        db.collection("smartwatch")
                .whereEqualTo("patientEmail", email)
                .orderBy("entryDate", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            SmartwatchStudy study = task.getResult().getDocuments().get(0).toObject(SmartwatchStudy.class);
                            lastEntry = study.entryDate;
                            try {
                                lastImportTV.setText("W bazie znajdują się dane do "
                                        + Utilities.parseComparisionDate(lastEntry));
                            } catch (ParseException e) {
                                lastImportTV.setText("W bazie znajdują się dane do");
                                e.printStackTrace();
                            }
                            ArrayList<SleepStage> studyStages = study.getSleepStages();
                            compareTimestamp = studyStages.get(studyStages.size()-1).endDate;
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}