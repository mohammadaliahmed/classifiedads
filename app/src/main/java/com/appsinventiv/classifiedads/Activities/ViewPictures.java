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

public class ViewPictures extends AppCompatActivity {
    ViewPager mViewPager;
    AdPicturesAdapter adapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    ImageButton back;
    TextView backText;


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
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        back=(ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backText=(TextView)findViewById(R.id.backext);

        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new AdPicturesAdapter(ViewPictures.this, SliderAdapter.pictures);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(onPageChangeListener);

        addBottomDots(0);

    }
ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        addBottomDots(position);

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
};
    private void addBottomDots(int currentPage) {
        dots = new TextView[SliderAdapter.pictures.size()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
