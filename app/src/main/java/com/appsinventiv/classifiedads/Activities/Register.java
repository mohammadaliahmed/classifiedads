package com.appsinventiv.classifiedads.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appsinventiv.classifiedads.Classes.PrefManager;
import com.appsinventiv.classifiedads.Manifest;
import com.appsinventiv.classifiedads.Model.User;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.appsinventiv.classifiedads.Utils.GetAdAddress;
import com.appsinventiv.classifiedads.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Register extends AppCompatActivity {
    Button signup;
    TextView login;
    DatabaseReference mDatabase;
    private PrefManager prefManager;
    ArrayList<String> userslist = new ArrayList<String>();
    EditText e_fullname, e_username, e_email, e_password, e_phone, e_city;
    String fullname, username, email, password, phone, city;
    Double longitude = 60.3323097, latitude = 30.0493247;
    int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        getPermissions();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");


        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userslist.add(dataSnapshot.getKey());
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

        e_fullname = (EditText) findViewById(R.id.name);
        e_username = (EditText) findViewById(R.id.username);
        e_email = (EditText) findViewById(R.id.email);
        e_password = (EditText) findViewById(R.id.password);
        e_phone = (EditText) findViewById(R.id.phone);
        e_city = (EditText) findViewById(R.id.city);


        signup = (Button) findViewById(R.id.signup);
        login = findViewById(R.id.signin);
        login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p == 1) {

                    if (e_fullname.getText().toString().length() == 0) {
                        e_fullname.setError("Please enter your name");
                    } else if (e_username.getText().toString().length() == 0) {
                        e_username.setError("Please enter username");
                    } else if (e_email.getText().toString().length() == 0) {
                        e_email.setError("Please enter your email");
                    } else if (e_password.getText().toString().length() == 0) {
                        e_password.setError("Please enter your password");
                    } else if (e_phone.getText().toString().length() == 0) {
                        e_phone.setError("Please enter your phone number");
                    }
                    else {
                        Intent intent = new Intent(Register.this, GPSTrackerActivity.class);
                        startActivityForResult(intent, 1);


                    }
                }else {
                    getPermissions();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

            if (data != null) {

                Bundle extras = data.getExtras();
                longitude = extras.getDouble("Longitude");
                latitude = extras.getDouble("Latitude");

                registerUser();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void registerUser() {
        fullname = e_fullname.getText().toString();
        username = e_username.getText().toString();
        email = e_email.getText().toString();
        password = e_password.getText().toString();
        phone = e_phone.getText().toString();
//        city = e_city.getText().toString();
            city = GetAdAddress.getCity(Register.this, latitude, longitude);

        if (userslist.contains("" + username)) {
            CommonUtils.showToast("Username is already taken\nPlease choose another");
        } else {
            long time = System.currentTimeMillis();
            int randomPIN = (int) (Math.random() * 900000) + 100000;
            username = username.trim();
            username = username.toLowerCase();
            ArrayList<String> favoruiteAds=new ArrayList<>();
            mDatabase
                    .child(username)
                    .setValue(new User(fullname, username, email, password, "" + phone, city, "no", "" + randomPIN, "no", SharedPrefs.getFcmKey(), latitude, longitude, time,favoruiteAds))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast("Thank you for registering");
                            SharedPrefs.setUsername(username);
                            SharedPrefs.setUserCity(city);
                            SharedPrefs.setIsLoggedIn("yes");
                            launchHomeScreen();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    CommonUtils.showToast("There was some error");
                }
            });
        }
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(Register.this, HomePage.class));

        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("permissions", "" + permissions[0]);
        if (permissions[0].equalsIgnoreCase("android.permission.ACCESS_FINE_LOCATION") && grantResults[0] == 0) {
            p = 1;
        } else {
            boolean isgranted = true;

//            getPermissions();
            boolean showRationale = false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                showRationale = shouldShowRequestPermissionRationale(permissions[0]);
            }
            if (!showRationale) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 1);
                isgranted = false;
            }
            CommonUtils.showToast("This app needs your location permission");
        }

    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {
                    p = 1;
                }
            }
        }
        return true;
    }

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }


}
