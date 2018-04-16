package com.appsinventiv.classifiedads.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.classifiedads.AdObserver;
import com.appsinventiv.classifiedads.Adapter.SelectedImagesAdapter;
import com.appsinventiv.classifiedads.Category.MainCategory;
import com.appsinventiv.classifiedads.Classes.GifSizeFilter;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.Model.PicturesModel;
import com.appsinventiv.classifiedads.Model.SelectedAdImages;
import com.appsinventiv.classifiedads.Model.User;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.appsinventiv.classifiedads.Utils.CompressImage;
import com.appsinventiv.classifiedads.Utils.NotificationAsync;
import com.appsinventiv.classifiedads.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SubmitAd extends AppCompatActivity implements AdObserver {
    StorageReference mStorageRef;
    Button pickPicture, submitAd;
    EditText title, price, description;
    DatabaseReference mDatabase;

    ArrayList<String> imageUrl;
    Double longitude=60.3323097,latitude=30.0493247;

    SharedPreferences userPref;
    String username, city, phonenumber;

    List<Uri> mSelected;
    ArrayList<SelectedAdImages> selectedAdImages;
    SelectedImagesAdapter adapter;
    AdObserver adObserver;

    ArrayList<String> adCover;
    private static final int REQUEST_CODE_CHOOSE = 23;

    long time;
    EditText usernameField, phoneField, locationField;
    TextView chooseCategoryText;
    public static String mainCategory, childCategory;
    public static Activity fa;
    String adminFcmKey;

    @Override
    protected void onPostResume() {
        if (mainCategory == null) {
            chooseCategoryText.setText("Choose category");
        } else {
            if (childCategory != null) {
//                if (subChild != null) {
//                    chooseCategoryText.setText(mainCategory + " > " + childCategory + " > " + subChild);
//                } else {
                chooseCategoryText.setText(mainCategory + " > " + childCategory);
//                }
            } else {
                chooseCategoryText.setText(mainCategory);
            }
        }
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_ad);
        SubmitAd.this.setTitle("Submit a free Ad");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        adObserver=SubmitAd.this;

        getPermissions();

        fa = this;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    adminFcmKey = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        imageUrl = new ArrayList<String>();
        adCover = new ArrayList<String>();


        pickPicture = (Button) findViewById(R.id.pickpicture);
        submitAd = (Button) findViewById(R.id.submit);

        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.price);
        description = (EditText) findViewById(R.id.description);

        usernameField = (EditText) findViewById(R.id.username);
        phoneField = (EditText) findViewById(R.id.phone);
        locationField = (EditText) findViewById(R.id.location);

        chooseCategoryText = (TextView) findViewById(R.id.choose_category);


        chooseCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SubmitAd.this, MainCategory.class);
                startActivity(i);

            }
        });


        submitAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUrl.isEmpty()){
                    CommonUtils.showToast("Please select atleast one image");
                }
                else if (mainCategory == null) {
                    Toast.makeText(SubmitAd.this, "Please choose a category!", Toast.LENGTH_SHORT).show();

                } else if (title.getText().toString().length() == 0) {
                    title.setError("Cannot be null");

                } else if (price.getText().toString().length() == 0) {
                    price.setError("Cannot be null");
                } else if (description.getText().toString().length() == 0) {
                    description.setError("Cannot be null");
                } else {
                    Intent intent = new Intent(SubmitAd.this, GPSTrackerActivity.class);
                    startActivityForResult(intent, 1);
                }
//                submitAd();
            }
        });
        time = System.currentTimeMillis();

        username = SharedPrefs.getUsername();


        mDatabase.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User user = dataSnapshot.getValue(User.class);
                    usernameField.setText(user.getName());
                    phoneField.setText("" + user.getPhone());
                    locationField.setText(user.getCity());
                    username = user.getUsername();
                    phonenumber = user.getPhone();
                    city = user.getCity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        showPickedPictures();

        pickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAdImages.clear();
                Matisse.from(SubmitAd.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(5)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

    }

    private void getPermissions() {
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
    }

    private void showPickedPictures() {
        selectedAdImages = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(SubmitAd.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapter = new SelectedImagesAdapter(SubmitAd.this, selectedAdImages);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            adapter.notifyDataSetChanged();
            for (Uri img :
                    mSelected) {
                selectedAdImages.add(new SelectedAdImages("" + img));
                adapter.notifyDataSetChanged();
                CompressImage compressImage=new CompressImage(SubmitAd.this);
                imageUrl.add(compressImage.compressImage("" + img));
            }
        } if (requestCode == 1) {
            Bundle extras = data.getExtras();
             longitude  = extras.getDouble("Longitude");
             latitude = extras.getDouble("Latitude");
//            Toast.makeText(fa, longitude+"   "+latitude, Toast.LENGTH_SHORT).show();

            submitAd();

        }

        super.onActivityResult(requestCode, resultCode, data);

    }



    void submitAd() {



            String Adtitle = title.getText().toString(),
                    AdDescription = description.getText().toString();
            long AdPrice = Long.parseLong(price.getText().toString());

            String username = SharedPrefs.getUsername();
            mDatabase.child("users").child(username).child("adsPosted").child("" + time).setValue("" + time);


            mDatabase.child("ads").child("" + time).setValue(new AdDetails(Adtitle, AdDescription, username, "" + phonenumber, city, ""
                    , "Active", mainCategory, childCategory,
                    time, AdPrice, 0,latitude,longitude)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SubmitAd.this, "Error" + e, Toast.LENGTH_SHORT).show();
                }
            });

            int count = 0;
            for (String img : imageUrl) {

                putPictures(img, "" + time, count);
                count++;
                adObserver.onUploaded(count,imageUrl.size());

            }

            NotificationAsync notificationAsync = new NotificationAsync(SubmitAd.this);
            String NotificationTitle = "New Ad by " + username + " from " + city;
            String NotificationMessage = "Title: " + Adtitle + "   Price: " + AdPrice;
            notificationAsync.execute("ali", adminFcmKey, NotificationTitle, NotificationMessage);


            mainCategory = null;
//        subChild = null;
            childCategory = null;
//        finish();



    }

    public void putPictures(String path, final String key, final int count) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        ;
        Uri file = Uri.fromFile(new File(path));


        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        mDatabase.child("ads").child("" + time).child("pictures")
                                .push()
                                .setValue(new PicturesModel("" + downloadUrl, count));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(SubmitAd.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUploaded(int count,int arraySize) {
//        CommonUtils.showToast(count+"     "+arraySize);
        if(count==arraySize){
            Intent i=new Intent(SubmitAd.this,SuccessPage.class);
            startActivity(i);
            finish();
        }


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

