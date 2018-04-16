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
import com.appsinventiv.classifiedads.Model.PicturesModel;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.Constants;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AliAh on 13/01/2018.
 */

public class HomepageAppleAdapter extends RecyclerView.Adapter<HomepageAppleAdapter.ViewHolder>{

    List<AdDetails> mobileAds;
    Context context;
//    private List<String> adTitlesList = Collections.emptyList();
    private LayoutInflater mInflater;
    DatabaseReference mDatabase;

    // data is passed into the constructor
    public HomepageAppleAdapter(Context context, ArrayList<AdDetails> mobileAds) {
        this.mInflater = LayoutInflater.from(context);
        this.mobileAds = mobileAds;
        this.context=context;
        mDatabase= FirebaseDatabase.getInstance().getReference();

    }
    @Override
    public HomepageAppleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ad_card_layout_home, parent, false);
        HomepageAppleAdapter.ViewHolder viewHolder = new HomepageAppleAdapter.ViewHolder(view);
        return viewHolder;    }


    @Override
    public void onBindViewHolder(final HomepageAppleAdapter.ViewHolder holder, int position) {
        final AdDetails model = mobileAds.get(position);
        final AdDetails adId=mobileAds.get(position);
        DecimalFormat formatter = new DecimalFormat("##,###,###");
        String formatedPrice = formatter.format(model.getPrice());
        holder.adTitleView.setText(model.getTitle());
        holder.adPriceView.setText("Rs "+formatedPrice);
        mDatabase.child("ads").child(""+model.getTime()).child("pictures").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                    PicturesModel model1=childSnapshot.getValue(PicturesModel.class);
                    if(model1.getPosition()==0){
                        Glide.with(context).load(model1.getImageUrl()).into(holder.adImageView);
                    }else {
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(context,AdPage.class);

                i.putExtra("adId",""+adId.getTime());
                context.startActivity(i);
////
            }
        });


    }

    @Override
    public int getItemCount() {
        if(mobileAds==null){
            return 0;
        }else if(mobileAds.size()> Constants.HORIZONTAL_LIST_HOME_LIMIT){
            return Constants.HORIZONTAL_LIST_HOME_LIMIT;
        }else {
            return mobileAds.size();
        }
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        public View myView;
        public TextView adTitleView,adPriceView;
        public ImageView adImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            adTitleView = itemView.findViewById(R.id.ad_title);
            adPriceView = itemView.findViewById(R.id.ad_price);
            adImageView = itemView.findViewById(R.id.ad_picture);


        }


    }

    // convenience method for getting data at click position
    public AdDetails getItem(int id) {
        return mobileAds.get(id);
    }


}
