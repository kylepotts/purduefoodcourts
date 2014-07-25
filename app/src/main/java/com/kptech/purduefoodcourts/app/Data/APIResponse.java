package com.kptech.purduefoodcourts.app.Data;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kyle on 5/10/14.
 */
public class APIResponse {
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    // "Food Area Name / EG Legal Grease " -> <List of items at that area>

    public APIResponse(List<String> list, HashMap<String, List<String>> hash){
        this.listDataHeader = list;
        this.listDataChild = hash;
    }

    public List<String>getList(){
        return this.listDataHeader;
    }

    public HashMap<String, List<String>> getHash(){
        return listDataChild;
    }
}
