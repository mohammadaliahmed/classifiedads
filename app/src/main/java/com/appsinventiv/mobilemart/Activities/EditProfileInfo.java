package com.appsinventiv.mobilemart.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.mobilemart.Model.User;
import com.appsinventiv.mobilemart.R;
import com.appsinventiv.mobilemart.Utils.CommonUtils;
import com.appsinventiv.mobilemart.Utils.GetAdAddress;
import com.appsinventiv.mobilemart.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileInfo extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText e_fullname, e_username, e_email, e_password, e_phone, e_city;
    Button update;

    String fullname, username, email, password, phone, city;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_info);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        EditProfileInfo.this.setTitle("My Account");
        e_fullname = (EditText) findViewById(R.id.name);
        e_username = (EditText) findViewById(R.id.username);
        e_email = (EditText) findViewById(R.id.email);
        e_password = (EditText) findViewById(R.id.password);
        e_phone = (EditText) findViewById(R.id.phone);
        e_city = (EditText) findViewById(R.id.city);
        mDatabase = FirebaseDatabase.getInstance().getReference();
          userId = SharedPrefs.getUsername();



        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
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
                } else {
                    fullname = e_fullname.getText().toString();
                    username = e_username.getText().toString();
                    email = e_email.getText().toString();
                    password = e_password.getText().toString();
                    phone = e_phone.getText().toString();

                    mDatabase.child("users").child(userId).child("email").setValue(e_email.getText().toString());
                    mDatabase.child("users").child(userId).child("phone").setValue(e_phone.getText().toString());
                    mDatabase.child("users").child(userId).child("password").setValue(e_password.getText().toString());
                    mDatabase.child("users").child(userId).child("name").setValue(e_fullname.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           CommonUtils.showToast("Profile updated!");
                           Intent i =new Intent(EditProfileInfo.this,HomePage.class);
                           startActivity(i);
                           finish();
                        }
                    });


                }
            }
        });


        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
//                        CommonUtils.showToast(""+user.getLatitude());
                        e_fullname.setText(user.getName());
                        e_username.setText(user.getUsername());
                        e_email.setText(user.getEmail());
                        e_phone.setText(user.getPhone());
                        e_password.setText(user.getPassword());

                        e_city.setText(GetAdAddress.getFullAddress(EditProfileInfo.this,user.getLatitude(),user.getLongitude()));


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            updateViews();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
