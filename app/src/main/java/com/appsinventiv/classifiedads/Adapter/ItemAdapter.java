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
import android.widget.Toast;


import com.appsinventiv.classifiedads.Activities.AdPage;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.Model.Item;
import com.appsinventiv.classifiedads.Interface.OnLoadMoreListener;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.ViewHolder.ItemViewHolder;
import com.appsinventiv.classifiedads.ViewHolder.ProgressBarViewHolder;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by maliahmed on 15/12/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter {

    List<AdDetails> itemList;
    Activity activity;
    Context ctx;
    LayoutInflater mLInflater;

    private static final int ITEM_VIEW = 0;
    private static final int LOADING_VIEW = 1;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    private OnLoadMoreListener onLoadMoreListener;
    boolean isLoading;
    boolean isAllLoaded = false;



    public ItemAdapter(List<AdDetails> itemList, Activity activity, Context ctx, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.activity = activity;
        this.ctx = ctx;

        mLInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();

                    lastVisibleItem = llm.findLastVisibleItemPosition();
                    totalItemCount = llm.getItemCount();
                    Log.i("isLoading", String.valueOf(isLoading));
                    Log.i("isAllLoaded", String.valueOf(isAllLoaded));
                    Log.i("isLast", String.valueOf(lastVisibleItem));


                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold) && !isAllLoaded) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }

                }
            });


        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) != null ? ITEM_VIEW : LOADING_VIEW;
    }

    public void setOnLoadMore(OnLoadMoreListener onLoadMore) {
        onLoadMoreListener = onLoadMore;
    }


    public void setIsLoading(boolean param) {
        isLoading = param;
    }

    public void isFullLoaded(boolean param) {
        isAllLoaded = param;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == ITEM_VIEW) {
            View v = mLInflater.inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(v);
        }
        else if (viewType == LOADING_VIEW) {
            View v1 = mLInflater.inflate(R.layout.progressbar_item, parent, false);
            return new ProgressBarViewHolder(v1);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

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

                    i.putExtra("adId",""+adId.getTime());
                    ctx.startActivity(i);
//
                }
            });

        }
        else if (getViewType == LOADING_VIEW) {

            if (isLoading && !isAllLoaded) {

                ((ProgressBarViewHolder) holder).progressBar.setVisibility(
                        View.VISIBLE);
                ((ProgressBarViewHolder) holder).progressBar.setIndeterminate(true);

            } else if (!isLoading || isAllLoaded) {

                ((ProgressBarViewHolder) holder).progressBar.setVisibility(View.GONE);
            }
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
