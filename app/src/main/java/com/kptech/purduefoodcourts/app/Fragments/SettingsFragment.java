package com.kptech.purduefoodcourts.app.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.kptech.purduefoodcourts.app.R;

/**
 * Created by kyle on 8/19/14.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs);
    }
}
