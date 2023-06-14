package com.example.sleepdiary.watchdata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.sleepdiary.R;
import com.example.sleepdiary.diary.EntriesAdapter;
import com.example.sleepdiary.diary.SleepDiary;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class WatchListActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference collectRef;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private String email, patId;

    private WatchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        db = FirebaseFirestore.getInstance();
        collectRef = db.collection("sleepdata");
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        recyclerView = findViewById(R.id.watchRv);
        recyclerView.setItemAnimator(null);
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){

        Query query = collectRef.whereEqualTo("patientEmail", email).
                orderBy("startDate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<WatchData> options = new FirestoreRecyclerOptions.Builder<WatchData>()
                .setQuery(query, WatchData.class)
                .build();

        adapter = new WatchAdapter(options);
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