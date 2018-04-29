package com.appsinventiv.classifiedads.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.classifiedads.Adapter.HomeCategoryAdapter;
import com.appsinventiv.classifiedads.Adapter.HomepageAppleAdapter;
import com.appsinventiv.classifiedads.Adapter.HomepageSamsungAdapter;
import com.appsinventiv.classifiedads.Classes.CategoryClass;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.appsinventiv.classifiedads.Utils.Constants;
import com.appsinventiv.classifiedads.Utils.SharedPrefs;
import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    HomepageSamsungAdapter samsungAdapter;
    HomepageAppleAdapter appleAdapter;
    HomeCategoryAdapter categoryAdapter;
    DatabaseReference mDatabase;

    ArrayList<AdDetails> samsungAds;
    ArrayList<CategoryClass> categoryClassList;
    ArrayList<AdDetails> appleAds;
    ImageView bigAd;
    private ProgressBar pgsBar, pgsBar1;

    Button moreAppleAds, moreSamsungAds;
    RelativeLayout submitAd;
    String featuredAdUrl;
    ImageView featuredAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        HomePage.this.setTitle("");
        mDatabase = FirebaseDatabase.getInstance().getReference();


        bigAd = (ImageView) findViewById(R.id.bigad);
        pgsBar = (ProgressBar) findViewById(R.id.pBar);
        pgsBar1 = (ProgressBar) findViewById(R.id.pBar1);
        featuredAd = findViewById(R.id.bigad);

        pgsBar.setVisibility(View.VISIBLE);
        pgsBar1.setVisibility(View.VISIBLE);
        getPermissions();

        moreSamsungAds = (Button) findViewById(R.id.more1);
        moreAppleAds = (Button) findViewById(R.id.more2);


        submitAd = findViewById(R.id.submitAd);


        submitAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefs.getIsLoggedIn().equalsIgnoreCase("yes")) {
                    gotoSubmitAdActivity();
                } else {
                    gotoLoginActivity(Constants.SUBMIT_ACTIVITY);
                }


            }
        });
        moreSamsungAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                intent.putExtra("category", "Samsung");
                startActivity(intent);
            }
        });

        moreAppleAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                intent.putExtra("category", "Apple");

                startActivity(intent);
            }
        });


        if (SharedPrefs.getIsLoggedIn().equalsIgnoreCase("yes")) {
            mDatabase.child("users").child(SharedPrefs.getUsername()).child("fcmKey").setValue(SharedPrefs.getFcmKey());
        }

        if (!SharedPrefs.getFeaturedAd().equalsIgnoreCase("")) {
            Glide.with(HomePage.this).load(SharedPrefs.getFeaturedAd()).into(featuredAd);
        }

        initcategories();
        initSamsungAds();
        initAppleAds();
