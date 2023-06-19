package com.example.sleepdiary.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sleepdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class DiaryEntryActivity extends AppCompatActivity {

    public String EXTRA="";
    private TextView dateTv, a1tv, a2tv, a3tv, a4tv, a5tv, a6tv, a7tv, a8tv, a9tv;
    private FirebaseFirestore db;
    private String idEntry;
    private AlertDialog.Builder dialogBuilder, dialogBuilder3;
    private AlertDialog dialog, dialog3;
    private Button cancelDialog, cancelDialog3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);

        Intent intent = getIntent();
        EXTRA = intent.getStringExtra("EXTRA");
        Log.d("sleepdiary3", EXTRA);

        dateTv = (TextView) findViewById(R.id.dateTv);
        a1tv = (TextView) findViewById(R.id.a1tv);
        a2tv = (TextView) findViewById(R.id.a2tv);
        a3tv = (TextView) findViewById(R.id.a3tv);
        a4tv = (TextView) findViewById(R.id.a4tv);
        a5tv = (TextView) findViewById(R.id.a5tv);
        a6tv = (TextView) findViewById(R.id.a6tv);
        a7tv = (TextView) findViewById(R.id.a7tv);
        a8tv = (TextView) findViewById(R.id.a8tv);
        a9tv = (TextView) findViewById(R.id.a9tv);

        db = FirebaseFirestore.getInstance();

        db.collection("sleepdiary").whereEqualTo("id", EXTRA).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e !=null) {
                        }
                        DocumentChange documentChange = documentSnapshots.getDocumentChanges().get(0);
                        String dateQ =  documentChange.getDocument().getData().get("entryDate").toString();
                        String a1 =  documentChange.getDocument().getData().get("q1").toString();
                        String a2 =  documentChange.getDocument().getData().get("q2").toString();
                        String a3 =  documentChange.getDocument().getData().get("q3").toString();
                        String a4 =  documentChange.getDocument().getData().get("q4").toString();
                        String a5 =  documentChange.getDocument().getData().get("q5").toString();
                        String a6 =  documentChange.getDocument().getData().get("q6").toString();
                        String a7 =  documentChange.getDocument().getData().get("q7").toString();
                        String a8 =  documentChange.getDocument().getData().get("q8").toString();
                        String a9 =  documentChange.getDocument().getData().get("q9").toString();
                        idEntry = documentChange.getDocument().getData().get("id").toString();

                        dateTv.setText(dateQ);
                        a1tv.setText(a1);
                        a2tv.setText(a2);
                        a3tv.setText(a3);
                        a4tv.setText(a4);
                        a5tv.setText(a5);
                        a6tv.setText(a6);
                        a7tv.setText(a7);
                        a8tv.setText(a8);
                        a9tv.setText(a9);
                    }
                });
    }

    public void onDeleteClick(View view) {

        CollectionReference itemsRef = db.collection("sleepdiary");
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
        Intent intent = new Intent(DiaryEntryActivity.this, DiaryListActivity.class);
        DiaryEntryActivity.this.startActivity(intent);
        DiaryEntryActivity.this.finish();
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
                Intent i = new Intent(DiaryEntryActivity.this, DiaryListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //finish();
            }
        });
    }
}