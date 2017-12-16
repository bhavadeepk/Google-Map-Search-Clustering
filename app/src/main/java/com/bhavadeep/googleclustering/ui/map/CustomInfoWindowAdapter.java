package com.bhavadeep.googleclustering.ui.map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bhavadeep.googleclustering.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View view;

    void setItemClicked(CustomClusterItem itemClicked) {
        this.itemClicked = itemClicked;
    }

    private CustomClusterItem itemClicked;

    CustomInfoWindowAdapter(Context context) {
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.info_window, null, false);

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView titleTextView = view.findViewById(R.id.title_info);
        TextView locationTextView = view.findViewById(R.id.location_info);
        TextView addressTextView = view.findViewById(R.id.address_info);
        TextView ratingTextView = view.findViewById(R.id.ratings_info);
        if (itemClicked != null) {
            titleTextView.setText(itemClicked.getTitle());
            locationTextView.setText(itemClicked.getPosition().toString());
            addressTextView.setText(itemClicked.getAddress());
            String ratings = "Ratings : " + itemClicked.getRatings();
            ratingTextView.setText(ratings);
        }
        return view;
    }
}
