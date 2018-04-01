package com.appsinventiv.classifiedads.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.appsinventiv.classifiedads.Activities.AdPage;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.Model.PicturesModel;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.appsinventiv.classifiedads.ViewHolder.ItemViewHolder;
import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by maliahmed on 15/12/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    Context context;
    ArrayList<AdDetails> itemList;
    LayoutInflater layoutInflater;
    DatabaseReference mDatabase;

    public ItemAdapter(Context context, ArrayList<AdDetails> itemList) {
        mDatabase= FirebaseDatabase.getInstance().getReference();
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_layout, parent, false);
        ItemAdapter.ViewHolder viewHolder = new ItemAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemAdapter.ViewHolder holder, int position) {
        final AdDetails model = itemList.get(position);
        holder.title.setText(model.getTitle());
        holder.price.setText("Rs " + model.getPrice());
        holder.time.setText(getFormattedDate(context, model.getTime()));
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
    }

    public String getFormattedDate(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd MMM ";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday ";
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("dd MMM , h:mm aa", smsTime).toString();
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price, time;
        public ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleItem);
            price = itemView.findViewById(R.id.price);
            time = itemView.findViewById(R.id.time);
            thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }
}