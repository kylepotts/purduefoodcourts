package com.kptech.purduefoodcourts.app;

import android.app.Application;
import android.util.Log;

/**
 * Created by kyle on 8/27/14.
 */
public class AppClass extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("appclass","onCreate()");
        // Initialize the singletons so their instances
        // are bound to the application process.
       // initSingletons();
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d("appClass","terminate");
    }

    protected void initSingletons()
    {
        // Initialize the instance of MySingleton
        //MySingleton.initInstance();
    }

    public void customAppMethod()
    {
        // Custom application method
    }
}
