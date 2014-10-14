package com.dangchienhsgs.giffus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/* Adapter for the ViewGroup to make our tabs swipeable */
public class TabAdapter extends FragmentPagerAdapter {
    // List Fragment, include Home Fragment, Friend Fragment, Library Fragment
    List<Fragment> list;

    // Constructor
    public TabAdapter(FragmentManager fm) {
        super(fm);
        list =new ArrayList<Fragment>();
    }

    // add a fragment to list
    public void add(Fragment fragment){
        list.add(fragment);
    }

    // get a fragment with position i
    @Override
    public Fragment getItem(int i) {
        // 0: Home Fragment
        // 1: Friend Fragment
        // 2: LibraryFragment
        return list.get(i);
    }

    // get size of list
    @Override
    public int getCount() {
        return list.size();
    }
}
