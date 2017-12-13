package com.bhavadeep.googleclustering.UI;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bhavadeep.googleclustering.Models.Result;
import com.bhavadeep.googleclustering.Presenter.IViewUpdater;
import com.bhavadeep.googleclustering.Presenter.MainPresenter;
import com.bhavadeep.googleclustering.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MapViewFragment.OnMapFragmentInteractionListener{

    FloatingActionButton fabMain;
    FloatingActionButton fabList;
    FloatingActionButton fabMap;
    private boolean isFabMenuOpen = false;
    MapViewFragment mapViewFragment;
    ListFragment listFragment;
    final String TAG_MAP = "Map";
    final String TAG_LIST = "List";
    String activeFragmentTag = TAG_MAP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isGooglePlayServicesAvailable()){
            setContentView(R.layout.activity_main);
        }
        if(savedInstanceState == null){
            mapViewFragment = MapViewFragment.newInstance();
            listFragment = ListFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.fragment_container,mapViewFragment, TAG_MAP )
                    .add(R.id.fragment_container, listFragment, TAG_LIST ).addToBackStack(TAG_LIST).commit();
        }
        fabMain = findViewById(R.id.floatingActionButton);
        fabList = findViewById(R.id.fab_list);
        fabMap = findViewById(R.id.fab_map);
        View.OnClickListener fabClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.floatingActionButton){
                    if(isFabMenuOpen){
                        hideMenu();
                    }
                    else {
                        showMenu();
                    }
                }
                else {
                    if (v.getId() == R.id.fab_map) {
                        hideMenu();
                        if(getFragmentManager().findFragmentByTag(TAG_LIST) != null){
                            getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag(TAG_LIST))
                                                        .show(getFragmentManager().findFragmentByTag(TAG_MAP)).commit();
                            activeFragmentTag = TAG_MAP;
                        }
                    } else {
                        hideMenu();
                        if(getFragmentManager().findFragmentByTag(TAG_MAP) != null){
                            getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag(TAG_MAP))
                                    .show(getFragmentManager().findFragmentByTag(TAG_LIST)).commit();
                            activeFragmentTag = TAG_LIST;
                        }

                     }
                }
            }
        };
        fabMain.setOnClickListener(fabClickListener);
        fabList.setOnClickListener(fabClickListener);
        fabMap.setOnClickListener(fabClickListener);

    }

    private void showMenu() {
        fabMap.setClickable(true);
        fabList.setClickable(true);
        fabMap.animate().translationX(-getResources().getDimension(R.dimen._100dp)).rotation(360).setDuration(300);
        fabList.animate().translationY(-getResources().getDimension(R.dimen._100dp)).rotation(360).setDuration(300);
        isFabMenuOpen = true;
    }

    private void hideMenu() {
        fabMap.setClickable(false);
        fabList.setClickable(false);
        fabMap.animate().translationX(0).rotation(0).setDuration(300);
        fabList.animate().translationY(0).rotation(0).setDuration(300);
        isFabMenuOpen = false;
    }

    boolean isGooglePlayServicesAvailable(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int availability = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(availability == ConnectionResult.SUCCESS){
            return true;
        }
        else if(googleApiAvailability.isUserResolvableError(availability)){
            googleApiAvailability.getErrorDialog(this, availability,0).show();
        }
        else {
            Toast.makeText(this, "Can't access Google Play Services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    void showToast(){
        Toast.makeText(this,"HEY",Toast.LENGTH_LONG).show();
    }
}
