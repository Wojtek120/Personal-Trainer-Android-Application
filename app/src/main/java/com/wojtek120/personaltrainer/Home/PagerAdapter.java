package com.wojtek120.personaltrainer.Home;

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
    private static final String TAG = "FragmentPagerAdapter";

    /** List with all fragments that are used in PagerAdapter class */
    private final List<Fragment> mFragmentList = new ArrayList<>();

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
        return mFragmentList.get(position);
    }

    /**
     * Returns count of all fragments
     * @return size of mFragmentList
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * Adds fragment to the list mFragmentList
     * @param fragment fragment to be added
     */
    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }
}
