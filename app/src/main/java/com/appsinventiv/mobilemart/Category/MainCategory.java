package com.appsinventiv.mobilemart.Category;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.appsinventiv.mobilemart.Adapter.CategoryAdapter;
import com.appsinventiv.mobilemart.Model.CategoryItem;
import com.appsinventiv.mobilemart.R;

import java.util.ArrayList;
import java.util.List;

public class MainCategory extends AppCompatActivity {
    public static Activity fa;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CategoryAdapter adapter;
    List<CategoryItem> itemList = new ArrayList<CategoryItem>();
    String intentFromFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);
        MainCategory.this.setTitle("Choose mobile brand ");

        fa = this;
        String category = "main";
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CategoryAdapter(itemList, MainCategory.this, MainCategory.this, category);
        recyclerView.setAdapter(adapter);


        Intent i = getIntent();
        intentFromFilters = i.getStringExtra("fromFilters");

        CategoryItem categoryItem;

        if (intentFromFilters != null) {
            categoryItem = new CategoryItem("All brands", R.drawable.all_ads_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Samsung", R.drawable.samsung_category);
            itemList.add(categoryItem);
        } else {
            categoryItem = new CategoryItem("Samsung", R.drawable.samsung_category);
            itemList.add(categoryItem);
        }
        categoryItem = new CategoryItem("Apple", R.drawable.apple_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("OPPO", R.drawable.oppo_category);
        itemList.add(categoryItem);



        categoryItem = new CategoryItem("Infinix", R.drawable.infinix_category);
        itemList.add(categoryItem);


        categoryItem = new CategoryItem("Nokia", R.drawable.nokia_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("LG", R.drawable.lg_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Huawei", R.drawable.huawei_category);
        itemList.add(categoryItem);


        categoryItem = new CategoryItem("HTC", R.drawable.htc_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Motorola", R.drawable.motorola_category);
        itemList.add(categoryItem);



        adapter.notifyDataSetChanged();


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
    }
}
