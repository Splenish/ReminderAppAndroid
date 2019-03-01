package com.example.reminderapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

public class ReminderData {
    int minutes;
    int hours;
    String reminder_text;
    Context context;

    public ReminderData(Context context) {
        this.context = context;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getReminder_text() {
        return reminder_text;
    }

    public void setReminder_text(String reminder_text) {
        this.reminder_text = reminder_text;
    }

    public void saveDataToPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Text", reminder_text);
        editor.putInt("Hours", hours);
        editor.putInt("Minutes", minutes);
        editor.apply();
    }

    public void loadFromPrefs() {
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMinute = rightNow.get(Calendar.MINUTE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        reminder_text = preferences.getString("Text", "");
        hours = preferences.getInt("Hours", currentHour);
        minutes = preferences.getInt("Minutes", currentMinute);
    }

}
