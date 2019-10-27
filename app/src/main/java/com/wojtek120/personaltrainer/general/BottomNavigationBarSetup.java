package com.wojtek120.personaltrainer.general;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.utils.BottomNavigationbarHelper;

public class BottomNavigationBarSetup {

    /**
     * setup and change bottom navigation bar animations
     */
    public void setupNavigationBar(BottomNavigationViewEx bottomNavigationViewEx, Context context, int activityNumber) {

        BottomNavigationbarHelper.changeBottomNavbarLook(bottomNavigationViewEx);
        BottomNavigationbarHelper.setupNavigationBetweenActivities(context, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(activityNumber);
        menuItem.setChecked(true);
    }

}
