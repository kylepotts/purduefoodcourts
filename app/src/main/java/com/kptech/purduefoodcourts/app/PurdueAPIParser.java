package com.kptech.purduefoodcourts.app;

import android.util.Log;

import com.kptech.purduefoodcourts.app.Data.APIResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kyle on 5/10/14.
 */

/*
<Menu xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
<Breakfast>
<MenuSection>
<Name>Legal Grease</Name>
<Items>
<MenuItem>
<Name>Scrambled Eggs</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Potato Gems</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Pancakes</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Smokey Links</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Potāto, Potäto</Name>
<Items>
<MenuItem>
<Name>Apple Cinnamon Oatmeal</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Pink Grapefruit Half</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Jasmine Rice</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Salami and Swiss</Name>
<Items>
<MenuItem>
<Name>Yogurt and Fruit Bar</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Mini Danish</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Cinnamon Coffeecake</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
</Breakfast>
<Lunch>
<MenuSection>
<Name>International Market</Name>
<Items>
<MenuItem>
<Name>Tempura Batter Vegetables</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Whole Green Beans</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Fried Rice</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Beef Vegetable Stir Fry</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Chicken and Seafood Noodle Soup</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Legal Grease</Name>
<Items>
<MenuItem>
<Name>Thincut Fries</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Turkey Burger</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Panko Breaded Fish</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Parmesan Tomato</Name>
<Items>
<MenuItem>
<Name>Sausage Pizza</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Loaded Potato Pizza</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Garlic Cheese Pizza</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Potāto, Potäto</Name>
<Items>
<MenuItem>
<Name>Corn</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Turkey Noodle Soup</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Black Bean Veg Fajitas</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Taco Bar</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Salami and Swiss</Name>
<Items>
<MenuItem>
<Name>Deli & Fresh Baked Breads</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Sugar Hill</Name>
<Items>
<MenuItem>
<Name>Apple Crisp</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Chocolate Pudding</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Peanut Butter Choc Chunk Cookie</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
</Lunch>
<Dinner>
<MenuSection>
<Name>International Market</Name>
<Items>
<MenuItem>
<Name>Tempura Batter Vegetables</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Beef Vegetable Stir Fry</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Whole Green Beans</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Fried Rice</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Chicken and Seafood Noodle Soup</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Legal Grease</Name>
<Items>
<MenuItem>
<Name>Hamburgers</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Thincut Fries</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Grilled Hot Dog</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Parmesan Tomato</Name>
<Items>
<MenuItem>
<Name>Garlic Cheese Pizza</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Meat Lover's Pizza</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Loaded Potato Pizza</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Potāto, Potäto</Name>
<Items>
<MenuItem>
<Name>Black Bean Veg Fajitas</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Turkey Noodle Soup</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Mexican Rice</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Corn</Name>
<IsVegetarian>true</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Taco Bar</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Salami and Swiss</Name>
<Items>
<MenuItem>
<Name>Deli & Fresh Baked Breads</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
<MenuSection>
<Name>Sugar Hill</Name>
<Items>
<MenuItem>
<Name>Apple Crisp</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Peanut Butter Choc Chunk Cookie</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
<MenuItem>
<Name>Chocolate Pudding</Name>
<IsVegetarian>false</IsVegetarian>
</MenuItem>
</Items>
</MenuSection>
</Dinner>
</Menu>
 */
public class PurdueAPIParser {
    private String xml;
    private JSONObject rootObject;
    List<String> listDataHeaderBreakfast;
    HashMap<String, List<String>> listDataChildBreakfast;
    List<String> listDataHeaderLunch;
    HashMap<String, List<String>> listDataChildLunch;
    List<String> listDataHeaderDinner;
    HashMap<String, List<String>> listDataChildDinner;

    public PurdueAPIParser(String xml) throws JSONException {
        this.xml = xml;
        rootObject = new JSONObject(xml);
    }


    public APIResponse getBreakfast(){
        JSONArray array = null;
        try {
             array = rootObject.getJSONArray("Breakfast");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getItems(array,"Breakfast");
        APIResponse response = new APIResponse(listDataHeaderBreakfast, listDataChildBreakfast);
        return response;
    }


    public APIResponse getLunch(){
        JSONArray array = null;
        try {
            array = rootObject.getJSONArray("Lunch");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getItems(array,"Lunch");
        APIResponse response = new APIResponse(listDataHeaderLunch,listDataChildLunch);
        return  response;
    }

    public APIResponse getDinner(){
        JSONArray array = null;
        try {
            array = rootObject.getJSONArray("Dinner");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getItems(array,"Dinner");
        APIResponse response = new APIResponse(listDataHeaderDinner,listDataChildDinner);
        return  response;
    }


    public void getItems(JSONArray array, String type){


        for(int i=0; i<array.length(); i++){
            JSONObject station = null;
            try {
                station = array.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String name = null;
            JSONArray items = null;
            try {
                name = station.getString("Name");
                items = station.getJSONArray("Items");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<String> foodItems = null;
            for(int j=0; i<items.length(); j++){
                JSONObject item = null;
                try {
                    item = items.getJSONObject(j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("debug-item",item.toString());

                String itemName = null;
                try {
                    itemName = item.getString("Name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("debug","adding item " + itemName);

                foodItems.add(itemName);
                if(type.equals("Breakfast")) {
                    listDataHeaderBreakfast.add(name);
                    String key = listDataHeaderBreakfast.get(i);
                    listDataChildBreakfast.put(key, foodItems);
                }

                if(type.equals("Lunch")){
                    listDataHeaderLunch.add(name);
                    String key = listDataHeaderLunch.get(i);
                    listDataChildLunch.put(key, foodItems);
                }

                if(type.equals("Dinner")){
                    listDataHeaderDinner.add(name);
                    String key = listDataHeaderDinner.get(i);
                    listDataChildDinner.put(key, foodItems);
                }
            }
        }


    }




}
