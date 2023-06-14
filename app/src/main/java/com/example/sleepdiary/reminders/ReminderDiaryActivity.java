package com.example.sleepdiary.reminders;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sleepdiary.R;

import java.util.Calendar;
import java.util.Locale;

public class ReminderDiaryActivity extends AppCompatActivity {

    private TextView selectedTime;
    int hours, minutes;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    //TODO save set time in firebase or on the device
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_diary);
        createNotificationChannel();

        selectedTime = findViewById(R.id.selectedTime);
        calendar = Calendar.getInstance();
    }

    public void onSetTime(View view) {
        showTimePicker();
    }

    public void onSetReminder(View view) {
        Log.d("ALARM123", String.valueOf(calendar.getTime()));

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        //TODO change interval
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        Toast.makeText(this, "Ustawiono przypomnienie!", Toast.LENGTH_SHORT).show();

        //TODO improve calendar
        Log.d("ALARM123", String.valueOf(calendar.getTime()));
    }

    public void onCancelReminder(View view) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);
        selectedTime.setText("00:00");
        hours = 0;
        minutes = 0;
        Toast.makeText(this, "Wyłączono przypomnienie", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        CharSequence name = "sleepdiaryNotificationChannel";
        String description = "Channel for sleep diary";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel("sleepdiary1", name, importance);
        notificationChannel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private void showTimePicker(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hours = i;
                minutes = i1;
                selectedTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hours, minutes));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                onTimeSetListener, hours, minutes, true);
        timePickerDialog.setTitle("Ustaw godzinę");
        timePickerDialog.show();
    }
}