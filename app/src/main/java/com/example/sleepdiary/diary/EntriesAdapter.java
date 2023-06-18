package com.example.sleepdiary.diary;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sleepdiary.R;
import com.example.sleepdiary.diary.DiaryEntryActivity;
import com.example.sleepdiary.diary.SleepDiary;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class EntriesAdapter
        extends FirestoreRecyclerAdapter<SleepDiary, EntriesAdapter.EntriesHolder>{

    public EntriesAdapter(@NonNull FirestoreRecyclerOptions<SleepDiary> options) {
        super(options);
    }

    public String id;

    @Override
    protected void onBindViewHolder(@NonNull EntriesHolder holder, int position, @NonNull SleepDiary model) {
        holder.dateAd.setText(model.getEntryDate());

        holder.showButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = model.getId();
                Intent i = new Intent (holder.showButton1.getContext(), DiaryEntryActivity.class);
                i.putExtra("EXTRA", id);
                holder.showButton1.getContext().startActivity(i);
                Log.d("sleepdiary", id);
                //((Activity)holder.showButton1.getContext()).finish();
            }
        });

    }

    @NonNull
    @Override
    public EntriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item, parent, false);
        return new EntriesHolder(v);
    }

    class EntriesHolder extends RecyclerView.ViewHolder {
        TextView dateAd;
        Button showButton1;

        public EntriesHolder(View itemView) {
            super(itemView);
            dateAd = itemView.findViewById(R.id.dateAd);
            showButton1 = itemView.findViewById(R.id.showButton1);
        }
    }

}