package com.appsinventiv.mobilemart.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.appsinventiv.mobilemart.Adapter.SliderAdapter;
import com.appsinventiv.mobilemart.Model.AdDetails;
import com.appsinventiv.mobilemart.Model.PicturesModel;
import com.appsinventiv.mobilemart.Model.User;
import com.appsinventiv.mobilemart.R;
import com.appsinventiv.mobilemart.Utils.CommonUtils;
import com.appsinventiv.mobilemart.Utils.GetAdAddress;
import com.appsinventiv.mobilemart.Utils.SharedPrefs;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdPage extends AppCompatActivity {
    TextView title, price, price1, date1, category, time, city, description, views, username, viewMore, location;
    ViewPager mViewPager;
    public ArrayList<PicturesModel> picUrls = new ArrayList<>();

    SliderAdapter adapter;
    long viewCount;
    String adId, adIdFromLink, adPicUrl;
    ProgressDialog progressDialog;
    LinearLayout call, sms, whatsapp;
    String phoneNumber;
    DatabaseReference mDatabase;
    DotsIndicator dotsIndicator;
    ImageView back;
    String adBy;
    ImageView ad;
    LikeButton likeButton;
    LinearLayout favourite, report, shareAd;
    ArrayList<String> userLikedAds = new ArrayList<>();
    AdDetails adModel;
    RelativeLayout wholeView;
    ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        onNewIntent(getIntent());
        ad = findViewById(R.id.ad);
        dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);
        back = findViewById(R.id.back);
        report = findViewById(R.id.report);
        wholeView=findViewById(R.id.wholeView);
        scrollView=findViewById(R.id.scrollView2);

        progressDialog = new ProgressDialog(AdPage.this);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        time = findViewById(R.id.time);
        city = findViewById(R.id.city);
        username = findViewById(R.id.username);
        price1 = findViewById(R.id.price1);
        date1 = findViewById(R.id.date1);
        category = findViewById(R.id.category);
        viewMore = findViewById(R.id.viewMoreAds);
        description = findViewById(R.id.description);
        views = findViewById(R.id.views);
        call = findViewById(R.id.call);
        sms = findViewById(R.id.sms);
        whatsapp = findViewById(R.id.whatsapp);
        location = findViewById(R.id.location);
        likeButton = findViewById(R.id.heart_button);
        favourite = findViewById(R.id.favourite);
        shareAd=findViewById(R.id.sharead);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        adId = intent.getStringExtra("adId");

        if (adId == null) {
            adId = adIdFromLink;
            init(adId);
        } else {
            init(adId);
        }

        shareAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "Please checkout this ad on Mobile Mart.\n\nhttp://mobilemart.pk/ad/"+adId);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Whatsapp");
                startActivity(Intent.createChooser(shareIntent, "Share ad via.."));
            }
        });


        Glide.with(this).load(SharedPrefs.getFeaturedAd()).into(ad);

        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdPage.this, MoreAdsByUser.class);
                i.putExtra("adsBy", adBy);
                startActivity(i);
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeButton.performClick();

            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdPage.this, ReportAd.class);
                i.putExtra("adTtitle", adModel.getTitle());
                i.putExtra("adId", adId);

                i.putExtra("adPicUrl", adPicUrl);

                startActivity(i);
            }
        });

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                userLikedAds.add(adId);
                mDatabase.child("users").child(SharedPrefs.getUsername()).child("favourites").setValue(userLikedAds).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Added to favourites");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                mDatabase.child("users").child(SharedPrefs.getUsername())
                        .child("favourites").child("" + userLikedAds.indexOf(adId)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userLikedAds.remove(adId);
                        CommonUtils.showToast("Removed from favourites");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getUserLikedAds();

    }

    private void getUserLikedAds() {
        mDatabase.child("users").child(SharedPrefs.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if (user.getFavourites() != null) {
                            userLikedAds = user.getFavourites();
                            if (user.getFavourites().contains(adId)) {
                                likeButton.setLiked(true);
                            } else {
                                likeButton.setLiked(false);

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


    public void init(String id) {
        mViewPager = findViewById(R.id.viewPager);
        adapter = new SliderAdapter(AdPage.this, picUrls);
        mViewPager.setAdapter(adapter);
        dotsIndicator.setViewPager(mViewPager);


        mDatabase.child("ads").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    wholeView.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    AdDetails adDetails = dataSnapshot.getValue(AdDetails.class);
                    if (adDetails != null) {
                        adModel = adDetails;
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String formatedPrice = formatter.format(adDetails.getPrice());
                        title.setText(adDetails.getTitle());
                        price.setText("Rs " + formatedPrice);
                        price1.setText("Rs " + formatedPrice);
                        time.setText(getFormattedDate(AdPage.this, adDetails.getTime()));
                        date1.setText(getFormattedDate(AdPage.this, adDetails.getTime()));
                        category.setText(adDetails.getMainCategory());

                        location.setText(GetAdAddress.getAddress(AdPage.this, adDetails.getLattitude(), adDetails.getLongitude()));
                        description.setText("" + adDetails.getDescription());
                        username.setText(adDetails.getUsername());
                        adBy = adDetails.getUsername();


                        for (DataSnapshot childSnapshot : dataSnapshot.child("pictures").getChildren()) {
                            PicturesModel model = childSnapshot.getValue(PicturesModel.class);
                            picUrls.add(model);
                            adapter.notifyDataSetChanged();
                            if (model.getPosition() == 0) {
                                adPicUrl = "" + model.getImageUrl();
                            }
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
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNumber.substring(0, 2).equalsIgnoreCase("03")) {
                    phoneNumber = "+92" + phoneNumber.substring(1);
                    String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                } else {
                    String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_ad_page, menu);
//        menu.findItem(R.id.action_favourite).setIcon(R.drawable.heart_fill);
        return true;
    }

    protected void onNewIntent(Intent intent) {

        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            adIdFromLink = data.substring(data.lastIndexOf("/") + 1);
            adId = adIdFromLink;
        }
    }


}
