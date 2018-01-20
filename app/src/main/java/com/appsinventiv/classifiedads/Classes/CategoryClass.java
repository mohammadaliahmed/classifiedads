package com.appsinventiv.classifiedads.Classes;

import android.widget.ImageView;

/**
 * Created by AliAh on 16/01/2018.
 */

public class CategoryClass {
    public CategoryClass() {
    }

    String title;
    int category_icon;

    public CategoryClass(String title, int category_icon) {
        this.title = title;
        this.category_icon = category_icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(int category_icon) {
        this.category_icon = category_icon;
    }
}