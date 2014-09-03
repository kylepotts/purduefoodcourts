package com.kptech.purduefoodcourts.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.kptech.purduefoodcourts.app.activities.SettingsActivity;
import com.kptech.purduefoodcourts.app.fragments.CourtGridFragment;
import com.kptech.purduefoodcourts.app.receivers.NotifyFavoritesReceiver;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.Calendar;
import java.util.Random;


public class MainActivity extends  Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JodaTimeAndroid.init(this);
        setAlarm();


    }

    @Override
    public void onStart(){
        super.onStart();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
       CourtGridFragment frag = new CourtGridFragment();
        fragmentTransaction.replace(android.R.id.content, frag).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.default_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public  int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public  void setAlarm(){
        Intent myIntent = new Intent(MainActivity.this, NotifyFavoritesReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
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


        Log.d("debug-time-to-notify",time);


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



}
