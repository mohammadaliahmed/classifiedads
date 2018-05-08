package com.appsinventiv.mobilemart.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.mobilemart.R;


/**
 * Created by maliahmed on 15/12/2017.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder {


    public TextView title;
    public TextView price;
    public TextView time;
    public ImageView thumbnail;
    public ItemViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.titleItem);
        price = (TextView) itemView.findViewById(R.id.price);
        time = (TextView) itemView.findViewById(R.id.time);
        thumbnail=(ImageView)itemView.findViewById(R.id.thumbnail);
    }
}
