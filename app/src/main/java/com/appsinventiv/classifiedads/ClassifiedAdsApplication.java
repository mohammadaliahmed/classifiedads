package com.appsinventiv.classifiedads;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.UserManager;

/**
 * Created by AliAh on 29/03/2018.
 */

public class ClassifiedAdsApplication  extends Application{
    private static ClassifiedAdsApplication instance;

    public SharedPreferences getPrefs() {
        return prefs;
    }

    private SharedPreferences prefs;

    public static ClassifiedAdsApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
