package com.appsinventiv.mobilemart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appsinventiv.mobilemart.Activities.ViewPictures;
import com.appsinventiv.mobilemart.Model.PicturesModel;
import com.appsinventiv.mobilemart.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by AliAh on 25/12/2017.
 */

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public static ArrayList<PicturesModel> pictures;


    public SliderAdapter(Context context,ArrayList<PicturesModel> pictures) {
        this.context = context;
        this.pictures=pictures;
    }



    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.product_slider,container,false);
        ImageView imageView=view.findViewById(R.id.slider_image);
        Glide.with(context)
                .load(pictures.get(position).getImageUrl())
                .into(imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, ViewPictures.class);
                context.startActivity(i);
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
