package com.appsinventiv.classifiedads.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.appsinventiv.classifiedads.Adapter.HomeCategoryAdapter;
import com.appsinventiv.classifiedads.Adapter.HomePageCarsAdsAdapter;
import com.appsinventiv.classifiedads.Adapter.HomePageMobileAdsAdapter;
import com.appsinventiv.classifiedads.Classes.CategoryClass;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    HomePageMobileAdsAdapter adapterMobiles;
    HomePageCarsAdsAdapter adapterCars;
    HomeCategoryAdapter categoryAdapter;
    DatabaseReference mDatabase;

    ArrayList<AdDetails> mobilePhone;
    ArrayList<CategoryClass> categoryClassList;
    ArrayList<AdDetails> cars;
    ImageView bigAd;
    private ProgressBar pgsBar,pgsBar1;

    Button moreCars,moreMobiles;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        bigAd=(ImageView)findViewById(R.id.bigad);
        pgsBar = (ProgressBar) findViewById(R.id.pBar);
        pgsBar1 = (ProgressBar) findViewById(R.id.pBar1);

        pgsBar.setVisibility(View.VISIBLE);
        pgsBar1.setVisibility(View.VISIBLE);

        moreMobiles=(Button)findViewById(R.id.more_mobiles);
        moreCars =(Button)findViewById(R.id.more_cars);

        moreMobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        moreCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MainActivity.class);
                startActivity(intent);
            }
        });


        mDatabase= FirebaseDatabase.getInstance().getReference();

        initcategories();
        initMobileAds();
        initCarAds();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initcategories() {
        categoryClassList =new ArrayList<CategoryClass>();
        CategoryClass c=new CategoryClass("Mobiles",R.drawable.mobile_category);
        categoryClassList.add(c);

        c=new CategoryClass("Cars",R.drawable.car_category);
        categoryClassList.add(c);

        c=new CategoryClass("Bikes",R.drawable.bike_category);
        categoryClassList.add(c);

        c=new CategoryClass("Electronics",R.drawable.electronics_category);
        categoryClassList.add(c);

        c=new CategoryClass("Property",R.drawable.property_category);
        categoryClassList.add(c);

        c=new CategoryClass("Jobs",R.drawable.job_category);
        categoryClassList.add(c);

        c=new CategoryClass("Kids",R.drawable.kids_category);
        categoryClassList.add(c);

        c=new CategoryClass("Fashion",R.drawable.fashion_category);
        categoryClassList.add(c);

        c=new CategoryClass("Services",R.drawable.services_category);
        categoryClassList.add(c);



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.category_recyclerview);
//        LinearLayoutManager horizontalLayoutManagaer
//                = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
        int numberOfColumns = 3;

        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
//        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        categoryAdapter = new HomeCategoryAdapter(this, categoryClassList);
        recyclerView.setAdapter(categoryAdapter);
    }


    public void initMobileAds(){
        mobilePhone=new ArrayList<AdDetails>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ads_recyclerview_mobiles);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapterMobiles = new HomePageMobileAdsAdapter(this, mobilePhone);
        recyclerView.setAdapter(adapterMobiles);

        mDatabase.child("ads").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null){
                    AdDetails adDetails=dataSnapshot.getValue(AdDetails.class);
                    if(adDetails!=null){
                        mobilePhone.add(adDetails);

                        Collections.sort(mobilePhone, new Comparator<AdDetails>() {
                            @Override
                            public int compare(AdDetails listData, AdDetails t1) {
                                Long ob1 = listData.getViews();
                                Long ob2 = t1.getViews();

                                return ob2.compareTo(ob1);

                            }
                        });
                        pgsBar.setVisibility(View.GONE);

                        adapterMobiles.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void initCarAds(){
        cars=new ArrayList<AdDetails>();

        RecyclerView recyclerViewCars = (RecyclerView) findViewById(R.id.ads_recyclerview_cars);
        LinearLayoutManager horizontalLayoutManagaerCars
                = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCars.setLayoutManager(horizontalLayoutManagaerCars);
        adapterCars = new HomePageCarsAdsAdapter(this, cars);
        recyclerViewCars.setAdapter(adapterCars);
        mDatabase.child("ads").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null){
                    AdDetails adDetails=dataSnapshot.getValue(AdDetails.class);
                    if(adDetails!=null){
                        cars.add(adDetails);

                        Collections.sort(cars, new Comparator<AdDetails>() {
                            @Override
                            public int compare(AdDetails listData, AdDetails t1) {
                                Long ob1 = listData.getViews();
                                Long ob2 = t1.getViews();

                                return ob2.compareTo(ob1);

                            }
                        });
                        pgsBar1.setVisibility(View.GONE);

                        adapterCars.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
