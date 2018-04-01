package com.appsinventiv.classifiedads.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Classes.PrefManager;
import com.appsinventiv.classifiedads.Model.User;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.appsinventiv.classifiedads.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;

public class Register extends AppCompatActivity {
    Button login, signup;
    DatabaseReference mDatabase;
    private PrefManager prefManager;
    ArrayList<String> userslist = new ArrayList<String>();
    EditText e_fullname, e_username, e_email, e_password, e_phone, e_city;
    String fullname, username, email, password, phone, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

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
        login = (Button) findViewById(R.id.login);
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

                if (e_fullname.getText().toString().length() == 0) {
                    e_fullname.setError("Cannot be null");
                } else if (e_username.getText().toString().length() == 0) {
                    e_username.setError("Cannot be null");
                } else if (e_email.getText().toString().length() == 0) {
                    e_email.setError("Cannot be null");
                } else if (e_password.getText().toString().length() == 0) {
                    e_password.setError("Cannot be null");
                } else if (e_phone.getText().toString().length() == 0) {
                    e_phone.setError("Cannot be null");
                } else if (e_city.getText().toString().length() == 0) {
                    e_city.setError("Cannot be null");
                } else {
                    fullname = e_fullname.getText().toString();
                    username = e_username.getText().toString();
                    email = e_email.getText().toString();
                    password = e_password.getText().toString();
                    phone = e_phone.getText().toString();
                    city = e_city.getText().toString();

                    if (userslist.contains("" + username)) {
                        CommonUtils.showToast("Username is already taken\nPlease choose another");
                    } else {
                        int randomPIN = (int) (Math.random() * 900000) + 100000;
                        mDatabase
                                .child(username)
                                .setValue(new User(fullname, username, email, password, "" + phone, city, "no", "" + randomPIN, "no"))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        CommonUtils.showToast("Thank you for registering");
                                        SharedPrefs.setUsername(username);
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
            }
        });


    }


    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(Register.this, MainActivity.class));

        finish();
    }
}
