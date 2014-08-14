package com.kptech.purduefoodcourts.app.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.kptech.purduefoodcourts.app.Data.APIResponse;
import com.kptech.purduefoodcourts.app.Fragments.CourtGridFragment;
import com.kptech.purduefoodcourts.app.Interfaces.OnFoodItemsReceivedHandler;
import com.kptech.purduefoodcourts.app.Interfaces.OnMenuXmlReceivedHandler;
import com.kptech.purduefoodcourts.app.MainActivity;
import com.kptech.purduefoodcourts.app.PurdueAPIParser;
import com.kptech.purduefoodcourts.app.R;
import com.kptech.purduefoodcourts.app.tasks.GetMenuXmlTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;

/**
 * Created by kyle on 8/2/14.
 */
public class NotifyFavoritesService extends Service implements OnMenuXmlReceivedHandler {
    private NotificationManager mManager;
    private Set<String> favorites;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);

        mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(),CourtGridFragment.class);

        Notification notification = new Notification(R.drawable.ic_launcher,"This is a test message!", System.currentTimeMillis());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent;
        pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(this.getApplicationContext(), "AlarmManagerDemo", "This is a test message!", pendingNotificationIntent);
        mManager.notify(0, notification);

        checkForFavorites();
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    public void checkForFavorites(){
        String[] courts = new String[]{"Ford","Wiley","Windsor","Earhart","Hillenbrand"};
        for(String court: courts){
            getMenu(court);
        }

    }




    public void getMenu(final String location){


        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String date = "/"+df.format(Calendar.getInstance().getTime());
        String mUrl = "http://api.hfs.purdue.edu/menus/v1/locations/"+location.toLowerCase()+date+"/";
        Log.d("debug-url", mUrl);
        GetMenuXmlTask task = new GetMenuXmlTask(mUrl,location,this);
        task.execute();


    }

    public void createNotifcation(String item, String court, String mealType){
        String notifcationTitle = "A favorite is being served!";
        String notiicationText = item + " is being served at " + court + " for " + mealType;
        int notificationId = 001;
// Build intent for notification content
        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(notifcationTitle)
                        .setContentText(notiicationText)
                        .setContentIntent(viewPendingIntent);

// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());

    }


    @Override
    public void onMenuXmlReceived(String xmlResp,String location) {

        APIResponse[] responses = new APIResponse[3];
        PurdueAPIParser apiParser = null;
        try {
            apiParser = new PurdueAPIParser(xmlResp);
        } catch(Exception e) {
            e.printStackTrace();
        }

        responses[0] = apiParser.getBreakfast();
        responses[1] = apiParser.getLunch();
        responses[2] = apiParser.getDinner();


        SharedPreferences settings = getSharedPreferences("fav", 0);
        Set<String> favorites = settings.getStringSet("favorites",null);
        this.favorites = favorites;

        String[] courts = new String[]{"Ford","Earhart","Wiley","Windsor","Hillenbrand"};

        for(String c: courts){
            for(int i=0; i<responses.length; i++){
                APIResponse response = responses[i];
                Iterator it = response.getHash().entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry pairs = (Map.Entry)it.next();
                    //Log.d("NotifyFavService", pairs.getKey() + " = " + pairs.getValue());
                    ArrayList<String> foodItems = (ArrayList<String>) pairs.getValue();
                    for(String foodItem: foodItems) {
                        Log.d("debug-foodItem",foodItem);
                        for (String favorite : favorites) {
                            Log.d("debug-fav",favorite);
                            if (favorite.equals(foodItem)) {
                                String mealType = null;
                                if (i == 0) {
                                    mealType = "Breakfast";
                                } else if (i == 1) {
                                    mealType = "Lunch";
                                } else {
                                    mealType = "Dinner";
                                    Log.d("debug", "match");
                                }
                                createNotifcation(foodItem, location, mealType);
                            }
                        }
                    }
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
        }



    }
}

