package com.wojtek120.personaltrainer.utils.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatePageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private HashMap<Fragment, Integer> fragmentsMap = new HashMap<>();
    private HashMap<String, Integer> fragmentsNumbersMap = new HashMap<>();
    private HashMap<Integer, String> fragmentsNamesMap = new HashMap<>();


    public StatePageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * Add new fragment
     * @param fragment - fragment to add
     * @param name - name of the fragment
     */
    public void addFragment(Fragment fragment, String name) {
        int number = fragments.size() - 1;

        fragments.add(fragment);
        fragmentsMap.put(fragment, number);
        fragmentsNumbersMap.put(name, number);
        fragmentsNamesMap.put(number, name);
    }

    /**
     * Returns fragment number by name
     * @param name - name of fragment which number should be returned
     * @return fragment number
     */
    public Integer getFragmentNumber(String name) {
        return fragmentsNumbersMap.get(name);
    }

    /**
     * Returns fragment number by Fragment object
     * @param fragment - fragment object which number should be returned
     * @return fragment number
     */
    public Integer getFragmentNumber(Fragment fragment) {
        return fragmentsMap.get(fragment);
    }

    /**
     * Returns fragment name by its number
     * @param fragmentNumber - fragment number which name should be returned
     * @return fragment name
     */
    public String getFragmentName(Integer fragmentNumber) {
        return fragmentsNamesMap.get(fragmentNumber);
    }
}
