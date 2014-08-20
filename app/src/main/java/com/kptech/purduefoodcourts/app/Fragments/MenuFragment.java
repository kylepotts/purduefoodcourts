package com.kptech.purduefoodcourts.app.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kptech.purduefoodcourts.app.Activities.SettingsActivity;
import com.kptech.purduefoodcourts.app.Adapters.MenuPagerAdapater;
import com.kptech.purduefoodcourts.app.R;

import java.util.ArrayList;

/**
 * Created by kyle on 5/9/14.
 */
public class MenuFragment extends FragmentActivity implements ListView.OnItemClickListener  {
    MenuPagerAdapater menuPagerAdapater;
    ViewPager mViewPager;
    DrawerLayout drawerLayout;
    ListView listView;
    public static ProgressDialog progressDialog;


    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.menu_fragment);
        String location = getIntent().getExtras().getString("Location");

        progressDialog = new ProgressDialog(this);
        String message = "Fetching " + location + "'s Menu";
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
        // CourtGridFragment.progressDialog.dismiss();

        MenuPagerAdapater menuPagerAdapater = new MenuPagerAdapater(getSupportFragmentManager(),this, location);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(menuPagerAdapater);
        getActionBar().setTitle(location);


        // Initalize Drawer with Court Names and set onClick Listeners
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.left_drawer);
        ArrayList<String> drawerItems = new ArrayList<String>();
        drawerItems.add(" Ford");
        drawerItems.add(" Hillenbrand");
        drawerItems.add(" Wiley");
        drawerItems.add(" Windsor");
        drawerItems.add(" Earhart");
        drawerItems.add(" Favorites");
        listView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerItems));
        listView.setOnItemClickListener(this);




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



    public void selectItem(int position){

        String location = null;
        boolean isFavorites;
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
        if(position <=4) {
            Intent i = new Intent(this, MenuFragment.class);
            i.putExtra("Location", location);
            startActivity(i);
            finish();
        } else {
            Log.d("fav-debug","chose fav");
            Intent i = new Intent(this,FavoritesFragment.class);
            startActivity(i);


        }

    }

    // When an Items is Selected from the Drawer
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectItem(i);
    }
}

