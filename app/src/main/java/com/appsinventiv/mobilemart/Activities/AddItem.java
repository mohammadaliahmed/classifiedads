package com.appsinventiv.mobilemart.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.mobilemart.R;


import java.util.HashMap;
import java.util.Map;

public class AddItem extends AppCompatActivity {
    EditText titleText;
    EditText descText;
    Button submitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        titleText = (EditText) findViewById(R.id.titleAdd);
        descText = (EditText) findViewById(R.id.descAdd);
        submitBtn = (Button) findViewById(R.id.buttonSubmit);

        getSupportActionBar().setHomeButtonEnabled(true);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
    }

    public void addItem(){
        String title = titleText.getText().toString().trim();
        String desc = descText.getText().toString().trim();

        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("title", title);
        itemMap.put("desc", desc);



    }
}

