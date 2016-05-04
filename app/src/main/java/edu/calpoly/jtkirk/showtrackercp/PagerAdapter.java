package edu.calpoly.jtkirk.showtrackercp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabWatching watchingTab = new TabWatching();
                return watchingTab;
            case 1:
                TabCompleted completedTab = new TabCompleted();
                return completedTab;
            case 2:
                TabToWatch toWatchTab = new TabToWatch();
                return toWatchTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}