//        getSplashImage();
        getFeaturedAd();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.name_drawer);
        TextView navSubtitle = (TextView) headerView.findViewById(R.id.phone_drawer);
        if (SharedPrefs.getUsername().equalsIgnoreCase("")) {
            navSubtitle.setText("Welcome to Mobile Mart");

            navUsername.setText("Login or Signup");
            navUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoLoginActivity(Constants.HOME_ACTIVITY);
                }
            });
        } else {
            navSubtitle.setText(SharedPrefs.getUserCity());

            navUsername.setText(SharedPrefs.getUsername());
        }
    }

    private void gotoSubmitAdActivity() {
        Intent i = new Intent(HomePage.this, SubmitAd.class);
        startActivity(i);
    }

    private void gotoLoginActivity(String activityName) {
        Intent i = new Intent(HomePage.this, Login.class);
        i.putExtra("takeUserToActivity", activityName);
        startActivity(i);
    }

    private void getSplashImage() {
        mDatabase.child("splashImage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    SharedPrefs.setSplashImage("" + dataSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFeaturedAd() {
        mDatabase.child("featuredAd").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    SharedPrefs.setFeaturedAd("" + dataSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initcategories() {
        categoryClassList = new ArrayList<CategoryClass>();
        CategoryClass c = new CategoryClass("OPPO", R.drawable.oppo_category);
        categoryClassList.add(c);

        c = new CategoryClass("LG", R.drawable.lg_category);
        categoryClassList.add(c);

        c = new CategoryClass("Apple", R.drawable.apple_category);
        categoryClassList.add(c);


        c = new CategoryClass("Huawei", R.drawable.huawei_category);
        categoryClassList.add(c);

        c = new CategoryClass("Infinix", R.drawable.infinix_category);
        categoryClassList.add(c);

        c = new CategoryClass("Samsung", R.drawable.samsung_category);
        categoryClassList.add(c);


        c = new CategoryClass("Q Mobile", R.drawable.qmobile_category);
        categoryClassList.add(c);

        c = new CategoryClass("HTC", R.drawable.htc_category);
        categoryClassList.add(c);

        c = new CategoryClass("Nokia", R.drawable.nokia_category);
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


    public void initSamsungAds() {
        samsungAds = new ArrayList<AdDetails>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ads_recyclerview_mobiles);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        samsungAdapter = new HomepageSamsungAdapter(this, samsungAds);
        recyclerView.setAdapter(samsungAdapter);
        recyclerView.invalidate();
        recyclerView.setNestedScrollingEnabled(false);

        mDatabase.child("ads").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    AdDetails adDetails = dataSnapshot.getValue(AdDetails.class);
                    if (adDetails != null) {
                        if (adDetails.getAdStatus().equalsIgnoreCase("Active")) {

                            if (adDetails.getMainCategory().equalsIgnoreCase("Samsung")) {
                                if (adDetails.getCity().equalsIgnoreCase(SharedPrefs.getUserCity())) {


                                    samsungAds.add(adDetails);

                                    Collections.sort(samsungAds, new Comparator<AdDetails>() {
                                        @Override
                                        public int compare(AdDetails listData, AdDetails t1) {
                                            Long ob1 = listData.getViews();
                                            Long ob2 = t1.getViews();

                                            return ob2.compareTo(ob1);

                                        }
                                    });
                                    pgsBar.setVisibility(View.GONE);

                                    samsungAdapter.notifyDataSetChanged();

                                }
                            }
                        }
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


    public void initAppleAds() {
        appleAds = new ArrayList<AdDetails>();

        RecyclerView recyclerViewApple = (RecyclerView) findViewById(R.id.ads_recyclerview_apple);
        LinearLayoutManager horizontalLayoutManagaerCars
                = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewApple.setLayoutManager(horizontalLayoutManagaerCars);
        appleAdapter = new HomepageAppleAdapter(this, appleAds);
        recyclerViewApple.setAdapter(appleAdapter);
        recyclerViewApple.invalidate();
        recyclerViewApple.setNestedScrollingEnabled(false);
        mDatabase.child("ads").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot .exists()) {
                    AdDetails adDetails = dataSnapshot.getValue(AdDetails.class);
                    if (adDetails != null) {
                        if (adDetails.getAdStatus().equalsIgnoreCase("Active")) {

                            if (adDetails.getMainCategory().equalsIgnoreCase("Apple")) {
                                if (adDetails.getCity().equalsIgnoreCase(SharedPrefs.getUserCity())) {
                                    appleAds.add(adDetails);

                                    Collections.sort(appleAds, new Comparator<AdDetails>() {
                                        @Override
                                        public int compare(AdDetails listData, AdDetails t1) {
                                            Long ob1 = listData.getViews();
                                            Long ob2 = t1.getViews();

                                            return ob2.compareTo(ob1);

                                        }
                                    });
                                    pgsBar1.setVisibility(View.GONE);

                                    appleAdapter.notifyDataSetChanged();

                                }
                            }
                        }
                    }
                }else {
                    pgsBar1.setVisibility(View.GONE);

                    CommonUtils.showToast("No Data");
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        final ImageView app_logo = findViewById(R.id.app_logo);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app_logo.setVisibility(View.GONE);

            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                app_logo.setVisibility(View.VISIBLE);

                return false;
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomePage.this, SearchResults.class);
                intent.putExtra("searchTerm", query);
                intent.putExtra("minPrice", 0);
                intent.putExtra("maxPrice", 9999999L);
                intent.putExtra("location", "Lahore");
                intent.putExtra("category", "All Brands");
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                mAdapter.getFilter().filter(newText);
//                Toast.makeText(HomePage.this, ""+newText, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
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

        if (id == R.id.nav_browse_ads) {
            // Handle the camera action
            Intent i = new Intent(HomePage.this, MainActivity.class);
            i.putExtra("category", "All Ads");

            startActivity(i);
        } else if (id == R.id.nav_submit) {
            if (SharedPrefs.getIsLoggedIn().equalsIgnoreCase("yes")) {
                gotoSubmitAdActivity();
            } else {
                gotoLoginActivity(Constants.SUBMIT_ACTIVITY);
            }

        } else if (id == R.id.nav_my_ads) {
            if (SharedPrefs.getIsLoggedIn().equalsIgnoreCase("yes")) {
                Intent i = new Intent(HomePage.this, MyAds.class);
                startActivity(i);
            } else {
                gotoLoginActivity(Constants.MY_ADS_ACTIVITY);
            }

        } else if (id == R.id.nav_account) {
            if (SharedPrefs.getIsLoggedIn().equalsIgnoreCase("yes")) {
                Intent i = new Intent(HomePage.this, EditProfileInfo.class);
                startActivity(i);
            } else {
                gotoLoginActivity(Constants.MY_ACCOUNT_ACTIVITY);
            }
        }
//        else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

}
