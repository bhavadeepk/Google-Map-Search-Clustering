package com.bhavadeep.googleclustering.UI;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bhavadeep.googleclustering.Models.Result;
import com.bhavadeep.googleclustering.R;
import com.bhavadeep.googleclustering.UI.Map.CustomClusterItem;
import com.bhavadeep.googleclustering.UI.Map.PicassoMarker;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;


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
        if(!resultList.equals(results))
            resultList.addAll(results);
        if(googleMap != null) {
            MarkerManager markerManager = new MarkerManager(googleMap);
            clusterManager = new ClusterManager<>(context, googleMap, markerManager);
            googleMap.setOnCameraIdleListener(clusterManager);
            clusterManager.setRenderer(new CustomClusterRenderer());
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
            for (Result r : resultList) {
                LatLng latLng = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());
                CustomClusterItem item = new CustomClusterItem(latLng, r.getName(), null, r.getIcon());
                clusterManager.addItem(item);
            }
            clusterManager.cluster();
        }

    }


    interface OnMapFragmentInteractionListener{

        void getData( );

    }

    public class CustomClusterRenderer extends DefaultClusterRenderer<CustomClusterItem> {

        private final int dimension = 50;
        private IconGenerator singleIconGenerator;
        private IconGenerator clusterIconGenerator;
        private View clusterView;
        private ImageView singleIconView;

        public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<CustomClusterItem> clusterManager) {
            super(context, map, clusterManager);

        }

        public CustomClusterRenderer() {
            super(context, getGoogleMap(), clusterManager );
            singleIconGenerator = new IconGenerator(context);
            clusterIconGenerator = new IconGenerator(context);
            clusterView = getActivity().getLayoutInflater().inflate(R.layout.marker_icon, null);
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
        protected void onBeforeClusterItemRendered(CustomClusterItem item, MarkerOptions markerOptions) {
            singleIconView = new ImageView(context);
            singleIconView.setLayoutParams(new ViewGroup.LayoutParams(dimension, dimension));
            singleIconView.setPadding(5,5,5,5);
            singleIconGenerator.setContentView(singleIconView);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<CustomClusterItem> cluster, MarkerOptions markerOptions) {
            Bitmap bitmap = clusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }

        @Override
        protected void onClusterItemRendered(CustomClusterItem clusterItem, Marker marker) {

            PicassoMarker picassoMarker = new PicassoMarker(marker, singleIconView, singleIconGenerator);
            Picasso.with(context).load(clusterItem.getIcon()).into(picassoMarker);
        }

        @Override
        protected void onClusterRendered(Cluster<CustomClusterItem> cluster, Marker marker) {
        }
    }
}
