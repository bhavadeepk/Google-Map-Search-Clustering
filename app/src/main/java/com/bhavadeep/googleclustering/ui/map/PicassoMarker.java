package com.bhavadeep.googleclustering.ui.map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;



public class PicassoMarker implements Target {
    private Marker marker;
    private ImageView imageView;
    private IconGenerator iconGenerator;

    public PicassoMarker(Marker marker, ImageView imageView, IconGenerator iconGenerator) {
        this.marker = marker;
        this.imageView = imageView;
        this.iconGenerator = iconGenerator;
    }


    @Override
    public int hashCode() {
        return marker.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof PicassoMarker) {
            Marker marker = ((PicassoMarker) o).marker;
            return this.marker.equals(marker);
        } else {
            return false;
        }
    }


    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        imageView.setImageBitmap(bitmap);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()));
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
