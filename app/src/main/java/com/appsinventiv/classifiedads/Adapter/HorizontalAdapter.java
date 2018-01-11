package com.appsinventiv.classifiedads.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.classifiedads.Activities.MainActivity;
import com.appsinventiv.classifiedads.Model.Data;
import com.appsinventiv.classifiedads.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by AliAh on 06/01/2018.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


    List<Data> horizontalList = Collections.emptyList();
    Context context;


    public HorizontalAdapter(List<Data> horizontalList, Context context) {
        this.horizontalList = horizontalList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtview;
        public MyViewHolder(View view) {
            super(view);
            imageView=(ImageView) view.findViewById(R.id.imageview);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.picked_images, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.imageView.setImageResource(horizontalList.get(position).imageId);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

//                Toast.makeText(context, list, Toast.LENGTH_SHORT).show();


            }

        });

    }


    @Override
    public int getItemCount()
    {
        return horizontalList.size();
    }

}
