package com.kptech.purduefoodcourts.app.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.kptech.purduefoodcourts.app.data.APIResponse;
import com.kptech.purduefoodcourts.app.interfaces.OnFoodItemsReceivedHandler;
import com.kptech.purduefoodcourts.app.PurdueAPIParser;
import com.kptech.purduefoodcourts.app.R;
import com.kptech.purduefoodcourts.app.tasks.GetFoodMenuTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.widget.TextView;

public class MenuListFragment extends ListFragment implements OnFoodItemsReceivedHandler  {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    private Activity menuFragAct;




    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        Log.d("debug-create","MenuListFragCreated");
        View rootView = inflater.inflate(
                R.layout.test_fragment, container, false);
        Bundle args = getArguments();

        String location = args.getString("Location");
        String mealType = args.getString("MealType");
        String xml = args.getString("xml",null);




        expListView = (ExpandableListView) rootView.findViewById(android.R.id.list);

        // If we have an adapter, we need to set it
        if(xml != null){
            Log.d("menuListFrag","xml is not null");
            PurdueAPIParser purdueAPIParser = null;
            try {
                purdueAPIParser = new PurdueAPIParser(xml);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            APIResponse response;
            if(location.equals("Breakfast")){
                response = purdueAPIParser.getBreakfast();
            } else if (location.equals("Lunch")){
                response = purdueAPIParser.getLunch();
            } else {
                response = purdueAPIParser.getDinner();
            }
            final com.kptech.purduefoodcourts.app.adapters.ExpandableListAdapter expandableListAdapter = new com.kptech.purduefoodcourts.app.adapters.ExpandableListAdapter(getActivity(),response.getList(),response.getHash());
            listAdapter = expandableListAdapter;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    expListView.setAdapter(listAdapter);
                    // Expand the list view by default
                    int count = listAdapter.getGroupCount();
                    for(int pos =1; pos<= count; pos++) {
                        expListView.expandGroup(pos - 1);
                    }

                }
            });


        }
        GetFoodMenuTask task = null;
        if(xml != null) {

             task = new GetFoodMenuTask(getActivity(), location, mealType, false,this);
        } else {
            task =  new GetFoodMenuTask(getActivity(), location, mealType, true,this);
        }
        task.execute();


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
                                    List<String> favListCopy = new ArrayList<String>();
                                    Set<String> favoritesCopy = new HashSet<String>();
                                    for(String s: set){
                                        favListCopy.add(s);
                                    }
                                    favListCopy.add(foodItem);
                                    favoritesCopy.addAll(favListCopy);
                                    editor.putStringSet("favorites",favoritesCopy);

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





    @Override
    public void onFoodItemsReceived(APIResponse r) {



        listAdapter = new com.kptech.purduefoodcourts.app.adapters.ExpandableListAdapter(getActivity(),r.getList(),r.getHash());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                expListView.setAdapter(listAdapter);
                // Expand the list view by default
                int count = listAdapter.getGroupCount();
                for(int pos =1; pos<= count; pos++){
                    expListView.expandGroup(pos-1);
                }

            }
        });



    }

}
