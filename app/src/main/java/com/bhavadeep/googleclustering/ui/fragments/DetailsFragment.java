package com.bhavadeep.googleclustering.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bhavadeep.googleclustering.R;


public class DetailsFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_BITMAP = "bitmap";
    private static final String ARG_RATING = "rating";

    public DetailsFragment() {
        //Empty Constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details_place, container, false);
        TextView titleTextView = rootView.findViewById(R.id.title_detail);
        TextView latitudeTextView = rootView.findViewById(R.id.latitude_detail);
        TextView longitudeTextView = rootView.findViewById(R.id.longitude_detail);
        TextView addressTextView = rootView.findViewById(R.id.address_detail);
        Button navigateButton = rootView.findViewById(R.id._navigate_button);
        RatingBar ratingBar = rootView.findViewById(R.id.ratingBar);
        ImageView iconImageView = rootView.findViewById(R.id.image_details);
        setRetainInstance(true);


        if (getArguments() != null) {
            final Bundle detailsBundle = getArguments();
            titleTextView.setText(detailsBundle.getString(ARG_TITLE));
            latitudeTextView.setText(detailsBundle.getString(ARG_LATITUDE));
            longitudeTextView.setText(detailsBundle.getString(ARG_LONGITUDE));
            addressTextView.setText(detailsBundle.getString(ARG_ADDRESS));
            iconImageView.setImageBitmap((Bitmap) detailsBundle.getParcelable(ARG_BITMAP));
            if (detailsBundle.getString(ARG_RATING) != null) {
                float ratings = Float.parseFloat(detailsBundle.getString(ARG_RATING));
                ratingBar.setRating(ratings);
            } else {
                ratingBar.setVisibility(View.GONE);
                TextView ratingsTitleView = rootView.findViewById(R.id.textView8);
                ratingsTitleView.setVisibility(View.GONE);
            }
            navigateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent googleMapsIntent = new Intent(Intent.ACTION_VIEW);
                    googleMapsIntent.setData(Uri.parse("geo:" + detailsBundle.getString(ARG_LATITUDE) + ","
                            + detailsBundle.getString(ARG_LONGITUDE) + "?z=11" + "&q=" + detailsBundle.getString(ARG_LATITUDE) + ","
                            + detailsBundle.getString(ARG_LONGITUDE) + "(" + detailsBundle.getString(ARG_TITLE) + ")"));
                    if (googleMapsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(googleMapsIntent);
                    }
                }
            });
        }

        return rootView;
    }

    public static DetailsFragment newInstance(String title, String latitude, String longitude, String address, String ratings, Bitmap bitmap) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle detailsBundle = new Bundle();
        detailsBundle.putString(ARG_TITLE, title);
        detailsBundle.putString(ARG_LATITUDE, latitude);
        detailsBundle.putString(ARG_LONGITUDE, longitude);
        detailsBundle.putString(ARG_ADDRESS, address);
        detailsBundle.putString(ARG_RATING, ratings);
        detailsBundle.putParcelable(ARG_BITMAP, bitmap);
        detailsFragment.setArguments(detailsBundle);
        return detailsFragment;
    }


}
