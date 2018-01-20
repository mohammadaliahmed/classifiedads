package com.appsinventiv.classifiedads.Classes;

import android.view.View;

/**
 * Created by AliAh on 15/01/2018.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
