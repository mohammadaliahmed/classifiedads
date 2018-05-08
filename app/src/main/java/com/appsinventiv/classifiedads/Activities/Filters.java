package com.appsinventiv.classifiedads.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Category.MainCategory;
import com.appsinventiv.classifiedads.R;

public class Filters extends AppCompatActivity {
    Button search;
    EditText keyword, min, max;
    String location;
    TextView chooseCategory;
    public static String mainCategory, childCategory;


    @Override
    protected void onPostResume() {
        if (mainCategory == null) {
            chooseCategory.setText("Choose mobile brand");
        } else {
            if (childCategory != null) {
//                if (subChild != null) {
//                    chooseCategoryText.setText(mainCategory + " > " + childCategory + " > " + subChild);
//                } else {
                chooseCategory.setText(mainCategory + " > " + childCategory);
//                }
            } else {
                chooseCategory.setText(mainCategory);
            }
        }
        super.onPostResume();
    }


    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        search = (Button) findViewById(R.id.search);
        keyword = (EditText) findViewById(R.id.keyword);
        min = (EditText) findViewById(R.id.minprice);
        max = (EditText) findViewById(R.id.maxprice);
        chooseCategory = findViewById(R.id.choose_category);


        chooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Filters.this, MainCategory.class);
                i.putExtra("fromFilters", "abc");
                startActivity(i);
            }
        });

        final String[] items = new String[]{"Select one", "Lahore", "Islamabad", "Karachi", "Faisalabad", "Peshawar", "Multan"};
        Spinner spinner = (Spinner) findViewById(R.id.locationchoose);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//                items[0] = "Multan";
//                 items[position];
                location = items[position];
//                Toast.makeText(Filters.this, ""+items[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long minPrice = 0;
                long maxPrice = 99999999L;
                String searchTerm;
                if (min.getText().toString().length() == 0 && max.getText().toString().length() == 0) {

                } else if (min.getText().toString().length() > 0 && max.getText().toString().length() == 0) {
                    minPrice = Long.parseLong(min.getText().toString());


                } else if (min.getText().toString().length() == 0 && max.getText().toString().length() > 0) {
                    maxPrice = Long.parseLong(max.getText().toString());

                } else {
                    minPrice = Long.parseLong(min.getText().toString());
                    maxPrice = Long.parseLong(max.getText().toString());
                }

                searchTerm = keyword.getText().toString();
                Intent intent = new Intent(Filters.this, SearchResults.class);
                intent.putExtra("searchTerm", searchTerm);
                intent.putExtra("minPrice", minPrice);
                intent.putExtra("maxPrice", maxPrice);
                intent.putExtra("location", location);
                intent.putExtra("category", mainCategory);

                startActivity(intent);


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
