package com.example.sleepdiary.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.sleepdiary.PatientIdSingleton;
import com.example.sleepdiary.R;
import com.example.sleepdiary.Utilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddDiaryActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder, dialogBuilder2, dialogBuilder4, dialogBuilder3, dialogBuilder5, dialogBuilder6;
    private AlertDialog dialog, dialog2, dialog3, dialog4, dialog5, dialog6;
    private Button cancelDialog, cancelDialog2, cancelDialog3, cancelDialog4, cancelDialog5, cancelDialog6;
    private TextView todayDate, que2, que3, que5, que7;
    private EditText que4, que9;
    private Spinner que8, que1, que6;
    private String qDate, q1, q2, q3,q4, q5, q6, q7, q8, q9, timeStamp;
    private int hour, minute;
    private int mYear, mMonth, mDay;
    private String idDiary;
    private FirebaseFirestore db;
    private String email, patId;
    private Timestamp ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        todayDate = (TextView) findViewById(R.id.todayDate);
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        todayDate.setText(date.format(formatter));

        PatientIdSingleton patientIdSingleton = com.example.sleepdiary.PatientIdSingleton.getInstance();
        patId = patientIdSingleton.getId();
    }

    public void onAddClick(View view) throws ParseException {
        todayDate = (TextView) findViewById(R.id.todayDate);
        que1 = (Spinner) findViewById(R.id.q1);
        que2 = (TextView) findViewById(R.id.q2);
        que3 = (TextView) findViewById(R.id.q3);
        que4 = (EditText) findViewById(R.id.q4);
        que5 = (TextView) findViewById(R.id.q5);
        que6 = (Spinner) findViewById(R.id.q6);
        que7 = (TextView) findViewById(R.id.q7);
        que8 = (Spinner) findViewById(R.id.q8);
        que9 = (EditText) findViewById(R.id.q9);

        qDate = todayDate.getText().toString();
        q1 = String.valueOf(que1.getSelectedItem());
        q2 = que2.getText().toString();
        q3 = que3.getText().toString();
        q4 = que4.getText().toString();
        q5 = que5.getText().toString();
        q6 = String.valueOf(que6.getSelectedItem());
        q7 = que7.getText().toString();
        q8 = String.valueOf(que8.getSelectedItem());
        q9 = que9.getText().toString();

        idDiary = UUID.randomUUID().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        timeStamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(qDate);
        ts = new Timestamp(date);

        int sleepMinutes = Utilities.getSleepTimeInMinutes(q3, q5, q4);
        int bedMinutes = Utilities.getBedTimeInMinutes(q2, q7);
        Log.d("add123", String.valueOf(sleepMinutes));
        Log.d("add123", String.valueOf(bedMinutes));

        if ( que4.getText().toString().isEmpty()==true){
            createNewPopup2();
        }
        else if (sleepMinutes > bedMinutes){
            createTimePopup();
        }
        else{
            FirebaseFirestore.getInstance().collection("sleepdiary").
                    whereEqualTo("patientEmail", email).
                    whereEqualTo("dateEntry", qDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        boolean documentExists = false;

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("QueryResult", "Is query result empty: " + task.getResult().isEmpty());
                                documentExists = !task.getResult().isEmpty();

                            }else {
                                Log.e("QueryResult", "Error getting documents.", task.getException());
                                documentExists = true;
                            }

                            if(documentExists){
                                createExistsPopup();
                            }
                            else {
                                addDataToFirestore(idDiary, email, patId, qDate, q1, q2, q3, q4, q5, q6, q7, q8, q9, ts);
                            }
                            Log.d("QueryResult2", String.valueOf(documentExists));
                            Log.d("QueryResult3", Calendar.getInstance().getTime().toString());
                            Log.d("QueryResult4", String.valueOf(Calendar.getInstance().getTimeInMillis()));
                        }
                    });
        }
    }

    private void addDataToFirestore(String id, String patientEmail, String patId, String dateEntry, String q1,
                                    String q2, String q3, String q4, String q5, String q6,
                                    String q7, String q8, String q9, Timestamp timestamp) {

        db = FirebaseFirestore.getInstance();
        CollectionReference dbCourses = db.collection("sleepdiary");

        SleepDiary sleepDiary = new SleepDiary(id, patId, patientEmail, dateEntry, q1, q2, q3, q4, q5, q6,
                q7, q8, q9, ts);

        db.collection("sleepdiary").document(id).set(sleepDiary).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                createPopupSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                createPopupFail();
            }
        });
    }

    public void onDateClick(View view) {
        todayDate = (TextView) findViewById(R.id.todayDate);
        showDatePicker(todayDate);
    }

    public void onQ2(View view) {
        que2 = (TextView) findViewById(R.id.q2);
        showTimePicker(que2);
    }

    public void onQ3(View view) {
        que3 = (TextView) findViewById(R.id.q3);
        showTimePicker(que3);
    }

    public void onQ5(View view) {
        que5 = (TextView) findViewById(R.id.q5);
        showTimePicker(que5);
    }

    public void onQ7(View view) {
        que7 = (TextView) findViewById(R.id.q7);
        showTimePicker(que7);
    }

    public void onHelpClick(View view) {
        createNewPopup();
    }

    public void createNewPopup(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.popuphelp, null);
        cancelDialog = (Button) popup.findViewById(R.id.cancelDialog);
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void createNewPopup2(){
        dialogBuilder2 = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.popuperror, null);
        cancelDialog2 = (Button) popup.findViewById(R.id.cancelDialog4);
        dialogBuilder2.setView(popup);
        dialog2 = dialogBuilder2.create();
        dialog2.show();

        cancelDialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
    }

    public void createPopupSuccess(){
        dialogBuilder4 = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.popsucess, null);
        cancelDialog4 = (Button) popup.findViewById(R.id.cancelDialog5);
        dialogBuilder4.setView(popup);
        dialog4 = dialogBuilder4.create();
        dialog4.show();

        cancelDialog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog4.dismiss();
                Intent intent = new Intent(AddDiaryActivity.this, DiaryListActivity.class);
                AddDiaryActivity.this.startActivity(intent);
                //AddDiaryActivity.this.finish();
            }
        });
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

    public void createExistsPopup(){
        dialogBuilder5 = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.popupexists, null);
        cancelDialog5 = (Button) popup.findViewById(R.id.cancelDialog10);
        dialogBuilder5.setView(popup);
        dialog5 = dialogBuilder5.create();
        dialog5.show();

        cancelDialog5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog5.dismiss();
            }
        });
    }

    public void createTimePopup(){
        dialogBuilder6 = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.popuptime, null);
        cancelDialog6 = (Button) popup.findViewById(R.id.cancelDialog10);
        dialogBuilder6.setView(popup);
        dialog6 = dialogBuilder6.create();
        dialog6.show();

        cancelDialog6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog6.dismiss();
            }
        });
    }

    private void showTimePicker(TextView tv) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                tv.setText(String.format(Locale.getDefault(),"%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                onTimeSetListener, hour, minute,true);
        timePickerDialog.setTitle("Ustaw godzinę");
        timePickerDialog.show();

    }

    private void showDatePicker(TextView tv) {

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
                        tv.setText(LocalDate.of(year, monthOfYear, dayOfMonth).format(formatter));
                        //Log.d("date123", new Date(year, monthOfYear, dayOfMonth).toString());
                        //Log.d("date123", ts.toString());
                        //Log.d("date123", ts.toDate().toString());
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    }