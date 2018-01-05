package com.appsinventiv.classifiedads.Category;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.appsinventiv.classifiedads.Activities.MainActivity;
import com.appsinventiv.classifiedads.Adapter.CategoryAdapter;
import com.appsinventiv.classifiedads.Adapter.ItemAdapter;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.Model.CategoryItem;
import com.appsinventiv.classifiedads.R;

import java.util.ArrayList;
import java.util.List;

public class MainCategory extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CategoryAdapter adapter;
    List<CategoryItem> itemList = new ArrayList<CategoryItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);
String category="main";

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CategoryAdapter(itemList,MainCategory.this,MainCategory.this,category);
        recyclerView.setAdapter(adapter);

        CategoryItem categoryItem=new CategoryItem("Vehicles");
        itemList.add(categoryItem);
        categoryItem=new CategoryItem("Mobile Phones");
        itemList.add(categoryItem);
        categoryItem=new CategoryItem("Job");
        itemList.add(categoryItem);

        adapter.notifyDataSetChanged();

    }
}
