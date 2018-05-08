package com.appsinventiv.mobilemart.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.mobilemart.Model.CustomerQueryModel;
import com.appsinventiv.mobilemart.Model.User;
import com.appsinventiv.mobilemart.R;
import com.appsinventiv.mobilemart.Utils.CommonUtils;
import com.appsinventiv.mobilemart.Utils.NotificationAsync;
import com.appsinventiv.mobilemart.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Help extends AppCompatActivity {
    EditText number, name, query;
    Button send;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Help");
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        query = findViewById(R.id.query);
        send = findViewById(R.id.send);


        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("users").child(SharedPrefs.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User user = dataSnapshot.getValue(User.class);
                    name.setText(user.getUsername());
                    number.setText(user.getPhone());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().length() == 0) {
                    name.setError("Please enter number");
                } else if (number.getText().length() == 0) {
                    number.setError("Please enter name");
                } else if (query.getText().length() == 0) {
                    query.setError("Please enter your message");
                } else {
                    long time = System.currentTimeMillis();
                    mDatabase.child("queries").push().setValue(new
                            CustomerQueryModel(name.getText().toString(),
                            number.getText().toString(),
                            query.getText().toString(), time
                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            NotificationAsync notificationAsync = new NotificationAsync(Help.this);
                            String NotificationTitle = "New query from " + SharedPrefs.getUsername();
                            String NotificationMessage = "Says: " + query.getText().toString();
                            notificationAsync.execute("ali", SharedPrefs.getadminFcmKey(), NotificationTitle, NotificationMessage);
                            CommonUtils.showToast("Query submitted");
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
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
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}
