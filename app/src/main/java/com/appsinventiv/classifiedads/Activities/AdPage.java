package com.appsinventiv.classifiedads.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Adapter.AdPicturesAdapter;
import com.appsinventiv.classifiedads.Adapter.SliderAdapter;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.Model.PicturesModel;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.appsinventiv.classifiedads.Utils.SharedPrefs;
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
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdPage extends AppCompatActivity {
    TextView title, price, price1, date1, category, time, city, description, views, username,viewMore;
    FirebaseFirestore db;
    ViewPager mViewPager;
    public ArrayList<PicturesModel> picUrls = new ArrayList<>();

    SliderAdapter adapter;
    long viewCount;
    String adId;
    ProgressDialog progressDialog;
    LinearLayout call, sms;
    String phoneNumber;
    Button favouriteAd;
    DatabaseReference mDatabase;
    boolean liked = false;
    DotsIndicator dotsIndicator;
    ImageView back;
    String adBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);
        back = findViewById(R.id.back);

        progressDialog = new ProgressDialog(AdPage.this);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        time = findViewById(R.id.time);
        city = findViewById(R.id.city);
        username = findViewById(R.id.username);
        price1 = findViewById(R.id.price1);
        date1 = findViewById(R.id.date1);
        category = findViewById(R.id.category);
        favouriteAd=findViewById(R.id.favouriteAd);
        viewMore=findViewById(R.id.viewMoreAds);
        description = findViewById(R.id.description);
        views = findViewById(R.id.views);
        call = findViewById(R.id.call);
        sms = findViewById(R.id.sms);


        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        adId = intent.getStringExtra("adId");
        init(adId);

        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdPage.this,MoreAdsByUser.class);
                i.putExtra("adsBy",adBy);
                startActivity(i);
            }
        });

        favouriteAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = SharedPrefs.getUsername();
                mDatabase.child("users").child(username).child("likedAds").child(""+adId).setValue(adId).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Marked as favourite");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });





        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(31.488126, 74.265703, 1);
            String addresss = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }


//        CommonUtils.showToast(address);
//        username.setText(addresss);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void init(String id) {
        mViewPager = findViewById(R.id.viewPager);
        adapter = new SliderAdapter(AdPage.this, picUrls);
        mViewPager.setAdapter(adapter);
        dotsIndicator.setViewPager(mViewPager);


        mDatabase.child("ads").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    AdDetails adDetails = dataSnapshot.getValue(AdDetails.class);
                    if (adDetails != null) {
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String formatedPrice = formatter.format(adDetails.getPrice());
                        title.setText(adDetails.getTitle());
                        price.setText("Rs " + formatedPrice);
                        price1.setText("Rs " + formatedPrice);
                        time.setText(getFormattedDate(AdPage.this, adDetails.getTime()));
                        date1.setText(getFormattedDate(AdPage.this, adDetails.getTime()));
                        category.setText(adDetails.getMainCategory());

//                        city.setText("" + adDetails.getCity());
                        description.setText("" + adDetails.getDescription());
                        username.setText(adDetails.getUsername());
                        adBy=adDetails.getUsername();


                        for (DataSnapshot childSnapshot : dataSnapshot.child("pictures").getChildren()) {
                            PicturesModel model = childSnapshot.getValue(PicturesModel.class);
                            picUrls.add(model);
                            adapter.notifyDataSetChanged();
                        }


                        viewCount = adDetails.getViews();
                        phoneNumber = adDetails.getPhone();
                        views.setText("Views: " + (adDetails.getViews() + 1));
                        viewCount++;
                        updateViews();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
//        updateViews();

        finish();
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

        mDatabase.child("ads").child(adId).child("views").setValue(viewCount).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            updateViews();
            finish();
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            return true;
        } else if (id == R.id.action_report) {

            return true;
        } else if (id == R.id.action_favourite) {
//            R.id.action_favourite
            if (!liked) {
                item.setIcon(R.drawable.heart_fill);
                liked = true;

            } else {

                item.setIcon(R.drawable.heart_empty);
                liked = false;

            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_ad_page, menu);
//        menu.findItem(R.id.action_favourite).setIcon(R.drawable.heart_fill);
        return true;
    }


}
