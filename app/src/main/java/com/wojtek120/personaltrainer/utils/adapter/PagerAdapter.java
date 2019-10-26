package com.wojtek120.personaltrainer.utils.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Pager adapter for tabs
 * Here all fragments are stored
 */
public class PagerAdapter extends FragmentPagerAdapter {
    /** List with all fragments that are used in PagerAdapter class */
    private final List<Fragment> fragmentList = new ArrayList<>();

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    /**
     * Returns fragment from position
     * @param position position of fragment to be returned
     * @return fragment
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     * Returns count of all fragments
     * @return size of fragmentList
     */
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * Adds fragment to the list fragmentList
     * @param fragment fragment to be added
     */
    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }
}
