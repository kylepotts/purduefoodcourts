package com.kptech.purduefoodcourts.app.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kyle on 5/9/14.
 */
public class MenuFragment extends FragmentActivity implements ListView.OnItemClickListener  {
    MenuPagerAdapater menuPagerAdapater;
    ViewPager mViewPager;
    DrawerLayout drawerLayout;
    ListView listView;


    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.menu_fragment);
        String location = getIntent().getExtras().getString("Location");
        CourtGridFragment.progressDialog.dismiss();

        MenuPagerAdapater menuPagerAdapater = new MenuPagerAdapater(getSupportFragmentManager(),this, location);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(menuPagerAdapater);
        getActionBar().setTitle(location);


        // Initalize Drawer with Court Names and set onClick Listeners
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.left_drawer);
        ArrayList<String> courts = new ArrayList<String>();
        courts.add("Ford");
        courts.add("Hillenbrand");
        courts.add("Wiley");
        courts.add("Windsor");
        courts.add("Earhart");
        listView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, courts));
        listView.setOnItemClickListener(this);




    }

    public void selectItem(int position){

        String location = null;
        switch (position){
            case 0:
                location = "Ford";
                break;
            case 1:
                location = "Hillenbrand";
                break;
            case 2:
                location = "Wiley";
                break;
            case 3:
                location = "Windsor";
                break;
            case 4:
                location = "Earhart";
                break;

        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        String message = "Fetching " + location +"'s Menu";
        progressDialog.setMessage(message);
        progressDialog.show();
        Intent i = new Intent(this,MenuFragment.class);
        i.putExtra("Location",location);
        startActivity(i);
        finish();

    }

    // When an Items is Selected from the Drawer
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectItem(i);
    }
}

