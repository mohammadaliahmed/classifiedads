package com.appsinventiv.classifiedads.Activities;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.appsinventiv.classifiedads.Model.ReportsModel;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportAd extends AppCompatActivity {
    String adTitle, adPicUrl;
    TextView title;
    ImageView img;
    CardView ad;
    String adId;
    RadioGroup options;
    RadioButton selected;
    Button submit;
    EditText et_description;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_ad);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent i = getIntent();
        adTitle = i.getStringExtra("adTtitle");
        adPicUrl = i.getStringExtra("adPicUrl");
        adId = i.getStringExtra("adId");

        this.setTitle("Report " + adTitle);

        img = findViewById(R.id.img);
        title = findViewById(R.id.title);
        ad = findViewById(R.id.ad);
        options = findViewById(R.id.options);
        submit = findViewById(R.id.submit);
        et_description = findViewById(R.id.description);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = options.getCheckedRadioButtonId();
                String description = et_description.getText().toString();
                selected = findViewById(selectedId);
                long time = System.currentTimeMillis();
                mDatabase.child("adsReported").push().setValue(new ReportsModel(adId, selected.getText().toString(), description + " ", time)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Reported Successfully");
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CommonUtils.showToast("There was some error");

                    }
                });
            }
        });


        title.setText(adTitle);
        Glide.with(this).load(adPicUrl).placeholder(R.drawable.placeholder).into(img);
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReportAd.this, AdPage.class);
                i.putExtra("adId", "" + adId);
                startActivity(i);
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
}
