package com.bhavadeep.googleclustering.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.bhavadeep.googleclustering.ui.map.CustomInfoWindowAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.Algorithm;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


public class MapViewFragment extends Fragment implements OnMapReadyCallback,
        ClusterManager.OnClusterItemInfoWindowClickListener<CustomClusterItem>,
        ClusterManager.OnClusterClickListener<CustomClusterItem>,
        ClusterManager.OnClusterItemClickListener<CustomClusterItem> {

    private MapView mapView;
    Target target;
    private GoogleMap googleMap;
    private OnMapFragmentInteractionListener listener;
    private Context context;
    List<Result> resultList;
    private CustomInfoWindowAdapter infoWindowAdapter;
    private CustomClusterItem itemClicked;

    public List<Result> getResultList() {
        return resultList;
    }

    View rootView;
    int count = 0;
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
        Log.d("Map Fragment", "OnMapReady");
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
        if (results.size() == listener.getCount() && !results.isEmpty()) {
            resultList.clear();
            resultList.addAll(results);
        } else {
            if (count < 1) {
                count++;
                Toast.makeText(context, "List empty : Trying again", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        listener.getData();
                    }
                }, 5000);
            } else {
                Toast.makeText(context, "Error retrieving data, check internet connection & try again", Toast.LENGTH_SHORT).show();
            }
        }
        if(googleMap != null) {
            clusterManager = new ClusterManager<>(context, googleMap);
            googleMap.setOnCameraIdleListener(clusterManager);
            googleMap.setOnInfoWindowClickListener(clusterManager);
            clusterManager.setRenderer(new CustomClusterRenderer(context, googleMap, clusterManager));
            googleMap.setOnMarkerClickListener(clusterManager);
            googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
            Algorithm<CustomClusterItem> algorithm = new NonHierarchicalDistanceBasedAlgorithm<>();
            clusterManager.setAlgorithm(algorithm);
            clusterManager.setOnClusterClickListener(this);
            clusterManager.setOnClusterItemInfoWindowClickListener(this);
            infoWindowAdapter = new CustomInfoWindowAdapter(context);
            clusterManager.getMarkerCollection().setOnInfoWindowAdapter(infoWindowAdapter);
            clusterManager.setOnClusterItemClickListener(this);
            googleMap.clear();
            clusterManager.clearItems();
            count = 0;
            for (final Result result : resultList) {
                final LatLng latLng = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
                final String name = result.getName();
                final String address = result.getAddress();
                final String ratings = result.getRating();
                target = new Target() {

                    @Override
                    public int hashCode() {
                        return (new Object()).hashCode();
                    }

                    @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Log.d("Picasso:", "OnBitmapLoaded");
                        CustomClusterItem item = new CustomClusterItem(latLng, name, address, ratings, bitmap);
                        clusterManager.addItem(item);
                        count++;
                        if (count == resultList.size()) {
                            clusterManager.cluster();
                            setCamera(clusterManager);
                            //Check until all the result are loaded on the map
                            listener.mapUpdated(clusterManager.getAlgorithm().getItems().size());
                        }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("Picasso:", "OnBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
                Picasso.with(context).load(result.getIcon()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(target);

            }
}
        else {
            Toast.makeText(context, "Maps loaded late. Re-clustering!!", Toast.LENGTH_SHORT).show();
                listener.getData();
                }

    }

    @Override
    public void onClusterItemInfoWindowClick(CustomClusterItem customClusterItem) {
        listener.showDetails(customClusterItem);
    }

    @Override
    public boolean onClusterClick(Cluster<CustomClusterItem> cluster) {
        {
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

            listener.closeFAB();

            return true;
        }
    }

    void setCamera(ClusterManager<CustomClusterItem> clusterManager) {
        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (CustomClusterItem item : clusterManager.getAlgorithm().getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onClusterItemClick(CustomClusterItem customClusterItem) {
        infoWindowAdapter.setItemClicked(customClusterItem);
        itemClicked = customClusterItem;
        return false;
    }



    public interface OnMapFragmentInteractionListener{

        void getData( );

        void showDetails(CustomClusterItem customClusterItem);

        int getCount();

        void mapUpdated(int size);

        void closeFAB();
    }

   }
