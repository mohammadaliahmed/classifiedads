package com.appsinventiv.classifiedads.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Adapter.ItemAdapter;
import com.appsinventiv.classifiedads.Adapter.SearchItemsAdapter;
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
import java.util.List;

public class SearchResults extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ItemAdapter adapter;
    ArrayList<AdDetails> itemList = new ArrayList<>();

    DatabaseReference mDatabase;
    private ProgressBar pgsBar;
    String category, location, searchTerm;
    long maxPrice, minPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        SearchResults.this.setTitle("Search results");

        pgsBar = (ProgressBar) findViewById(R.id.pBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        pgsBar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(SearchResults.this, itemList);
        recyclerView.setAdapter(adapter);
//        Intent intent = getIntent();
        Bundle intent = getIntent().getExtras();
        searchTerm = intent.getString("searchTerm", "");
        maxPrice = intent.getLong("maxPrice", 9999999999L);
        minPrice = intent.getLong("minPrice", 0);
        location = intent.getString("location", "");
        category = intent.getString("category", "");
//        Toast.makeText(this, ""+category, Toast.LENGTH_SHORT).show();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("ads");
        loadData();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void loadData() {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    AdDetails model = dataSnapshot.getValue(AdDetails.class);
                    if (model != null) {
                        if (model.getAdStatus().equals("Active")) {

                            searchTerm = searchTerm.toLowerCase();
                            location = location.toLowerCase();
                            if (model.getCity().toLowerCase().equals(location)) {
                                if (model.getPrice() > minPrice && model.getPrice() < maxPrice) {
                                    if (model.getTitle().toLowerCase().contains(searchTerm)) {
                                        if (model.getMainCategory().equals(category)) {
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
                                        } else if (category == null || category.equals("All ads") || category.equals("")) {
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

            if(SharedPrefs.getIsLoggedIn().equals("yes")){
                Intent i = new Intent(SearchResults.this, SubmitAd.class);
                startActivity(i);
            }else {
                Intent i = new Intent(SearchResults.this, Login.class);
                startActivity(i);
            }
        } else if (id == R.id.filters) {
            Intent i = new Intent(SearchResults.this, Filters.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
