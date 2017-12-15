package com.bhavadeep.googleclustering.UI.Map;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by ${Bhavadeep} on 12/14/2017.
 */

public class GlideTarget extends SimpleTarget {
    IconGenerator iconGenerator;
    CustomClusterItem item;
    ClusterManager clusterManager;
    String name;
    String icon;

    public GlideTarget(ClusterManager clusterManager, String name, String icon, LatLng latLng) {

        this.clusterManager = clusterManager;
        this.name = name;
        this.icon = icon;
        this.latLng = latLng;
    }

    LatLng latLng;
    public GlideTarget(IconGenerator iconGenerator, ImageView imageView, ClusterManager clusterManager, CustomClusterItem item) {
        this.iconGenerator = iconGenerator;
        this.imageView = imageView;
        this.clusterManager = clusterManager;
        this.item = item;
    }

    public GlideTarget(int width, int height, IconGenerator iconGenerator, ImageView imageView) {
        super(width, height);
        this.iconGenerator = iconGenerator;
        this.imageView = imageView;
    }

    ImageView imageView;
    @Override
    public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
       // CustomClusterItem item = new CustomClusterItem(latLng, name, null, icon, (Bitmap)resource);
        clusterManager.addItem(item);
        clusterManager.cluster();
    }
}
