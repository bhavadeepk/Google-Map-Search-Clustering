package com.bhavadeep.googleclustering.UI;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.bhavadeep.googleclustering.Models.Result;
import com.bhavadeep.googleclustering.R;
import com.bhavadeep.googleclustering.UI.Map.CustomClusterItem;
import com.bhavadeep.googleclustering.UI.Map.GlideTarget;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    Target target;

    private GoogleMap googleMap;
    private OnMapFragmentInteractionListener listener;
    private Context context;
    List<Result> resultList;
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
        View rootView  = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView = rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        initMap();
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
        MapViewFragment mapViewFragment = new MapViewFragment();

        return mapViewFragment;
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
        resultList.addAll(results);
        if(googleMap != null) {
            MarkerManager markerManager = new MarkerManager(googleMap);
            clusterManager = new ClusterManager<>(context, googleMap, markerManager);
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
            SimpleTarget<Bitmap> simpleTarget;
            final Set<Target> targetSet = new HashSet<>();
            for (final Result r : resultList) {
                final LatLng latLng = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());
                final String name = r.getName();
                final String icon = r.getIcon();
                final String snippet = (new StringBuilder()).append(r.getGeometry().getLocation().getLat()).append(" , ")
                        .append(r.getGeometry().getLocation().getLng()).toString();

             /*   GlideTarget glideTarget = new GlideTarget(clusterManager, name, icon, latLng);


                simpleTarget = new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        CustomClusterItem item = new CustomClusterItem(latLng, r.getName(), null, r.getIcon(), bitmap);
                        clusterManager.addItem(item);
                        clusterManager.cluster();
                    }

                  *//*  @Override
                    public int hashCode() {
                        return r.hashCode();
                    }*//*
                };
                //Glide.with(context).load(r.getIcon()).asBitmap().into(simpleTarget);*/

                target = new Target() {
                    @Override
                    public int hashCode() {
                        return (new Object()).hashCode();
                    }

                    @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                CustomClusterItem item = new CustomClusterItem(latLng, name, snippet, bitmap);
                clusterManager.addItem(item);
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
       // targetSet.add(target);
        Picasso.with(context).load(r.getIcon()).into(target);
    }
}
        else {
                Toast.makeText(context, "Maps null", Toast.LENGTH_SHORT).show();
                listener.getData();
                }

    }


    interface OnMapFragmentInteractionListener{

        void getData( );

    }

    public class CustomClusterRenderer extends DefaultClusterRenderer<CustomClusterItem> {

        private final int dimension = (int) getResources().getDimension(R.dimen._50dp);
        private final int padding = (int) getResources().getDimension(R.dimen._5dp);
        private IconGenerator singleIconGenerator;
        private IconGenerator clusterIconGenerator;
        private View clusterView;
        private ImageView singleIconView;

        public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<CustomClusterItem> clusterManager) {
            super(context, map, clusterManager);
            singleIconGenerator = new IconGenerator(context);
            clusterIconGenerator = new IconGenerator(context);
            clusterView = getActivity().getLayoutInflater().inflate(R.layout.marker_icon, null, false);
            clusterIconGenerator.setContentView(clusterView);
        }

        public CustomClusterRenderer() {
            super(context, getGoogleMap(), clusterManager );
            singleIconGenerator = new IconGenerator(context);
            clusterIconGenerator = new IconGenerator(context);
            clusterView = getActivity().getLayoutInflater().inflate(R.layout.marker_icon, null, false);
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
            singleIconView = new ImageView(context);
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

        @Override
        protected void onClusterItemRendered(CustomClusterItem clusterItem, Marker marker) {
          // PicassoMarker picassoMarker = new PicassoMarker(marker, singleIconView, singleIconGenerator);
           // Picasso.with(context).load(clusterItem.getIcon()).into(picassoMarker);
            super.onClusterItemRendered(clusterItem, marker);
        }

        @Override
        protected void onClusterRendered(Cluster<CustomClusterItem> cluster, Marker marker) {
        }
    }
}
