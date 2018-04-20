package com.appsinventiv.classifiedads.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.classifiedads.Activities.AdPage;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.Model.PicturesModel;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by AliAh on 05/04/2018.
 */

public class AdStatusAdapter extends RecyclerView.Adapter<AdStatusAdapter.ViewHolder> {
    Context context;
    ArrayList<AdDetails> itemList;
    LayoutInflater layoutInflater;
    DatabaseReference mDatabase;
    StatusChanged statusChanged;

    public AdStatusAdapter(Context context, ArrayList<AdDetails> itemList,StatusChanged statusChanged) {
            mDatabase= FirebaseDatabase.getInstance().getReference();
            this.layoutInflater = LayoutInflater.from(context);
            this.context = context;
            this.itemList = itemList;
            this.statusChanged=statusChanged;


    }

    @NonNull
    @Override
    public AdStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.ad_status_layout, parent, false);
        AdStatusAdapter.ViewHolder viewHolder = new AdStatusAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdStatusAdapter.ViewHolder holder, final int position) {
        final AdDetails model = itemList.get(position);
        holder.title.setText(model.getTitle());
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formatedPrice = formatter.format(model.getPrice());
        holder.price.setText("Rs " + formatedPrice);
        holder.time.setText(CommonUtils.getFormattedDate( model.getTime()));

        if(model.getAdStatus().equalsIgnoreCase("Active")){
            holder.aSwitch.setChecked(true);
        }else if(model.getAdStatus().equalsIgnoreCase("Inactive")){
            holder.aSwitch.setChecked(false);
        }else{
//            holder.itemView.setVisibility(View.GONE);
//            itemList.remove(position);
        }

        mDatabase.child("ads").child(""+model.getTime()).child("pictures").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                    PicturesModel model1=childSnapshot.getValue(PicturesModel.class);
                    if(model1.getPosition()==0){
                        Glide.with(context).load(model1.getImageUrl()).into(holder.thumbnail);
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
                Intent i=new Intent(context,AdPage.class);
                i.putExtra("adId",""+model.getTime());
                context.startActivity(i);
            }
        });

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(statusChanged!=null){
                    if(b) {
                        model.setAdStatus("Active");
                        statusChanged.onStatusChanged(compoundButton, position, "" + model.getTime(), "Active");

                    }else{
                        model.setAdStatus("Inactive");
                        statusChanged.onStatusChanged(compoundButton, position, "" + model.getTime(), "Inactive");

                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price, time;
        public ImageView thumbnail;
        public Switch aSwitch;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleItem);
            price = itemView.findViewById(R.id.price);
            time = itemView.findViewById(R.id.time);
            aSwitch=itemView.findViewById(R.id.switch1);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }
    public interface StatusChanged{
        public void onStatusChanged(View v,int pos,String adId,String value);
    }
}
