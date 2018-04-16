package com.appsinventiv.classifiedads.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Category.MainCategory;
import com.appsinventiv.classifiedads.Classes.PrefManager;
import com.appsinventiv.classifiedads.Model.User;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.appsinventiv.classifiedads.Utils.SharedPrefs;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class Login extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText e_username, e_password;
    private PrefManager prefManager;
    ArrayList<String> userlist = new ArrayList<String>();
    String username, password;
    Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        e_username = (EditText) findViewById(R.id.username);
        e_password = (EditText) findViewById(R.id.password);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userlist.add(dataSnapshot.getKey());
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

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
                finish();

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }

    private void userLogin() {

        if (e_username.getText().toString().length() == 0) {
            e_username.setError("Cannot be null");
        } else if (e_password.getText().toString().length() == 0) {
            e_password.setError("Cannot be null");
        } else {
            username = e_username.getText().toString();
            password = e_password.getText().toString();
            if (userlist.contains(username)) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            User user = dataSnapshot.child("" + username).getValue(User.class);
                            if (user != null) {
                                if (user.getPassword().equals(password)) {
                                    SharedPrefs.setUsername(user.getUsername());
                                    SharedPrefs.setUserCity(user.getCity());
                                    SharedPrefs.setIsLoggedIn("yes");
                                    launchHomeScreen();
                                } else {
                                    CommonUtils.showToast("Wrong password\nPlease try again");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else {
                CommonUtils.showToast("Username does not exist\nPlease Sign up");

            }
        }

    }


    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(Login.this, HomePage.class));
        finish();
    }
}
