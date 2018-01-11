package com.appsinventiv.classifiedads.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Adapter.SliderAdapter;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdPage extends AppCompatActivity {
    TextView title, price, time, city, description, views, username;
    FirebaseFirestore db;
    ViewPager mViewPager;
    ArrayList<String> picUrls = new ArrayList<String>();

    SliderAdapter adapter;
    long viewCount;
    String adId;
    ProgressDialog progressDialog;
    Button call, sms;
    String phoneNumber;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progressDialog = new ProgressDialog(AdPage.this);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        time = findViewById(R.id.time);
        city = findViewById(R.id.city);
        username = findViewById(R.id.username);

        description = findViewById(R.id.description);
        views = findViewById(R.id.views);
        call = findViewById(R.id.call);
        sms = findViewById(R.id.sms);


        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ads");

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        adId = intent.getStringExtra("adId");

        init(adId);
        initViewpager(adId);


    }

    public void init(String id) {
        mViewPager = findViewById(R.id.viewPager);
        adapter = new SliderAdapter(AdPage.this, picUrls);
        mViewPager.setAdapter(adapter);
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    AdDetails adDetails = dataSnapshot.getValue(AdDetails.class);
                    if(adDetails!=null){
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String formatedPrice = formatter.format(adDetails.getPrice());
                        title.setText(adDetails.getTitle());
                        price.setText("Rs " + formatedPrice);
                        time.setText(getFormattedDate(AdPage.this, adDetails.getTime()));
                        city.setText("" + adDetails.getCity());
                        description.setText("" + adDetails.getDescription());
                        username.setText(adDetails.getUsername());




                        for (DataSnapshot childSnapshot: dataSnapshot.child("pictures").getChildren()) {

                                                adapter.addUrls("" + childSnapshot.getValue());
                        }
                        viewCount = adDetails.getViews();
                        phoneNumber = adDetails.getPhone();
                        views.setText("Views: " + adDetails.getViews());
                        viewCount++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        db.collection("ads")
//                .document(id)
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot != null) {
//
//                    AdDetails adDetails = documentSnapshot.toObject(AdDetails.class);
//                    if (adDetails != null) {
//                        progressDialog.dismiss();
//                        DecimalFormat formatter = new DecimalFormat("#,###,###");
//                        String formatedPrice = formatter.format(adDetails.getPrice());
//                        title.setText(adDetails.getTitle());
//                        price.setText("Rs " + formatedPrice);
//                        time.setText(getFormattedDate(AdPage.this, adDetails.getTime()));
//                        city.setText("" + adDetails.getCity());
//                        description.setText("" + adDetails.getDescription());
//                        username.setText(adDetails.getUsername());
//
//                        viewCount = adDetails.getViews();
//                        phoneNumber = adDetails.getPhone();
//                        views.setText("Views: " + adDetails.getViews());
//                        viewCount++;
//                    }
//                }
//
//            }
//        });
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//
//
//                    }
//
//                });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(i);
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                startActivity(i);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateViews();
        finish();
    }

    public void initViewpager( String id) {







//        db.collection("ads")
//                .document(id)
//                .collection("pictures")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (final DocumentSnapshot document : task.getResult()) {
//                                final String documentId = document.getId();
////                                Toast.makeText(AdPage.this, ""+document.getId(), Toast.LENGTH_SHORT).show();
//                                db.collection("ads").document(id)
//                                        .collection("pictures")
//                                        .document(documentId)
//                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//
////                                               picUrls.add(""+documentSnapshot.getData())
//                                                Map<String, Object> pictureUrls = documentSnapshot.getData();
//                                                Map.Entry<String, Object> entry = pictureUrls.entrySet().iterator().next();
//                                                String key = entry.getKey();
//                                                String value = (String) entry.getValue();
////                                                picUrls.add(""+value);
//                                                adapter.addUrls("" + value);
//                                            }
//                                        });
//                            }
//                        }
//                    }
//                });
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

    public void updateViews() {

        mDatabase.child(adId).child("views").setValue(viewCount);


//        Map<String, Object> updates = new HashMap<>();
//        updates.put("views", viewCount);
//        db.collection("ads").document(adId).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                return;
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                return;
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            updateViews();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
