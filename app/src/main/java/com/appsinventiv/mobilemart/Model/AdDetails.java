package com.appsinventiv.mobilemart.Model;

/**
 * Created by maliahmed on 12/1/2017.
 */

public class AdDetails {
    String adId,title, description, username, phone, city, picUrl, adStatus, mainCategory, childCategory;
    long time, price, views;
    Double lattitude,longitude;

    public AdDetails() {
    }

    public AdDetails(String adId, String title, String description, String username, String phone, String city, String picUrl, String adStatus, String mainCategory, String childCategory, long time, long price, long views, Double lattitude, Double longitude) {
        this.adId = adId;
        this.title = title;
        this.description = description;
        this.username = username;
        this.phone = phone;
        this.city = city;
        this.picUrl = picUrl;
        this.adStatus = adStatus;
        this.mainCategory = mainCategory;
        this.childCategory = childCategory;
        this.time = time;
        this.price = price;
        this.views = views;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
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



    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}