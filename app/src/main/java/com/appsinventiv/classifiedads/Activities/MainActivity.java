package com.appsinventiv.classifiedads.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Adapter.ItemAdapter;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.Model.Item;
import com.appsinventiv.classifiedads.Interface.OnLoadMoreListener;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ItemAdapter adapter;
    List<AdDetails> itemList = new ArrayList<>();

    FirebaseFirestore db;

    SwipeRefreshLayout mSwipeRefreshLayout;

    SharedPreferences userPref;
    String city;
    DatabaseReference mDatabase;
    private ProgressBar pgsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pgsBar = (ProgressBar) findViewById(R.id.pBar);


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
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(itemList, MainActivity.this, MainActivity.this, recyclerView);
        recyclerView.setAdapter(adapter);
        userPref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        city=userPref.getString("city","");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ads");
        db = FirebaseFirestore.getInstance();
        pgsBar.setVisibility(View.VISIBLE);
        loadData();
//
//        adapter.setOnLoadMore(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                recyclerView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        itemList.add(null);
//                        adapter.notifyItemInserted(itemList.size() - 1);
//                    }
//                });
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        loadMoreData();
//                    }
//                }, 2000);
//
//            }
//        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openAdd = new Intent(MainActivity.this, SubmitAd.class);
                startActivity(openAdd);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        com.google.firebase.database.Query query = mDatabase.orderByChild("time").limitToLast(100);
        query
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot != null) {
                            AdDetails model = dataSnapshot.getValue(AdDetails.class);
                            if (model != null) {
                                if (model.getCity().contains(city)) {

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


//        query = db.collection("ads");
//        query.whereEqualTo("city","lahore")
//
//
//                .orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
//                .limit(Constants.FIRESTORE_LIMIT)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
//                        Toast.makeText(MainActivity.this, ""+querySnapshot, Toast.LENGTH_SHORT).show();
//
//                        for (DocumentSnapshot snapshot : querySnapshot.getDocuments()){
//                            AdDetails model = snapshot.toObject(AdDetails.class);
//                            itemList.add(model);
//                            adapter.notifyDataSetChanged();
//
//                            lastVisible = querySnapshot.getDocuments().get(querySnapshot.size() -1);
//                        }
//                    }
//                });
    }

//    public void loadMoreData() {
//        com.google.firebase.firestore.Query next = FirebaseFirestore.getInstance()
//                .collection("ads")
//                .whereEqualTo("city", "lahore")
//                .orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
//                .startAfter(lastVisible)
//                .limit(Constants.FIRESTORE_LIMIT);
//        next.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(final QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
//                if (e == null) {
//
//                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
//
//                        AdDetails model = snapshot.toObject(AdDetails.class);
//                        itemList.add(model);
//                        adapter.notifyDataSetChanged();
//                        lastVisible = querySnapshot.getDocuments().get(querySnapshot.size() - 1);
//                        adapter.setIsLoading(false);
//
//                    }
//
//                    com.google.firebase.firestore.Query next = FirebaseFirestore.getInstance()
//                            .collection("ads")
//                            .whereEqualTo("city", "lahore")
//
//                            .orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING);
//                    next.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                        @Override
//                        public void onEvent(QuerySnapshot querySnapshot2, FirebaseFirestoreException e) {
//                            if (e == null) {
//
//                                prevItemVisible = querySnapshot2.getDocuments().get(querySnapshot2.size() - 1);
//
//                                if (prevItemVisible.getId().equals(lastVisible.getId())) {
//                                    adapter.isFullLoaded(true);
//                                }
//                            }
//                        }
//
//                    });
//
//
//                } else {
//                    Log.e("errorLoadMore", e.getLocalizedMessage());
//                }
//            }
//
//        });
//
//    }

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
        getMenuInflater().inflate(R.menu.main, menu);
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
        else if(id==R.id.filters){
            Intent i=new Intent(MainActivity.this, Filters.class);
            startActivity(i);
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
