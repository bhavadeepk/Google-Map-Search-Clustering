package com.bhavadeep.googleclustering.UI.Map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by ${Bhavadeep} on 12/13/2017.
 */

public class PicassoMarkerOptions implements Target {
    Marker markerOptions;

    public PicassoMarkerOptions(Marker markerOptions) {
        this.markerOptions = markerOptions;
    }

    @Override
    public int hashCode() {
        return markerOptions.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof PicassoMarkerOptions) {
            Marker markerOptions = ((PicassoMarkerOptions) o).markerOptions;
            return this.markerOptions.equals(markerOptions);
        } else {
            return false;
        }
    }


    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        markerOptions.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
