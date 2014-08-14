package com.kptech.purduefoodcourts.app.tasks;

import android.os.AsyncTask;

import com.kptech.purduefoodcourts.app.Interfaces.OnMenuXmlReceivedHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by kyle on 8/13/14.
 */
public class GetMenuXmlTask extends AsyncTask<Void,Void,Void> {

    private String url;
    private String location;
    private OnMenuXmlReceivedHandler handler;

    public GetMenuXmlTask(String url, String location, OnMenuXmlReceivedHandler handler){
        this.url = url;
        this.location = location;
        this.handler = handler;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        String response = getXmlFormUrl();
        if(handler != null){
            handler.onMenuXmlReceived(response,location);
        }

        return null;
    }

    public String getXmlFormUrl(){
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
