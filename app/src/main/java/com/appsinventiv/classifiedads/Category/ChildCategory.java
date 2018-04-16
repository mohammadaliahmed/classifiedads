package com.appsinventiv.classifiedads.Category;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Adapter.CategoryAdapter;
import com.appsinventiv.classifiedads.Model.CategoryItem;
import com.appsinventiv.classifiedads.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChildCategory extends AppCompatActivity {
    public static Activity fa;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CategoryAdapter adapter;
    List<CategoryItem> itemList = new ArrayList<CategoryItem>();
    String category = "child";
    int mainCategoryPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_category);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        fa = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CategoryAdapter(itemList, ChildCategory.this, ChildCategory.this, category);
        recyclerView.setAdapter(adapter);

        Bundle intent = getIntent().getExtras();
        mainCategoryPosition = intent.getInt("mainCategory", 0);

        if (mainCategoryPosition == 0) {

            CategoryItem categoryItem = new CategoryItem("S7", R.drawable.apple_category);
            itemList.add(categoryItem);
            categoryItem = new CategoryItem("S8", R.drawable.apple_category);
            itemList.add(categoryItem);
            categoryItem = new CategoryItem("S9", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("S6", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("S5", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("S4", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("S3", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("S2", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("A5", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("A7", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Note 5", R.drawable.apple_category);
            itemList.add(categoryItem);


            adapter.notifyDataSetChanged();
        } else if (mainCategoryPosition == 1) {
            CategoryItem categoryItem = new CategoryItem("Iphone X", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Iphone 8", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Iphone 7", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("Iphone 6", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Iphone 5", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Iphone 4", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Iphone 3", R.drawable.apple_category);
            itemList.add(categoryItem);

            adapter.notifyDataSetChanged();
        } else if (mainCategoryPosition == 2) {
            CategoryItem categoryItem = new CategoryItem("Motorcycle", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Scooter", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Spare Parts", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("Quad Bike", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Bicycles", R.drawable.apple_category);
            itemList.add(categoryItem);

            adapter.notifyDataSetChanged();
        } else if (mainCategoryPosition == 3) {
            CategoryItem categoryItem = new CategoryItem("Houses", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Apartments", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Plots", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Shops", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("Offices", R.drawable.apple_category);
            itemList.add(categoryItem);

            adapter.notifyDataSetChanged();
        } else if (mainCategoryPosition == 4) {
            CategoryItem categoryItem = new CategoryItem("Education", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Classes", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Repair", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Catering", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("Food", R.drawable.apple_category);
            itemList.add(categoryItem);

            adapter.notifyDataSetChanged();
        } else if (mainCategoryPosition == 5) {
            CategoryItem categoryItem = new CategoryItem("Camera", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Computer", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Games", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Tv", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("Kitchen", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Ups", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Other", R.drawable.apple_category);
            itemList.add(categoryItem);

            adapter.notifyDataSetChanged();
        } else if (mainCategoryPosition == 6) {
            CategoryItem categoryItem = new CategoryItem("Clothes", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Accessories", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Makeup", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Footwear", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Watches", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("Jewellery", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Hair Products", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Skin Products", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Other Products", R.drawable.apple_category);
            itemList.add(categoryItem);

            adapter.notifyDataSetChanged();
        } else if (mainCategoryPosition == 7) {
            CategoryItem categoryItem = new CategoryItem("Online", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Marketing", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Advertising", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Education", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("Sales", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Customer Service", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Networking", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("Human Resource", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Manufacturing", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Medical", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Part Time", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("Other Jobs", R.drawable.apple_category);
            itemList.add(categoryItem);


            adapter.notifyDataSetChanged();
        } else if (mainCategoryPosition == 8) {
            CategoryItem categoryItem = new CategoryItem("Clothes", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Toys", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Prams", R.drawable.apple_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Bikes", R.drawable.apple_category);
            itemList.add(categoryItem);


            categoryItem = new CategoryItem("Accessories", R.drawable.apple_category);
            itemList.add(categoryItem);

            adapter.notifyDataSetChanged();
        }


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
