package com.kptech.purduefoodcourts.app.Adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.*;

import com.kptech.purduefoodcourts.app.Data.APIResponse;
import com.kptech.purduefoodcourts.app.Fragments.MenuListFragment;
import com.kptech.purduefoodcourts.app.Interfaces.OnFoodItemsReceivedHandler;
import com.kptech.purduefoodcourts.app.Interfaces.OnMenuXmlReceivedHandler;
import com.kptech.purduefoodcourts.app.PurdueAPIParser;
import com.kptech.purduefoodcourts.app.tasks.GetMenuXmlTask;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kyle on 5/9/14.
 */
public class MenuPagerAdapater extends FragmentPagerAdapter implements OnMenuXmlReceivedHandler {
    android.widget.ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private Activity context;
    String location;
    MenuListFragment[] menuListFragments = new MenuListFragment[3];
    String mealType;
    public MenuPagerAdapater(FragmentManager fm) {
        super(fm);
    }

    public MenuPagerAdapater(FragmentManager fm, Activity a, String location){
        super(fm);
        this.context = a;
        this.location = location;
    }

    @Override
    public Fragment getItem(int position) {
        String mealType = null;
        switch(position){
            case 0:
                mealType = "Breakfast";
                break;
            case 1:
                mealType = "Lunch";
                break;
            case 2:
                mealType = "Dinner";
                break;
        }

        Log.d("PagerAdaper","createMenuList");

        MenuListFragment test = new MenuListFragment();
        Bundle args = new Bundle();
        args.putString("Location", location);
        args.putString("MealType",mealType);
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String date = "/"+df.format(Calendar.getInstance().getTime());
        String mUrl = "http://api.hfs.purdue.edu/menus/v1/locations/"+location.toLowerCase()+date+"/";
        boolean isCached = isUrlCached(location,mUrl);

        PurdueAPIParser apiParser = null;
        if(isCached){
            Log.d("menupger","data already cached");
            SharedPreferences settings = context.getSharedPreferences("fav",0);
            String xml = settings.getString(location+"data",null);
            try {
                 apiParser = new PurdueAPIParser(xml);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(mealType.equals("Breakfast")){
                test = new MenuListFragment(apiParser.getBreakfast(),context);
            } else if (mealType.equals("Lunch")){
                test = new MenuListFragment(apiParser.getLunch(),context);
            } else {
                test = new MenuListFragment(apiParser.getDinner(),context);
            }
            test.setArguments(args);
            return test;

        } else {
            test.setArguments(args);
            return test;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "Breakfast";
        } else if (position == 1){
            return "Lunch";
        } else{
             return "Dinner";
        }

    }

    public boolean isUrlCached(String location,String url){
        SharedPreferences settings = context.getSharedPreferences("fav",0);
        String lastUrl = settings.getString(location+"url",null);
        String xml = settings.getString(location+"data",null);
        if(lastUrl == null || xml == null){
            Log.d("pageradpater","url is null");
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(location+"url",url);
           // editor.putString(location+"data",getXmlFormUrl(url));
            GetMenuXmlTask task = new GetMenuXmlTask(url,location,this);
            task.execute();
            editor.commit();
            return false;

        } else {
            String date = lastUrl.substring(lastUrl.length() - 10, lastUrl.length() - 1);
            Log.d("getfoodmenu", "lastUrlDate= " + date);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-yyyy");
            DateTime prev = formatter.parseDateTime(date);
            DateTime today = new DateTime();
            Duration duration = new Duration(prev,today);
            long daysBetween = duration.getStandardDays();
            Log.d("getfoodmenu","daysBetween= " + daysBetween);
            if(daysBetween > 0){
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(location+"url",url);
                //editor.putString(location+"data",getXmlFormUrl(url));
                GetMenuXmlTask task = new GetMenuXmlTask(url,location,this);
                task.execute();
                editor.commit();
                return false;
            } else {
                return true;
            }


        }



    }

    public void cacheWithXml(String location, String xml){
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String date = "/"+df.format(Calendar.getInstance().getTime());
        String mUrl = "http://api.hfs.purdue.edu/menus/v1/locations/"+location.toLowerCase()+date+"/";
        SharedPreferences.Editor editor = context.getSharedPreferences("fav",0).edit();
        editor.putString(location+"url",mUrl);
        editor.putString(location+"data",xml);


    }




    @Override
    public void onMenuXmlReceived(String xmlResp, String location) {
        cacheWithXml(location,xmlResp);

    }
}
