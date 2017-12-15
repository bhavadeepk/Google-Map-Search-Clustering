package com.bhavadeep.googleclustering.ui.map;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by ${Bhavadeep} on 12/13/2017.
 */

public class CustomClusterItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;
    private final Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public CustomClusterItem(LatLng position, String title, String snippet, Bitmap bitmap) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.bitmap = bitmap;
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
        return snippet;
    }
}
