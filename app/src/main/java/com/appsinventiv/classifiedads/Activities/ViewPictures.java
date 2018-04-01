package com.appsinventiv.classifiedads.Activities;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsinventiv.classifiedads.Adapter.AdPicturesAdapter;
import com.appsinventiv.classifiedads.Adapter.SliderAdapter;
import com.appsinventiv.classifiedads.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ViewPictures extends AppCompatActivity {
    ViewPager mViewPager;
    AdPicturesAdapter adapter;

    ImageButton back;
    TextView backText;
    DotsIndicator dotsIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pictures);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backText = (TextView) findViewById(R.id.backext);

        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new AdPicturesAdapter(ViewPictures.this, SliderAdapter.pictures);
        mViewPager.setAdapter(adapter);
        dotsIndicator.setViewPager(mViewPager);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
