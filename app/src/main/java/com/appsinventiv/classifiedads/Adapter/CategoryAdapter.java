package com.appsinventiv.classifiedads.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Activities.MainActivity;
import com.appsinventiv.classifiedads.Category.ChildCategory;
import com.appsinventiv.classifiedads.Category.SubChild;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.Model.CategoryItem;
import com.appsinventiv.classifiedads.R;

import java.util.List;

/**
 * Created by AliAh on 04/01/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    List<CategoryItem> itemList;
    Activity activity;
    Context ctx;
    LayoutInflater mLInflater;
    String category;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.category_title);

        }
    }

    public CategoryAdapter(List<CategoryItem> itemList, Activity activity, Context ctx,String category) {
        this.itemList = itemList;
        this.activity = activity;
        this.ctx = ctx;
        this.category=category;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        CategoryItem categoryItem = itemList.get(position);
        holder.title.setText(categoryItem.getItemName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(category.equals("main")){
                        Intent i=new Intent(ctx, ChildCategory.class);

                        ctx.startActivity(i);

                    }else if(category.equals("child")){
                        Intent i=new Intent(ctx, SubChild.class);


                        ctx.startActivity(i);

                    }if(category.equals("subchild")){
                        Intent i=new Intent(ctx, MainActivity.class);

                        ctx.startActivity(i);

                    }
                }
            });

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
