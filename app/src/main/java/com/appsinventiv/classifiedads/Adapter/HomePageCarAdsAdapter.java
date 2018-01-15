package com.appsinventiv.classifiedads.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.classifiedads.Activities.AdPage;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.Constants;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by AliAh on 11/01/2018.
 */

public class HomePageCarAdsAdapter extends RecyclerView.Adapter<HomePageCarAdsAdapter.ViewHolder> {
    List<AdDetails> carAds;
    Context context;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public HomePageCarAdsAdapter(Context context, List<AdDetails> carAds) {
        this.mInflater = LayoutInflater.from(context);
        this.carAds = carAds;
        this.context = context;

    }

    @Override
    public HomePageCarAdsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ad_card_layout_home, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(HomePageCarAdsAdapter.ViewHolder holder, int position) {

            final AdDetails model = carAds.get(position);
            final AdDetails adId = carAds.get(position);
            DecimalFormat formatter = new DecimalFormat("##,###,###");
            String formatedPrice = formatter.format(model.getPrice());
            holder.adTitleView.setText(model.getTitle());
            holder.adPriceView.setText("" + formatedPrice);
            Glide.with(context).load(model.getPicUrl()).into(holder.adImageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, AdPage.class);

                    i.putExtra("adId", "" + adId.getTime());
                    context.startActivity(i);
////
                }
            });

    }

    @Override
    public int getItemCount() {
       if(carAds==null){
           return 0;
       }else if(carAds.size()> Constants.HORIZONTAL_LIST_HOME_LIMIT){
           return Constants.HORIZONTAL_LIST_HOME_LIMIT;
       }else {
           return carAds.size();
       }
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView adTitleView, adPriceView;
        public ImageView adImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            adTitleView = itemView.findViewById(R.id.ad_title);
            adPriceView = itemView.findViewById(R.id.ad_price);
            adImageView = itemView.findViewById(R.id.ad_picture);
        }


    }


}
