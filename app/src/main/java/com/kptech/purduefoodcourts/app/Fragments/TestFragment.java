package com.kptech.purduefoodcourts.app.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TestFragment extends android.support.v4.app.Fragment {
    public static final String ARG_OBJECT = "object";
    public static  final String KEY_BREAKFAST = "Breakfast";
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String type;





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
        return rootView;
    }




    public APIResponse getMenu(final String location, final String mealType){
        final ProgressDialog progress =  ProgressDialog.show(getActivity(),"","Downloading",true);
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
                } else if (mealType == "Dinner"){
                    response[0] = apiParser.getDinner();
                }


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });

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
