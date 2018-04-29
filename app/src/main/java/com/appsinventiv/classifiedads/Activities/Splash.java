package com.appsinventiv.classifiedads.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.appsinventiv.classifiedads.Utils.SharedPrefs;
import com.bumptech.glide.Glide;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    ImageView splashImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashImage=findViewById(R.id.splashImage);


//        if(SharedPrefs.getSplashImage()==null||SharedPrefs.getSplashImage().equals("")){
//            CommonUtils.showToast("here");
//            Glide.with(this).load(R.drawable.main_logo).into(splashImage);
//
//        }else {
//            CommonUtils.showToast("here2");
//
//            Glide.with(this).load(SharedPrefs.getSplashImage()).into(splashImage);
//        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splash.this, HomePage.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
