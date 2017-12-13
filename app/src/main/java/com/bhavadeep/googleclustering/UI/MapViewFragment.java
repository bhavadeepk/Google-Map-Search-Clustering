package com.bhavadeep.googleclustering.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhavadeep.googleclustering.Models.Result;
import com.bhavadeep.googleclustering.Presenter.IViewUpdater;
import com.bhavadeep.googleclustering.Presenter.MainPresenter;
import com.bhavadeep.googleclustering.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class MapViewFragment extends Fragment implements OnMapReadyCallback, IViewUpdater {

    private MapView mapView;
    private GoogleMap googleMap;
    private OnMapFragmentInteractionListener listener;
    private Context context;
    MainPresenter presenter;
    List<Result> resultList;

    public MapViewFragment() {
        //Required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter(this);
        resultList = new ArrayList<>();
        presenter.getResults("food");
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
    public void onMapReady(GoogleMap gMap) {
        if (googleMap != null) {
            return;
        }
        googleMap = gMap;
        MapsInitializer.initialize(context);
        LatLng unitedStatesLatLng = new LatLng(45,-100);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(unitedStatesLatLng, (float) 3.4));
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

    @Override
    public void updateView(List<Result> results) {
        resultList.addAll(results);
        for(Result r : resultList){
            Log.d("Results", r.getAddress());
        }
    }

    interface OnMapFragmentInteractionListener{

    }
}
