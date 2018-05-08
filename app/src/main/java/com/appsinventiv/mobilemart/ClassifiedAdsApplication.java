package com.appsinventiv.mobilemart;

import android.app.Application;

/**
 * Created by AliAh on 29/03/2018.
 */

public class ClassifiedAdsApplication  extends Application{
    private static ClassifiedAdsApplication instance;


    public static ClassifiedAdsApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
