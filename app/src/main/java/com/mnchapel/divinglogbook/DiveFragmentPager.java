package com.mnchapel.divinglogbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class DiveFragmentPager extends FragmentStatePagerAdapter {

    // Number of tabs
    int numTabs;

    //
    Bundle args;



    /**
     *
     * @param fm
     * @param numTabs
     * @param args
     */
    public DiveFragmentPager(FragmentManager fm,
                             int numTabs,
                             Bundle args) {
        super(fm);
        this.numTabs = numTabs;
        this.args = args;
    }



    /**
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {

        Fragment tab;

        switch (position){
            case 0:
                tab = new DiveSummaryFragment();
                break;
            case 1:
                tab = new DiveChartFragment();
                break;
            case 2:
                tab = new DiveEquipmentFragment();
                break;
            case 3:
                tab = new DiveBioFragment();
                break;
            default:
                return null;
        }

        tab.setArguments(args);
        return tab;
    }



    /**
     * The number of tab
     * @return the number of tab
     */
    @Override
    public int getCount() {
        return numTabs;
    }
}
