package com.example.sleepdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleepdiary.authentication.ChangePasswordActivity;
import com.example.sleepdiary.authentication.LoginActivity;
import com.example.sleepdiary.diary.AddDiaryActivity;
import com.example.sleepdiary.diary.DiaryListActivity;
import com.example.sleepdiary.diary.StatisticsActivity;
import com.example.sleepdiary.reminders.ReminderDiaryActivity;
import com.example.sleepdiary.watchdata.WatchListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

//MENU
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        TextView tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText(email);

        database = FirebaseFirestore.getInstance();
        email = auth.getCurrentUser().getEmail();
        getId(email);
    }

    public void onRemindDiaryClick(View view) {
        startActivity(new Intent(MainActivity.this, ReminderDiaryActivity.class));
    }

    public void onChangePasswordClick(View view) {
        startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
    }

    public void onLogoutClick(View view) {
        auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    public void onAddClick(View view) {
        startActivity(new Intent(MainActivity.this, AddDiaryActivity.class));
    }

    public void onDiaryClick(View view) {
        startActivity(new Intent(MainActivity.this, DiaryListActivity.class));
    }

    public void onStatisticsClick(View view) {
        startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
    }

    public void onSmartClick(View view) {
        startActivity(new Intent(MainActivity.this, WatchListActivity.class));
    }

    private void getId(String email) {
        database.collection("patients").whereEqualTo("email", email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                        }
                        DocumentChange documentChange = documentSnapshots.getDocumentChanges().get(0);
                        String id = documentChange.getDocument().getData().get("id").toString();
                        PatientIdSingleton patientIdSingleton = com.example.sleepdiary.PatientIdSingleton.getInstance();
                        patientIdSingleton.setId(id);
                    }
                });
    }
}