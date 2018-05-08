package com.appsinventiv.mobilemart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.mobilemart.Activities.MainActivity;
import com.appsinventiv.mobilemart.Classes.CategoryClass;
import com.appsinventiv.mobilemart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AliAh on 16/01/2018.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    List<CategoryClass> categoryClassList;
    Context context;
    //    private List<String> adTitlesList = Collections.emptyList();
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public HomeCategoryAdapter(Context context, ArrayList<CategoryClass> categoryClassList) {
        this.mInflater = LayoutInflater.from(context);
        this.categoryClassList = categoryClassList;
        this.context = context;
    }

    @Override
    public HomeCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.home_category_item_layout, parent, false);
        HomeCategoryAdapter.ViewHolder viewHolder = new HomeCategoryAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(HomeCategoryAdapter.ViewHolder holder, int position) {
        final CategoryClass model = categoryClassList.get(position);

        holder.categoryTitle.setText(model.getTitle());

//        Glide.with(context).load(model.getPicUrl()).into(holder.adImageView);
        holder.categoryIcon.setImageResource(model.getCategory_icon());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, ""+model.getTitle(), Toast.LENGTH_SHORT).show();
                Intent i =new Intent(context,MainActivity.class);
                i.putExtra("category",model.getTitle());

                context.startActivity(i);
////
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryClassList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryTitle;
        public ImageView categoryIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.category_title);
            categoryIcon=itemView.findViewById(R.id.category_icon);

        }


    }


}
