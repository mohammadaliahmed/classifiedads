package com.appsinventiv.classifiedads.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsinventiv.classifiedads.Activities.AdPage;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.ViewHolder.ItemViewHolder;
import com.appsinventiv.classifiedads.ViewHolder.ProgressBarViewHolder;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by AliAh on 09/01/2018.
 */

public class SearchItemsAdapter extends RecyclerView.Adapter{

    List<AdDetails> itemList;
    Activity activity;
    Context ctx;
    LayoutInflater mLInflater;
    private static final int ITEM_VIEW = 0;

    public SearchItemsAdapter(List<AdDetails> itemList, Activity activity, Context ctx, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.activity = activity;
        this.ctx = ctx;

        mLInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW) {
            View v = mLInflater.inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int getViewType = holder.getItemViewType();
        final AdDetails model = itemList.get(position);

        if (getViewType == ITEM_VIEW) {
            DecimalFormat formatter = new DecimalFormat("##,###,###");
            String formatedPrice = formatter.format(model.getPrice());
            final AdDetails adId=itemList.get(position);
            ((ItemViewHolder) holder).title.setText(model.getTitle());
            ((ItemViewHolder) holder).price.setText("Rs "+formatedPrice);
            ((ItemViewHolder) holder).time.setText(getFormattedDate(ctx,model.getTime()));
            Glide.with(ctx).load(model.getPicUrl()).into((((ItemViewHolder) holder).thumbnail));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i =new Intent(ctx,AdPage.class);

                    i.putExtra("adId",""+adId.getAdId());
                    ctx.startActivity(i);
//
                }
            });

        }

    }
    public String getFormattedDate(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd MMM ";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
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
}
