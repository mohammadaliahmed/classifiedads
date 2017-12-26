package com.appsinventiv.classifiedads.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appsinventiv.classifiedads.R;
import com.bumptech.glide.Glide;

/**
 * Created by AliAh on 25/12/2017.
 */

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    int[] images={R.drawable.abcd,R.drawable.ab,R.drawable.abc};

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
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
                .load("https://firebasestorage.googleapis.com/v0/b/firestoreclassified.appspot.com/o/Photos%2F3fde666b2cf530de?alt=media&token=be9b456d-23bc-4715-99b9-731c2cba5f21")
                .into(imageView);
//        imageView.setImageResource(images[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
