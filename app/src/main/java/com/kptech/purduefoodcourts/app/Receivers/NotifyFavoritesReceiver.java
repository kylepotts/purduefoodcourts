package com.kptech.purduefoodcourts.app.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kptech.purduefoodcourts.app.Services.NotifyFavoritesService;

/**
 * Created by kyle on 8/2/14.
 */
public class NotifyFavoritesReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent service1 = new Intent(context, NotifyFavoritesService.class);
        context.startService(service1);

    }
}




