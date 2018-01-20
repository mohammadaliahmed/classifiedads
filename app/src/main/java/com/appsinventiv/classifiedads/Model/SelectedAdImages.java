package com.appsinventiv.classifiedads.Model;

import android.net.Uri;

import java.net.URI;

/**
 * Created by AliAh on 16/01/2018.
 */

public class SelectedAdImages {
    public Uri imageUri;

    public SelectedAdImages() {
    }

    public SelectedAdImages(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
