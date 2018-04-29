package com.appsinventiv.classifiedads.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;

import com.appsinventiv.classifiedads.Activities.AdPage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by AliAh on 10/04/2018.
 */

public class GetAdAddress {
    private GetAdAddress() {
    }

    public static String getAddress(Context context, Double lat, Double lon) {
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            address = addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public static String getCity(Context context, Double lat, Double lon) {
        if (lat == null || lon == null) {
            lon = 60.3323097;
            lat = 30.0493247;
        }
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            address = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public static String getFullAddress(Context context, Double lat, Double lon) {
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

}
