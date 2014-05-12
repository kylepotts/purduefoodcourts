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
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Activity a ;

    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.menu_fragment);
        getMenu();
        MenuPagerAdapater menuPagerAdapater = new MenuPagerAdapater(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        //mViewPager.setAdapter(menuPagerAdapater);


    }


    public void getMenu(){
        final ProgressDialog progress =  ProgressDialog.show(this,"","Downloading",true);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://api.hfs.purdue.edu/menus/v1/locations/ford/05-09-2014";
                String xml = getXmlFormUrl(url);
                PurdueAPIParser apiParser = null;
                try {
                    apiParser = new PurdueAPIParser(xml);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                APIResponse lunchResponse = apiParser.getLunch();
                listDataChild = lunchResponse.getHash();
                listDataHeader = lunchResponse.getList();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });

            }
        });
        thread.start();

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

