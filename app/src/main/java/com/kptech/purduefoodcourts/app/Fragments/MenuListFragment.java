package com.kptech.purduefoodcourts.app.Fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.kptech.purduefoodcourts.app.Data.APIResponse;
import com.kptech.purduefoodcourts.app.PurdueAPIParser;
import com.kptech.purduefoodcourts.app.R;

import junit.framework.Test;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import android.support.v4.*;
import android.widget.TextView;

public class MenuListFragment extends android.support.v4.app.Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;




    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        Log.d("debug-create","testFragCreated");
        View rootView = inflater.inflate(
                R.layout.test_fragment, container, false);
        Bundle args = getArguments();
        String location = args.getString("Location");
        String mealType = args.getString("MealType");
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
        APIResponse r = getMenu(location,mealType);
        listAdapter = new com.kptech.purduefoodcourts.app.Adapters.ExpandableListAdapter(getActivity(),r.getList(),r.getHash());
        expListView.setAdapter(listAdapter);
        // Expand the list view by default
        int count = listAdapter.getGroupCount();
        for(int pos =1; pos<= count; pos++){
            expListView.expandGroup(pos-1);
        }


        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                View v = adapterView.getChildAt(i);
                TextView tv = (TextView) v.findViewById(R.id.lblListItem);
                final String foodItem = tv.getText().toString();


                /*
                      Dialog for long press on item to add to favorites
                 */
                new AlertDialog.Builder(getActivity())
                        .setTitle("Add Favorite")
                        .setMessage("Do you want to add " + foodItem + " to your favorites?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences settings = getActivity().getSharedPreferences("fav", 0);
                                Set<String> set = settings.getStringSet("favorites",null);
                                SharedPreferences.Editor editor = settings.edit();
                                if(set == null){
                                    Log.d("debug-fav", "set is null");
                                    Set<String> hSet = new HashSet<String>();
                                    List<String> favList = new ArrayList<String>();
                                    favList.add(foodItem);
                                    hSet.addAll(favList);

                                    editor.putStringSet("favorites",hSet);
                                } else {
                                    Log.d("debug-fav","set is not empty adding item");
                                    set.add(foodItem);
                                    editor.putStringSet("favorites",set);

                                }
                                editor.commit();
                                // continue with delete
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }
        });


        return rootView;
    }




    public APIResponse getMenu(final String location, final String mealType){
        final APIResponse[] response = new APIResponse[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                String date = "/"+df.format(Calendar.getInstance().getTime());
                String mUrl = "http://api.hfs.purdue.edu/menus/v1/locations/"+location.toLowerCase()+date+"/";
                Log.d("debug-url", mUrl);
                String xml = getXmlFormUrl(mUrl);
                PurdueAPIParser apiParser = null;
                try {
                     apiParser = new PurdueAPIParser(xml,getActivity());
                } catch(Exception e) {
                    e.printStackTrace();
                }
                if(mealType == "Breakfast"){
                    response[0] = apiParser.getBreakfast();
                } else if (mealType == "Lunch"){
                    response[0] = apiParser.getLunch();
                } else if (mealType == "Dinner") {
                    response[0] = apiParser.getDinner();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response[0];

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
