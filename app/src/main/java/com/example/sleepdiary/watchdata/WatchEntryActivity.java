package com.example.sleepdiary.watchdata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sleepdiary.R;
import com.example.sleepdiary.diary.DiaryEntryActivity;
import com.example.sleepdiary.diary.DiaryListActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WatchEntryActivity extends AppCompatActivity {

    public String EXTRA="";
    private TextView tvStart, tvEnd, tvHeart;
    private FirebaseFirestore db;
    private String idEntry;
    private AlertDialog.Builder dialogBuilder, dialogBuilder3;
    private AlertDialog dialog, dialog3;
    private Button cancelDialog, cancelDialog3;

    private LineChart heartChart;
    private LineData heartData;
    private LineDataSet heartDataSet;
    private ArrayList heartEntriesArrayList;

    private LineChart accChart;
    private LineData accData;
    private LineDataSet accXDataSet, accYDataSet, accZDataSet;
    private ArrayList accXEntriesArrayList, accYEntriesArrayList, accZEntriesArrayList;
    private List<ILineDataSet> lines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_entry);

        Intent intent = getIntent();
        EXTRA = intent.getStringExtra("EXTRA");
        Log.d("sleepdiary3", EXTRA);

        heartChart = findViewById(R.id.heartChart);
        heartEntriesArrayList = new ArrayList<>();
        accChart = findViewById(R.id.accChart);
        accXEntriesArrayList = new ArrayList<>();
        accYEntriesArrayList = new ArrayList<>();
        accZEntriesArrayList = new ArrayList<>();
        lines = new ArrayList<ILineDataSet>();

        tvStart = findViewById(R.id.tvStart);
        tvEnd = findViewById(R.id.tvEnd);
        tvHeart = findViewById(R.id.tvHeart);

        db = FirebaseFirestore.getInstance();

        db.collection("sleepdata").whereEqualTo("id", EXTRA).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e !=null) {
                        }
                        DocumentChange documentChange = documentSnapshots.getDocumentChanges().get(0);

                        idEntry = documentChange.getDocument().getData().get("id").toString();

                        Timestamp timestamp = (Timestamp) documentChange.getDocument().getData().get("startDate");
                        Date stDate = timestamp.toDate();
                        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        String startDate = sfd.format(stDate);
                        tvStart.setText(startDate);

                        Timestamp timestamp2 = (Timestamp) documentChange.getDocument().getData().get("endDate");
                        Date edDate = timestamp2.toDate();
                        String endDate = sfd.format(edDate);
                        tvEnd.setText(endDate);

                        Long heartSum = 0L;

                        ArrayList<Long> heartList = (ArrayList<Long>) documentChange.getDocument().getData().get("heart");
                        for(int i=0; i<heartList.size(); i++){
                            Log.d("Watch123", heartList.get(i).toString());
                            heartSum = heartSum + heartList.get(i);
                            heartEntriesArrayList.add(new Entry(i+1, heartList.get(i)));
                        }

                        //casting to string
                        tvHeart.setText(heartSum.intValue()/heartList.size()+"");
                        heartDataSet = new LineDataSet(heartEntriesArrayList, "");
                        heartData = new LineData(heartDataSet);
                        heartChart.setData(heartData);
                        heartDataSet.setValueTextColor(Color.BLACK);
                        heartDataSet.setValueTextSize(16f);
                        heartDataSet.setDrawCircles(false);
                        heartChart.getDescription().setEnabled(false);
                        heartChart.setMaxVisibleValueCount(0);
                        heartChart.getAxisRight().setDrawLabels(false);
                        heartChart.getXAxis().setDrawLabels(false);
                        heartChart.getLegend().setEnabled(false);
                        heartChart.invalidate();
                        heartChart.refreshDrawableState();

                        ArrayList<Double> accXList = (ArrayList<Double>) documentChange.getDocument().getData().get("accX");
                        ArrayList<Double> accYList = (ArrayList<Double>) documentChange.getDocument().getData().get("accY");
                        ArrayList<Double> accZList = (ArrayList<Double>) documentChange.getDocument().getData().get("accZ");
                        for(int i=0; i<accXList.size(); i++){
                            accXEntriesArrayList.add(new Entry(i+1, accXList.get(i).floatValue()));
                            accYEntriesArrayList.add(new Entry(i+1, accYList.get(i).floatValue()));
                            accZEntriesArrayList.add(new Entry(i+1, accZList.get(i).floatValue()));
                        }

                        accXDataSet = new LineDataSet(accXEntriesArrayList, "oś X");
                        accYDataSet = new LineDataSet(accYEntriesArrayList, "oś Y");
                        accZDataSet = new LineDataSet(accZEntriesArrayList, "oś Z");
                        accYDataSet.setColor(Color.BLUE);
                        accZDataSet.setColor(Color.MAGENTA);
                        accXDataSet.setDrawCircles(false);
                        accYDataSet.setDrawCircles(false);
                        accZDataSet.setDrawCircles(false);
                        lines.add(accXDataSet);
                        lines.add(accYDataSet);
                        lines.add(accZDataSet);
                        accData = new LineData(lines);
                        accChart.setData(accData);
                        accChart.getDescription().setEnabled(false);
                        accChart.getAxisRight().setDrawLabels(false);
                        accChart.getXAxis().setDrawLabels(false);
                        accChart.setMaxVisibleValueCount(0);
                        accChart.invalidate();
                        accChart.refreshDrawableState();

                    }
                });
    }

    public void onDeleteClick(View view) {

        CollectionReference itemsRef = db.collection("sleepdata");
        Query query = itemsRef.whereEqualTo("id", idEntry);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        itemsRef.document(document.getId()).delete().
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            createPopupDelete();
                                        } else {
                                            createPopupFail();
                                        }
                                    }
                                });
                    }
                } else {
                    createPopupFail();
                }
            }
        });
    }

    public void onReturnClick(View view) {
        Intent intent = new Intent(WatchEntryActivity.this, WatchListActivity.class);
        WatchEntryActivity.this.startActivity(intent);
        WatchEntryActivity.this.finish();
    }

    public void createPopupFail(){
        dialogBuilder3 = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.popupfail, null);
        cancelDialog3 = (Button) popup.findViewById(R.id.cancelDialog6);
        dialogBuilder3.setView(popup);
        dialog3 = dialogBuilder3.create();
        dialog3.show();

        cancelDialog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog3.dismiss();
            }
        });
    }

    public void createPopupDelete(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.popupdelete, null);
        cancelDialog = (Button) popup.findViewById(R.id.noBtn);
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(WatchEntryActivity.this, WatchListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //finish();
            }
        });
    }
}