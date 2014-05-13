package com.kptech.purduefoodcourts.app.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.kptech.purduefoodcourts.app.Adapters.MenuPagerAdapater;
import com.kptech.purduefoodcourts.app.Data.APIResponse;
import com.kptech.purduefoodcourts.app.PurdueAPIParser;
import com.kptech.purduefoodcourts.app.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kyle on 5/9/14.
 */
public class MenuFragment extends FragmentActivity {
    MenuPagerAdapater menuPagerAdapater;
    ViewPager mViewPager;
    public static  final String KEY_BREAKFAST = "Breakfast";


    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.menu_fragment);
        //getMenu();
        String location = getIntent().getExtras().getString("Location");
        String mealType = getIntent().getExtras().getString("MealType");
        MenuPagerAdapater menuPagerAdapater = new MenuPagerAdapater(getSupportFragmentManager(),this, location, mealType);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(menuPagerAdapater);


    }



}

