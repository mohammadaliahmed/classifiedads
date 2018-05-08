package com.appsinventiv.mobilemart.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsinventiv.mobilemart.Adapter.AdStatusAdapter;
import com.appsinventiv.mobilemart.Model.AdDetails;
import com.appsinventiv.mobilemart.R;
import com.appsinventiv.mobilemart.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ActiveAds extends Fragment {

    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    AdStatusAdapter adapter;
    ArrayList<AdDetails> adDetailsArrayList = new ArrayList<>();

    Context context;
    String fcmKey;

    ArrayList<String> adKeys = new ArrayList<>();

    public ActiveAds() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String username = SharedPrefs.getUsername();
        mDatabase.child("users").child(username).child("adsPosted").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String adId = childSnapshot.getValue(String.class);
                        adKeys.add(adId);
//                        Toast.makeText(context, ""+adId, Toast.LENGTH_SHORT).show();
                        loadAds(adId);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void loadAds(String id) {
        mDatabase.child("ads").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    AdDetails adDetails = dataSnapshot.getValue(AdDetails.class);
                    if (adDetails != null) {
                        if(adDetails.getAdStatus().equalsIgnoreCase("Active")|| adDetails.getAdStatus().equalsIgnoreCase("Inactive")) {
                            adDetailsArrayList.add(adDetails);
                            Collections.sort(adDetailsArrayList, new Comparator<AdDetails>() {
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_active_ads, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_active);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdStatusAdapter(context, adDetailsArrayList, new AdStatusAdapter.StatusChanged() {
            @Override
            public void onStatusChanged(View v, int pos, String adId, String value) {
                mDatabase.child("ads").child(adId).child("adStatus").setValue(value);
            }
        });
        recyclerView.setAdapter(adapter);

        return rootView;


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
