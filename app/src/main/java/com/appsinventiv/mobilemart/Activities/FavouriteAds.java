package com.appsinventiv.mobilemart.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.appsinventiv.mobilemart.Adapter.ItemAdapter;
import com.appsinventiv.mobilemart.Model.AdDetails;
import com.appsinventiv.mobilemart.Model.User;
import com.appsinventiv.mobilemart.R;
import com.appsinventiv.mobilemart.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteAds extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ItemAdapter adapter;
    ArrayList<AdDetails> itemList = new ArrayList<>();
    ArrayList<String> userLikedAds = new ArrayList<>();

    DatabaseReference mDatabase;
    private ProgressBar pgsBar;

    @Override
    protected void onPause() {
        super.onPause();
        itemList.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_ads);
        pgsBar = findViewById(R.id.pBar);
        pgsBar.setVisibility(View.VISIBLE);


        FavouriteAds.this.setTitle("Favourite Ads");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();
        recyclerView.setNestedScrollingEnabled(false);



        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        loadData();
    }

    private void loadData() {
        mDatabase.child("users").child(SharedPrefs.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pgsBar.setVisibility(View.GONE);
                if(dataSnapshot!=null){
                    User user=dataSnapshot.getValue(User.class);
                    if(user!=null){
                        if(user.getFavourites()!=null){
                            userLikedAds=user.getFavourites();
                            for (String adId:userLikedAds) {
                                if(adId!=null) {
                                    getAdsFromDb(adId);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAdsFromDb(String adId) {
        mDatabase.child("ads").child(adId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    AdDetails adDetails=dataSnapshot.getValue(AdDetails.class);
                    if (adDetails!=null){
                        itemList.add(adDetails);
//                        Collections.sort(itemList, new Comparator<AdDetails>() {
//                            @Override
//                            public int compare(AdDetails listData, AdDetails t1) {
//                                Long ob1 = listData.getTime();
//                                Long ob2 = t1.getTime();
//
//                                return ob2.compareTo(ob1);
//
//                            }
//                        });
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            updateViews();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    }
