package com.appsinventiv.classifiedads.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.appsinventiv.classifiedads.ClassifiedAdsApplication;

/**
 * Created by AliAh on 29/03/2018.
 */

public class SharedPrefs {
    Context context;




    private SharedPrefs() {

    }

    public static String getUsername() {
        SharedPreferences pref;
        String username;
        pref = ClassifiedAdsApplication.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = pref.getString("username", "");
        return username;
    }

    public static void setUsername(String username) {
        SharedPreferences pref = ClassifiedAdsApplication.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", username);
        editor.apply();
    }
}
