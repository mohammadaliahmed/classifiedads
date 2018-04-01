package com.appsinventiv.classifiedads.Model;

import android.net.Uri;

import java.net.URI;

/**
 * Created by AliAh on 16/01/2018.
 */

public class SelectedAdImages {
   String url;

    public SelectedAdImages() {
    }

    public SelectedAdImages(String url) {

        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
