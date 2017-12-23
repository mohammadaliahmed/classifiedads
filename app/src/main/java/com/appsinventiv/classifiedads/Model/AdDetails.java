package com.appsinventiv.classifiedads.Model;

/**
 * Created by maliahmed on 12/1/2017.
 */

public class AdDetails {
    String title, description,  city,picUrl, isActive, mainCategory, childCategory, subChild;
    long time,userId,price;

    public AdDetails() {
    }

    public AdDetails(String title, String description, String city, String picUrl, String isActive, String mainCategory, String childCategory, String subChild, long time, long userId, long price) {
        this.title = title;
        this.description = description;
        this.city = city;
        this.picUrl = picUrl;
        this.isActive = isActive;
        this.mainCategory = mainCategory;
        this.childCategory = childCategory;
        this.subChild = subChild;
        this.time = time;
        this.userId = userId;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(String childCategory) {
        this.childCategory = childCategory;
    }

    public String getSubChild() {
        return subChild;
    }

    public void setSubChild(String subChild) {
        this.subChild = subChild;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}