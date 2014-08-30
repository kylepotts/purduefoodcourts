package com.kptech.purduefoodcourts.app.tasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.kptech.purduefoodcourts.app.Data.APIResponse;
import com.kptech.purduefoodcourts.app.Interfaces.OnFoodItemsReceivedHandler;
import com.kptech.purduefoodcourts.app.PurdueAPIParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.joda.time.Duration;

/**
 * Created by kyle on 8/2/14.
 */
public class GetFoodMenuTask extends AsyncTask<Void,Void,Void> {
    private  Activity activity;
    private String location;
    private String mealType;
    private OnFoodItemsReceivedHandler handler;
    private boolean cacheData;

    public GetFoodMenuTask(Activity a,String location, String mealType, boolean cacheData ,OnFoodItemsReceivedHandler handler){
        this.activity = a;
        this.location = location;
        this.mealType = mealType;
        this.handler = handler;
        this.cacheData = cacheData;


    }
    @Override
    protected Void doInBackground(Void... voids) {
        APIResponse r =  getMenu(location,mealType);

        if(handler != null){
            handler.onFoodItemsReceived(r);
        }

        return null;
    }

    public void cacheData(String url, String xml, String location){
        Log.d("foodmenutask","caching data");
        SharedPreferences.Editor editor = activity.getSharedPreferences("fav",0).edit();
        editor.putString(location+"url",url);
        editor.putString(location+"data",getXmlFormUrl(url));
        editor.commit();
    }


    public APIResponse getMenu(final String location, final String mealType){
                APIResponse response = null;
                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                String date = "/"+df.format(Calendar.getInstance().getTime());
                String mUrl = "http://api.hfs.purdue.edu/menus/v1/locations/"+location.toLowerCase()+date+"/";
                Log.d("debug-url", mUrl);
                if(cacheData){
                    cacheData(mUrl,getXmlFormUrl(mUrl),location);
                }
               // checkForLastUrlPulled(location,mUrl);
                boolean isUrlCahced = isUrlCached(location,mUrl);
                /*
                String xml = null;
                if(!isUrlCahced) {
                    xml = getXmlFormUrl(mUrl);
                } else {
                    xml = activity.getSharedPreferences("favs",0).getString(location+"data",null);
                }
                */
                String xml = getXmlFormUrl(mUrl);
                PurdueAPIParser apiParser = null;
                try {
                    apiParser = new PurdueAPIParser(xml);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                if(mealType == "Breakfast"){
                    response = apiParser.getBreakfast();
                } else if (mealType == "Lunch"){
                    response = apiParser.getLunch();
                } else if (mealType == "Dinner") {
                    response = apiParser.getDinner();
                }

                return response;



    }

    public boolean isUrlCached(String location,String url){
        SharedPreferences settings = activity.getSharedPreferences("fav",0);
        String lastUrl = settings.getString(location+"url",null);
        if(lastUrl == null){
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(location+"url",url);
            editor.putString(location+"data",getXmlFormUrl(url));
            editor.commit();
            return false;

        } else {
            String date = lastUrl.substring(lastUrl.length()-10, lastUrl.length()-1);
            Log.d("getfoodmenu","lastUrlDate= " + date);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-yyyy");
            DateTime prev = formatter.parseDateTime(date);
            DateTime today = new DateTime();
            Duration duration = new Duration(prev,today);
            long daysBetween = duration.getStandardDays();
            Log.d("getfoodmenu","daysBetween= " + daysBetween);
            if(daysBetween > 0){
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(location+"url",url);
                editor.putString(location+"data",getXmlFormUrl(url));
                editor.commit();
                return false;
            } else {
                return true;
            }


        }

    }


    public String getXmlFormUrl(String url){
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        String responseString = null;
        try {
            HttpResponse response = client.execute(get);
            responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }


}
