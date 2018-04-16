package com.appsinventiv.classifiedads.Utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appsinventiv.classifiedads.Fragments.ActiveAds;
import com.appsinventiv.classifiedads.Fragments.PendingAds;


/**
 * Created by AliAh on 02/03/2018.
 */

public class SimpleFragmentPagerAdapter  extends FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ActiveAds();
        }
        else {
            return new PendingAds();
        }

//        else {
//            return new InActiveAds();
//        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 1:
                return "Pending Ads";
            case 0:
                return "Active Ads";

//            case 3:
//                return mContext.getString(R.string.category_nature);
            default:
                return null;
        }
    }

}
