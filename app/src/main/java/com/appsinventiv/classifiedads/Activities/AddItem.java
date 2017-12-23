package com.appsinventiv.classifiedads.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appsinventiv.classifiedads.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddItem extends AppCompatActivity {
    EditText titleText;
    EditText descText;
    Button submitBtn;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        titleText = (EditText) findViewById(R.id.titleAdd);
        descText = (EditText) findViewById(R.id.descAdd);
        submitBtn = (Button) findViewById(R.id.buttonSubmit);

        db = FirebaseFirestore.getInstance();
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
        itemMap.put("time", FieldValue.serverTimestamp());

        db.collection("Item").add(itemMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(AddItem.this, "Posted", Toast.LENGTH_LONG).show();
                titleText.setText("");
                descText.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("errorPosting", e.getLocalizedMessage());
            }
        });

    }
}

