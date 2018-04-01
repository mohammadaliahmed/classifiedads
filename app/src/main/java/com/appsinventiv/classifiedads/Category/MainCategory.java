package com.appsinventiv.classifiedads.Category;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.appsinventiv.classifiedads.Activities.MainActivity;
import com.appsinventiv.classifiedads.Adapter.CategoryAdapter;
import com.appsinventiv.classifiedads.Adapter.ItemAdapter;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.Model.CategoryItem;
import com.appsinventiv.classifiedads.R;

import java.util.ArrayList;
import java.util.List;

public class MainCategory extends AppCompatActivity {
    public static Activity fa;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CategoryAdapter adapter;
    List<CategoryItem> itemList = new ArrayList<CategoryItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);
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

        CategoryItem categoryItem = new CategoryItem("Samsung", R.drawable.car_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Apple", R.drawable.car_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("OPPO", R.drawable.car_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Lenovo", R.drawable.car_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Voice", R.drawable.car_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Xiomi", R.drawable.car_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Infinix", R.drawable.car_category);
        itemList.add(categoryItem);


        categoryItem = new CategoryItem("Nokia", R.drawable.car_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("LG", R.drawable.car_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Huawei", R.drawable.car_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Sony", R.drawable.car_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("HTC", R.drawable.car_category);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Motorola", R.drawable.car_category);
        itemList.add(categoryItem);


        categoryItem = new CategoryItem("Haier", R.drawable.car_category);
        itemList.add(categoryItem);


        categoryItem = new CategoryItem("BlackBerry", R.drawable.car_category);
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
