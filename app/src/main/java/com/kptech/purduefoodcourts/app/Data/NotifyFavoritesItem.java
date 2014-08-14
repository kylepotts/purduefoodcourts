package com.kptech.purduefoodcourts.app.Data;

/**
 * Created by kyle on 8/14/14.
 */
public class NotifyFavoritesItem {

    private String item;
    private String location;
    private String mealType;

    public NotifyFavoritesItem(String item, String location, String mealType){
        this.item = item;
        this.location = location;
        this.mealType = mealType;

    }

    public String createNotifcationstring(){
        return location + " " + mealType + " " + item;
    }
}
