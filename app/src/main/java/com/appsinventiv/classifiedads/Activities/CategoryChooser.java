package com.appsinventiv.classifiedads.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.appsinventiv.classifiedads.R;

public class CategoryChooser extends AppCompatActivity {
    private ListView lv;
    private String[] groupArray = {"Vehicles", "Mobile Phones", "Property"};
//    private String[][] childArray = {{"Test1", "Test2", "Test3"},
//            {"Video1", "Video2", "Video3"}, {"Audio1", "Audio2", "Audio3"}};

    private String[] list0 = {"Cars", "Bike", "Quad"};
    private String[] list1 = {"Accessories", "Brand New", "Used"};
    private String[] list2 = {"For sale", "Rent"};

    private String[] list3 = {"Accessories", "Brand New", "Used"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        lv = (ListView) findViewById(R.id.list);
        String[] data = getIntent().getStringArrayExtra("strArray");
        AdapterView.OnItemClickListener clickListener = null;

        // If no data received means this is the first activity
        if (data == null) {
            data = groupArray;
            clickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent intent = new Intent(CategoryChooser.this, CategoryChooser.class);
                    if(position==0) {
                        intent.putExtra("strArray", list0);
                        Toast.makeText(CategoryChooser.this, "list" + position, Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    }else if(position==1){
                        intent.putExtra("strArray", list1);
                        Toast.makeText(CategoryChooser.this, "list" + position, Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    }
                    else if(position==2){
                        intent.putExtra("strArray", list2);
                        Toast.makeText(CategoryChooser.this, "list" + position, Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    }
                }
            };

        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, data);

        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(clickListener);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
