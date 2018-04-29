package com.appsinventiv.classifiedads.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.appsinventiv.classifiedads.Adapter.ItemAdapter;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.SharedPrefs;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ItemAdapter adapter;
    ArrayList<AdDetails> itemList = new ArrayList<>();


    SwipeRefreshLayout mSwipeRefreshLayout;

    String city;
    DatabaseReference mDatabase;
    private ProgressBar pgsBar;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        pgsBar = (ProgressBar) findViewById(R.id.pBar);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent i = getIntent();

        category = i.getStringExtra("category");
        if (category != null) {
            MainActivity.this.setTitle(category);
        }


//        SubmitAd.fa.finish();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();
        recyclerView.setNestedScrollingEnabled(false);


        pgsBar.setVisibility(View.VISIBLE);


        loadData();

    }

    public void refreshItems() {
        itemList.clear();
        loadData();
        onItemsLoadComplete();
    }

    public void onItemsLoadComplete() {
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void loadData() {

        if (category == null || category.equalsIgnoreCase("") || category.equalsIgnoreCase("All Brands")) {
            mDatabase.child("ads").limitToLast(200).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot != null) {
                        AdDetails model = dataSnapshot.getValue(AdDetails.class);
                        pgsBar.setVisibility(View.GONE);
                        if (model != null) {
                            if (model.getAdStatus().equalsIgnoreCase("Active")) {
                                if (category == null || category.equalsIgnoreCase("") || category.equalsIgnoreCase("All Brands")) {
                                    itemList.add(model);
                                    Collections.sort(itemList, new Comparator<AdDetails>() {
                                        @Override
                                        public int compare(AdDetails listData, AdDetails t1) {
                                            Long ob1 = listData.getTime();
                                            Long ob2 = t1.getTime();

                                            return ob2.compareTo(ob1);

                                        }
                                    });


                                    adapter.notifyDataSetChanged();
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
        } else if (category != null || !category.equalsIgnoreCase(""))  {
            mDatabase.child("ads").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    AdDetails model = dataSnapshot.getValue(AdDetails.class);
                    pgsBar.setVisibility(View.GONE);
                    if (model != null) {
                        if (model.getMainCategory().equalsIgnoreCase(category)) {

                            itemList.add(model);
                            Collections.sort(itemList, new Comparator<AdDetails>() {
                                @Override
                                public int compare(AdDetails listData, AdDetails t1) {
                                    Long ob1 = listData.getTime();
                                    Long ob2 = t1.getTime();

                                    return ob2.compareTo(ob1);

                                }
                            });
                            pgsBar.setVisibility(View.GONE);

                            adapter.notifyDataSetChanged();
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



    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.add_ad) {

            if (SharedPrefs.getIsLoggedIn().equalsIgnoreCase("yes")) {
                Intent i = new Intent(MainActivity.this, SubmitAd.class);
                startActivity(i);
            } else {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
            }
        } else if (id == R.id.filters) {
            Intent i = new Intent(MainActivity.this, Filters.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
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

}
