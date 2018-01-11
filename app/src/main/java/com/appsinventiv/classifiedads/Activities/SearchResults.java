package com.appsinventiv.classifiedads.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Adapter.SearchItemsAdapter;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchResults extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SearchItemsAdapter adapter;
    List<AdDetails> itemList = new ArrayList<>();
    FirebaseFirestore db;
    DocumentSnapshot lastVisible;

    String city="lahore";
    DatabaseReference mDatabase;
    SearchView searchView;
    private ProgressBar pgsBar;
    String category,location,searchTerm;
    long        maxPrice,minPrice;


    //    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        pgsBar = (ProgressBar) findViewById(R.id.pBar);
        pgsBar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchItemsAdapter(itemList, SearchResults.this,SearchResults.this, recyclerView);
        recyclerView.setAdapter(adapter);
//        Intent intent = getIntent();
        Bundle intent=getIntent().getExtras();
        searchTerm=intent.getString("searchTerm","");
        maxPrice=intent.getLong("maxPrice",9999999999L);
        minPrice=intent.getLong("minPrice",0);



        location=intent.getString("location","");





        db = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ads");
        loadData();
    }
    public void loadData(){
        itemList.clear();
        com.google.firebase.database.Query query=mDatabase.limitToLast(1000);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null){
                    AdDetails model=dataSnapshot.getValue(AdDetails.class);
                    if(model!=null) {
                        if (model.getPrice()>minPrice && model.getPrice()<maxPrice) {
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

//        CollectionReference collectionReference = db.collection("ads");
//        collectionReference
////       .whereEqualTo("city","abc")
//                .orderBy("price")
//                .whereGreaterThanOrEqualTo("price",0)
//                .whereLessThanOrEqualTo("price",600)
//                .orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
//
//
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                        for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
//                            AdDetails model = snapshot.toObject(AdDetails.class);
//                            itemList.add(model);
//                            adapter.notifyDataSetChanged();
//
//                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() -1);
//                        }
//                    }
//                });

//        collectionReference.whereEqualTo("city","jhu");
//        collectionReference
//        .addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
//                for (DocumentSnapshot snapshot : querySnapshot.getDocuments()){
//                    AdDetails model = snapshot.toObject(AdDetails.class);
//                    itemList.add(model);
//                    adapter.notifyDataSetChanged();
//
//                    lastVisible = querySnapshot.getDocuments().get(querySnapshot.size() -1);
//                }
//            }
//        });



//        Query query = collectionReference.whereEqualTo("city", "lahore");
//        query
//                .orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
//                .limit(50)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
//
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
}
