package com.kptech.purduefoodcourts.app.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kptech.purduefoodcourts.app.Fragments.MenuFragment;
import com.kptech.purduefoodcourts.app.Fragments.TestFragment;

/**
 * Created by kyle on 5/9/14.
 */
public class MenuPagerAdapater extends FragmentPagerAdapter {
    public MenuPagerAdapater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        TestFragment test = new TestFragment();
        Bundle args = new Bundle();
        args.putInt(test.ARG_OBJECT,position+1);
        test.setArguments(args);
        return test;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
