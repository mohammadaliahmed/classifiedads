package com.appsinventiv.classifiedads.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Adapter.SliderAdapter;
import com.appsinventiv.classifiedads.Classes.MyDataClass;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdPage extends AppCompatActivity {
    TextView title, price, time;
    FirebaseFirestore db;
    ViewPager mViewPager;
    int layouts[]={R.drawable.ab,R.drawable.abc,R.drawable.abcd};
    ArrayList<String> picUrls = new ArrayList<String>();

    SliderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        time = findViewById(R.id.time);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);


        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String adId = intent.getStringExtra("adId");

        init(adId);
        initViewpager(adId);


    }

    public void init(String id) {
        db.collection("ads")
                .document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        if (documentSnapshot != null) {
                            AdDetails adDetails = documentSnapshot.toObject(AdDetails.class);
                            if (adDetails != null) {
                                title.setText(adDetails.getTitle());
                                price.setText("" + adDetails.getPrice());
                                time.setText(getFormattedDate(AdPage.this, adDetails.getTime()));
                            }
                        }
                    }
                });
    }

    public void initViewpager(final String id) {


        mViewPager=findViewById(R.id.viewPager);
        adapter=new SliderAdapter(AdPage.this,picUrls);
        mViewPager.setAdapter(adapter);

        db.collection("ads")
                .document(id)
                .collection("pictures")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (final DocumentSnapshot document : task.getResult()) {
                                final String documentId=document.getId();
//                                Toast.makeText(AdPage.this, ""+document.getId(), Toast.LENGTH_SHORT).show();
                                db.collection("ads").document(id)
                                        .collection("pictures")
                                        .document(documentId)
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

//                                               picUrls.add(""+documentSnapshot.getData())
                                                Map<String, Object> pictureUrls = documentSnapshot.getData();
                                                Map.Entry<String,Object> entry = pictureUrls.entrySet().iterator().next();
                                                String key= entry.getKey();
                                                String value= (String) entry.getValue();
//                                                picUrls.add(""+value);
                                                adapter.addUrls(""+value);
//                                                Toast.makeText(AdPage.this, ""+value, Toast.LENGTH_SHORT).show();
                                            }
                                        });
//                                adapter.setPictures(picUrls);


                            }




                        }
                    }
                });


    }

    public String getFormattedDate(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd MMM ";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday ";
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("dd MMM , h:mm aa", smsTime).toString();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
