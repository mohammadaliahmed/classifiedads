package com.appsinventiv.classifiedads.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appsinventiv.classifiedads.R;

public class SuccessPage extends AppCompatActivity {
    Button backtohome,postnew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_page);
        backtohome=(Button)findViewById(R.id.back);
        postnew=(Button)findViewById(R.id.postnew);
        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SuccessPage.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        postnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  i=new Intent(SuccessPage.this, SubmitAd.class);
                startActivity(i);
                finish();
            }
        });
    }
}
