package com.bhavadeep.googleclustering.ui.map;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;



public class CustomClusterItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String address;
    private final String ratings;
    private final Bitmap bitmap;

    public CustomClusterItem(LatLng position, String title, String address, String ratings, Bitmap bitmap) {
        this.position = position;
        this.title = title;
        this.address = address;
        this.ratings = ratings;
        this.bitmap = bitmap;
    }

    public String getAddress() {
        return address;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getRatings() {
        return ratings;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
    }

}
