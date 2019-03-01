package com.example.reminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TimePicker remind_time = findViewById(R.id.reminder_time);
        remind_time.setIs24HourView(true);
        Button doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReminderData data = new ReminderData(this);
        data.loadFromPrefs();
        EditText editText = findViewById(R.id.edit_text);
        editText.setText(data.getReminder_text());
    }

    @Override
    public void onClick(View v) {
        ReminderData data = new ReminderData(this);
        EditText editText = findViewById(R.id.edit_text);
        if(v.getId() == R.id.done_button) {
            TimePicker picker = findViewById(R.id.reminder_time);

            data.setHours(picker.getCurrentHour());
            data.setMinutes(picker.getCurrentMinute());
            data.setReminder_text(editText.getText().toString());
            data.saveDataToPrefs();

            Calendar rightNow = Calendar.getInstance();
            int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
            int currentMinute = rightNow.get(Calendar.MINUTE);

            long timeToAlarmInMillis = ((data.getHours() - currentHour) * 3600 * 1000) + ((data.getMinutes() - currentMinute) * 60 * 1000);
            Log.d("TIME",  "" + timeToAlarmInMillis);
            timeToAlarmInMillis = timeToAlarmInMillis - (currentTimeMillis()%3600000)%60000;
            Log.d("TIME",  "" + timeToAlarmInMillis);

            if(timeToAlarmInMillis < 0) {
                Toast.makeText(this, "Cannot set reminder to past", Toast.LENGTH_SHORT).show();


            } else if (data.getReminder_text() == null) {
                Toast.makeText(this, "Enter reminder text", Toast.LENGTH_SHORT).show();
            } else {
                AlarmManager mgr=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mgr.set(AlarmManager.RTC_WAKEUP, currentTimeMillis() + timeToAlarmInMillis, pi);
                Toast.makeText(this, "Reminder set", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
