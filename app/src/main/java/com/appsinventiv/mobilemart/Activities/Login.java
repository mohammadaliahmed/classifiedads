package com.appsinventiv.mobilemart.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appsinventiv.mobilemart.Classes.PrefManager;
import com.appsinventiv.mobilemart.Model.User;
import com.appsinventiv.mobilemart.R;
import com.appsinventiv.mobilemart.Utils.CommonUtils;
import com.appsinventiv.mobilemart.Utils.Constants;
import com.appsinventiv.mobilemart.Utils.SharedPrefs;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText e_username, e_password;
    private PrefManager prefManager;
    ArrayList<String> userlist = new ArrayList<String>();
    String username, password;
    Button login;
    TextView register;
    String takeUserToActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register =  findViewById(R.id.register);
        login =  findViewById(R.id.login);
        e_username =  findViewById(R.id.username);
        e_password =  findViewById(R.id.password);
        Intent i=getIntent();
        takeUserToActivity=i.getStringExtra("takeUserToActivity");


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
            e_username.setError("Please enter username");
        } else if (e_password.getText().toString().length() == 0) {
            e_password.setError("Please enter your password");
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
        if(takeUserToActivity.equalsIgnoreCase(Constants.HOME_ACTIVITY)){
            startActivity(new Intent(Login.this, HomePage.class));

        }else if(takeUserToActivity.equalsIgnoreCase(Constants.SUBMIT_ACTIVITY)){
            startActivity(new Intent(Login.this, SubmitAd.class));
        }
        else if(takeUserToActivity.equalsIgnoreCase(Constants.MY_ACCOUNT_ACTIVITY)){
            startActivity(new Intent(Login.this, EditProfileInfo.class));
        }else if(takeUserToActivity.equalsIgnoreCase(Constants.MY_ADS_ACTIVITY)){
            startActivity(new Intent(Login.this, MyAds.class));
        }else if(takeUserToActivity.equalsIgnoreCase(Constants.MY_FAVOURITES_ACTIVITY)){
            startActivity(new Intent(Login.this, FavouriteAds.class));
        }
        finish();

    }
}
