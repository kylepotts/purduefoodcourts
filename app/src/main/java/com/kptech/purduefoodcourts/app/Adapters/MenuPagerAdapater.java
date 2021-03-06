package com.kptech.purduefoodcourts.app.adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.*;

import com.kptech.purduefoodcourts.app.fragments.MenuListFragment;
import com.kptech.purduefoodcourts.app.interfaces.OnMenuXmlReceivedHandler;
import com.kptech.purduefoodcourts.app.PurdueAPIParser;
import com.kptech.purduefoodcourts.app.tasks.GetMenuXmlTask;


import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;

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

        MenuListFragment menuList = new MenuListFragment();
        Bundle args = new Bundle();
        args.putString("Location", location);
        args.putString("MealType",mealType);
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String date = "/"+df.format(Calendar.getInstance().getTime());
        String mUrl = "http://api.hfs.purdue.edu/menus/v1/locations/"+location.toLowerCase()+date+"/";

        // to check if we have the current menu for this location already cached
        boolean isCached = isUrlCached(location,mUrl);

        PurdueAPIParser apiParser = null;
        /*
            If the current menu is cached, load the xml for the menu from shared preferences and create an apiParser from the xml
            Then create a new MenuListFragment and give it the apiParse, then the Fragment will display the menu instantly without
            pulling it from the api site
         */
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
                args.putString("xml",xml);
                menuList = new MenuListFragment();
            } else if (mealType.equals("Lunch")){
                menuList = new MenuListFragment();
            } else {
                menuList = new MenuListFragment();
            }
            menuList.setArguments(args);
            return menuList;

        } else {
            menuList.setArguments(args);
            return menuList;
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
        // Load the menu xml and the url used to retreieve it
        SharedPreferences settings = context.getSharedPreferences("fav",0);
        String lastUrl = settings.getString(location+"url",null);
        String xml = settings.getString(location+"data",null);
        // if either or null, we have not cached the data so run a task and gets it and caches it
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
            // Now we have cached data, we need to compare the dates, we strip the date from the previous url we used
            // and compare it with todays date
            String date = lastUrl.substring(lastUrl.length() - 10, lastUrl.length() - 1);
            Log.d("getfoodmenu", "lastUrlDate= " + date);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-yyyy");
            DateTime prev = formatter.parseDateTime(date);
            DateTime today = new DateTime();
            Duration duration = new Duration(prev,today);
            long daysBetween = duration.getStandardDays();
            Log.d("getfoodmenu","daysBetween= " + daysBetween);
            // If there is greater than one day between the cached menu, then we need to recache the data
            if(daysBetween > 0){
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(location+"url",url);
                GetMenuXmlTask task = new GetMenuXmlTask(url,location,this);
                task.execute();
                editor.commit();
                return false;
            } else {
                return true;
            }


        }



    }

    public void cacheMenu(String location, String xml){
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String date = "/"+df.format(Calendar.getInstance().getTime());
        String mUrl = "http://api.hfs.purdue.edu/menus/v1/locations/"+location.toLowerCase()+date+"/";
        SharedPreferences.Editor editor = context.getSharedPreferences("fav",0).edit();
        editor.putString(location+"url",mUrl);
        editor.putString(location+"data",xml);


    }




    @Override
    public void onMenuXmlReceived(String xmlResp, String location) {
        cacheMenu(location,xmlResp);

    }
}
