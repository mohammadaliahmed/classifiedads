package com.appsinventiv.classifiedads.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Adapter.ItemAdapter;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.appsinventiv.classifiedads.Utils.SharedPrefs;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MoreAdsByUser extends AppCompatActivity {
    ArrayList<AdDetails> arrayList = new ArrayList<>();
    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_ads_by_user);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent  i=getIntent();
        final String adsByUser=i.getStringExtra("adsBy");
        this.setTitle("Ads by "+adsByUser);

        recyclerView = findViewById(R.id.recyclerview);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ItemAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

        mDatabase.child("ads").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null){
                    AdDetails model=dataSnapshot.getValue(AdDetails.class);
                    if(model!=null){
                        if(model.getUsername().equals(adsByUser)){
                            arrayList.add(model);
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
