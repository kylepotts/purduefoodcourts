package com.kptech.purduefoodcourts.app.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;


import com.kptech.purduefoodcourts.app.R;
import com.kptech.purduefoodcourts.app.receivers.NotifyFavoritesReceiver;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by kyle on 8/20/14.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs);
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("OnSharedPrefChanged",key);
        if(key.equals("pref_key_time_picker")){
            setAlarm();

        }

    }

    public void setAlarm(){
        Intent myIntent = new Intent(this, NotifyFavoritesReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());



        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String time = settings.getString("pref_key_time_picker","0");
        if(time.equals("0")){
            String randHour = "0"+randInt(7,9);
            int min = randInt(0,60);
            String randMin;
            if(min <10){
                randMin = "0"+min;
            } else {
                randMin = ""+min;
            }
            String wholeTime = randHour+":"+randMin;
            SharedPreferences.Editor edit = settings.edit();
            edit.putString("pref_key_time_picker",wholeTime);
            edit.commit();


        }
        time = settings.getString("pref_key_time_picker","0");
        String[] splitTime = time.split(":");
        String hour = splitTime[0];
        String min = splitTime[1];

        int hourPref = Integer.parseInt(hour);
        int minPref = Integer.parseInt(min);
        calendar.set(Calendar.HOUR_OF_DAY, hourPref);
        calendar.set(Calendar.MINUTE,minPref);


        Log.d("debug-time-to-notify", time);


        long _alarm;
        Calendar now = Calendar.getInstance();
        Calendar alarm = Calendar.getInstance();
        alarm.set(Calendar.HOUR_OF_DAY, hourPref);
        alarm.set(Calendar.MINUTE, minPref);

        if(alarm.getTimeInMillis()<= now.getTimeInMillis()){
            _alarm = alarm.getTimeInMillis() +(AlarmManager.INTERVAL_DAY+1);

        } else {
            _alarm = alarm.getTimeInMillis();
        }




        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, _alarm,
                AlarmManager.INTERVAL_DAY, pendingIntent);


    }

    public  int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


}
