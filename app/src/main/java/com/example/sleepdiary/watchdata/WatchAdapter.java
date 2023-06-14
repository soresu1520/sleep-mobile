package com.example.sleepdiary.watchdata;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WatchAdapter extends FirestoreRecyclerAdapter<WatchData, WatchAdapter.WatchHolder> {

    public WatchAdapter(@NonNull FirestoreRecyclerOptions<WatchData> options) {
        super(options);
    }

    public String id;

    @Override
    protected void onBindViewHolder(@NonNull WatchAdapter.WatchHolder holder, int position, @NonNull WatchData model) {
        Date timestamp = model.getStartDate().toDate();
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String startDate = sfd.format(timestamp);
        holder.dateStart.setText(startDate);
        Log.d("WATCH123", startDate);

        holder.showButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = model.getId();
                Intent i = new Intent (holder.showButton2.getContext(), WatchEntryActivity.class);
                i.putExtra("EXTRA", id);
                holder.showButton2.getContext().startActivity(i);
                Log.d("sleepdiary", id);
                //((Activity)holder.showButton2.getContext()).finish();
            }
        });

    }

    @NonNull
    @Override
    public WatchAdapter.WatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.watch_item, parent, false);
        return new WatchAdapter.WatchHolder(v);
    }

    class WatchHolder extends RecyclerView.ViewHolder {
        TextView dateStart;
        Button showButton2;

        public WatchHolder(View itemView) {
            super(itemView);
            dateStart = itemView.findViewById(R.id.dateStart);
            showButton2 = itemView.findViewById(R.id.showButton2);
        }
    }

    private static Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (Activity) context;
    }
}
