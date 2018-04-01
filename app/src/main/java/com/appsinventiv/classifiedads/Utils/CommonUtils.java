package com.appsinventiv.classifiedads.Utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.appsinventiv.classifiedads.ClassifiedAdsApplication;

/**
 * Created by AliAh on 29/03/2018.
 */

public class CommonUtils {

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }
    public static void showToast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("WrongConstant")
            public void run() {
                Toast.makeText(ClassifiedAdsApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
