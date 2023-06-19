package com.example.sleepdiary.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sleepdiary.PatientIdSingleton;
import com.example.sleepdiary.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DiaryListActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference collectRef;
    private RecyclerView recyclerView;
    private String patId;

    private EntriesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);
        db = FirebaseFirestore.getInstance();
        collectRef = db.collection("sleepdiary");
        PatientIdSingleton patientIdSingleton = com.example.sleepdiary.PatientIdSingleton.getInstance();
        patId = patientIdSingleton.getId();
        recyclerView = findViewById(R.id.diaryRv);
        setUpRecyclerView();
        recyclerView.setItemAnimator(null);
    }

    public void onAddClick(View view) {
        startActivity(new Intent(DiaryListActivity.this, AddDiaryActivity.class));
        DiaryListActivity.this.finish();
    }

    private void setUpRecyclerView(){

        Query query = collectRef.whereEqualTo("patientId", patId).
                orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<SleepDiary> options = new FirestoreRecyclerOptions.Builder<SleepDiary>()
                .setQuery(query, SleepDiary.class)
                .build();

        adapter = new EntriesAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}