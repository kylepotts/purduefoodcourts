package com.kptech.purduefoodcourts.app.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.*;

import com.kptech.purduefoodcourts.app.Data.APIResponse;
import com.kptech.purduefoodcourts.app.Fragments.MenuFragment;
import com.kptech.purduefoodcourts.app.Fragments.TestFragment;
import com.kptech.purduefoodcourts.app.PurdueAPIParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kyle on 5/9/14.
 */
public class MenuPagerAdapater extends FragmentPagerAdapter {
    android.widget.ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private Activity context;
    String location;
    String mealType;
    public MenuPagerAdapater(FragmentManager fm) {
        super(fm);
    }

    public MenuPagerAdapater(FragmentManager fm, Activity a, String location, String mealType){
        super(fm);
        this.context = a;
        this.location = location;
        this.mealType = mealType;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("debug", "creating new test fragment position = "+position);
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
        TestFragment test = new TestFragment();
        Bundle args = new Bundle();
        args.putInt(test.ARG_OBJECT,position+1);
        args.putString("Location",location);
        args.putString("MealType",mealType);
        test.setArguments(args);
        return test;
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





}
