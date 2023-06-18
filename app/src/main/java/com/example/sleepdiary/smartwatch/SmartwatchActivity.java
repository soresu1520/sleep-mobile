package com.example.sleepdiary.smartwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleepdiary.BuildConfig;
import com.example.sleepdiary.R;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SmartwatchActivity extends AppCompatActivity {

    private TextView testTV;
    private final String TAG = "sgw4files";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartwatch);
        testTV = (TextView) findViewById(R.id.testTV);

        String[] requiredPermissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
        ActivityCompat.requestPermissions(this, requiredPermissions, 0);


//        String pathSH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
//                "/Samsung Health";
        String pathSH = Environment.getExternalStorageDirectory() + "/Download/Samsung Health";
        File shFolder = new File(pathSH);
        testTV.setText(Environment.getExternalStorageDirectory().toString());
        File[] filesSH = shFolder.listFiles();
        String pathData = filesSH[filesSH.length-1].toString();
        File dataFolder = new File(pathData);
        File[] filesData = dataFolder.listFiles();
        File sleep_stages = null;
        File oxygen_saturation = null;

        for(File file: filesData){
            if(file.toString().contains("sleep_stage")){
                sleep_stages = file;
            }
            if(file.toString().contains("oxygen_saturation")){
                oxygen_saturation = file;
            }
        }

        testTV.setText(sleep_stages.toString());

        try {
        File stages_csv = new File(sleep_stages.toString());
        CSVReader reader = new CSVReader(new FileReader(stages_csv.getAbsolutePath()));

        FileReader filereader = new FileReader(stages_csv);
        CSVReader csvReader2 = new CSVReaderBuilder(filereader)
                .withSkipLines(2)
                .build();
        List<String[]> allData = csvReader2.readAll();
//        Log.d(TAG, allData.get(0)[0]);

            ArrayList<SleepStage> stagesList = new ArrayList<SleepStage>();

            for(String[] data: allData){
                SleepStage newStage = new SleepStage(data[0], data[9], data[5]);
                stagesList.add(newStage);
            }

            stagesList.sort(Comparator.comparing(o -> o.startDate));

            Log.d(TAG, stagesList.get(0).startDate);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Nie znaleziono plików", Toast.LENGTH_SHORT).show();
        }
    }
}