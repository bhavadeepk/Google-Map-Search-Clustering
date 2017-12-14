package com.bhavadeep.googleclustering.UI.Map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by ${Bhavadeep} on 12/13/2017.
 */

public class CustomClusterItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;
    private final String icon;

    public CustomClusterItem(LatLng position, String title, String snippet, String icon) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
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
