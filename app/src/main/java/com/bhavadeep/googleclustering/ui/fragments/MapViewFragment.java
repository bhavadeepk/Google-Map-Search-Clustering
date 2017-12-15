package com.bhavadeep.googleclustering.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bhavadeep.googleclustering.models.Result;
import com.bhavadeep.googleclustering.R;
import com.bhavadeep.googleclustering.ui.map.CustomClusterItem;
import com.bhavadeep.googleclustering.ui.map.CustomClusterRenderer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    Target target;
    private GoogleMap googleMap;
    private OnMapFragmentInteractionListener listener;
    private Context context;
    List<Result> resultList;
    View rootView;
    ClusterManager<CustomClusterItem> clusterManager;

    public MapViewFragment() {
        //Required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultList = new ArrayList<>();

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
            mapView = rootView.findViewById(R.id.map_view);
            mapView.onCreate(savedInstanceState);
            initMap();
            setRetainInstance(true);
        }
        return rootView;
    }

    private void initMap() {
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap gMap) {
        if (googleMap != null) {
            return;
        }

        googleMap = gMap;
        MapsInitializer.initialize(context);
        LatLng unitedStatesLatLng = new LatLng(45,-100);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(unitedStatesLatLng, (float) 3.4));
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                listener.getData();
            }
        });
       }

    public static MapViewFragment newInstance() {
        return new MapViewFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnMapFragmentInteractionListener){
            listener = (OnMapFragmentInteractionListener) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void updateView(List<Result> results) {
        resultList.clear();
        resultList.addAll(results);
        if(googleMap != null) {
            clusterManager = new ClusterManager<>(context, googleMap);
            googleMap.setOnCameraIdleListener(clusterManager);
            googleMap.setOnInfoWindowClickListener(clusterManager);
            clusterManager.setRenderer(new CustomClusterRenderer(context, googleMap, clusterManager));
            googleMap.setOnMarkerClickListener(clusterManager);
            clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<CustomClusterItem>() {
                @Override
                public boolean onClusterClick(Cluster<CustomClusterItem> cluster) {
                    // Create the builder to collect all essential cluster items for the bounds.
                    LatLngBounds.Builder builder = LatLngBounds.builder();
                    for (ClusterItem item : cluster.getItems()) {
                        builder.include(item.getPosition());
                    }
                    // Get the LatLngBounds
                    final LatLngBounds bounds = builder.build();

                    // Animate camera to the bounds
                    try {
                        getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
                }
            });
            googleMap.clear();
            clusterManager.clearItems();
            for (final Result r : resultList) {
                final LatLng latLng = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());
                final String name = r.getName();
                final String snippet = (new StringBuilder()).append(r.getGeometry().getLocation().getLat()).append(" , ")
                        .append(r.getGeometry().getLocation().getLng()).append("\n").append(r.getAddress())
                        .append("Ratings : ").append((r.getRating()!=null ? r.getRating() : "Unavailable")).toString();

                target = new Target() {
                    @Override
                    public int hashCode() {
                        return (new Object()).hashCode();
                    }

                    @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Log.d("Picasso:", "OnBitmapLoaded");
                CustomClusterItem item = new CustomClusterItem(latLng, name, snippet, bitmap);
                clusterManager.addItem(item);
                if(resultList.indexOf(r) == resultList.size()-1)
                    clusterManager.cluster();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("Picasso:", "OnBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d("Picasso:", "OnPreLoaded");
            }
        };
        Picasso.with(context).load(r.getIcon()).into(target);
    }
}
        else {
                Toast.makeText(context, "Maps null", Toast.LENGTH_SHORT).show();
                listener.getData();
                }

    }


    public interface OnMapFragmentInteractionListener{

        void getData( );

    }

   }
