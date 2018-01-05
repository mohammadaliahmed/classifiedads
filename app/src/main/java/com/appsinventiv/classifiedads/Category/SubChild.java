package com.appsinventiv.classifiedads.Category;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.appsinventiv.classifiedads.Adapter.CategoryAdapter;
import com.appsinventiv.classifiedads.Model.CategoryItem;
import com.appsinventiv.classifiedads.R;

import java.util.ArrayList;
import java.util.List;

public class SubChild extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CategoryAdapter adapter;
    List<CategoryItem> itemList = new ArrayList<CategoryItem>();
    String category="subchild";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_child);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CategoryAdapter(itemList,SubChild.this,SubChild.this,category);
        recyclerView.setAdapter(adapter);

        CategoryItem categoryItem=new CategoryItem("Toyota");
        itemList.add(categoryItem);
        categoryItem=new CategoryItem("Suzuki");
        itemList.add(categoryItem);
        categoryItem=new CategoryItem("Honda");
        itemList.add(categoryItem);

        adapter.notifyDataSetChanged();
    }
}
