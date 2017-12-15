package com.bhavadeep.googleclustering.UI.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bhavadeep.googleclustering.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by ${Bhavadeep} on 12/14/2017.
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<CustomClusterItem> {

    private final int dimension;
    private final int padding;
    private IconGenerator singleIconGenerator;
    private IconGenerator clusterIconGenerator;
    private View clusterView;
    private ImageView singleIconView;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<CustomClusterItem> clusterManager) {
        super(context, map, clusterManager);
        singleIconGenerator = new IconGenerator(context);
        clusterIconGenerator = new IconGenerator(context);
        singleIconView = new ImageView(context);
        clusterView = ((Activity)context).getLayoutInflater().inflate(R.layout.marker_icon, null, false);
        dimension = (int) context.getResources().getDimension(R.dimen._50dp);
        padding = (int) context.getResources().getDimension(R.dimen._5dp);
        clusterIconGenerator.setContentView(clusterView);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<CustomClusterItem> cluster) {
        return cluster.getSize()>3;
    }

    @Override
    public void setOnClusterClickListener(ClusterManager.OnClusterClickListener<CustomClusterItem> listener) {
        super.setOnClusterClickListener(listener);
    }

    @Override
    public void setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<CustomClusterItem> listener) {
        super.setOnClusterItemClickListener(listener);
    }

    @Override
    public void setOnClusterItemInfoWindowClickListener(ClusterManager.OnClusterItemInfoWindowClickListener<CustomClusterItem> listener) {
        super.setOnClusterItemInfoWindowClickListener(listener);
    }

    @Override
    protected void onBeforeClusterItemRendered(final CustomClusterItem item, MarkerOptions markerOptions) {
        singleIconView.setLayoutParams(new ViewGroup.LayoutParams(dimension, dimension));
        singleIconView.setPadding(padding,padding,padding,padding);
        singleIconView.setImageBitmap(item.getBitmap());
        singleIconGenerator.setContentView(singleIconView);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(singleIconGenerator.makeIcon()));
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<CustomClusterItem> cluster, MarkerOptions markerOptions) {
        Bitmap bitmap = clusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }

}

