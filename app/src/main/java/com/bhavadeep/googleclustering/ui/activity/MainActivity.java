package com.bhavadeep.googleclustering.ui.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bhavadeep.googleclustering.models.Result;
import com.bhavadeep.googleclustering.presenter.IViewUpdater;
import com.bhavadeep.googleclustering.presenter.MainPresenter;
import com.bhavadeep.googleclustering.R;
import com.bhavadeep.googleclustering.ui.fragments.DetailsFragment;
import com.bhavadeep.googleclustering.ui.fragments.ListFragment;
import com.bhavadeep.googleclustering.ui.fragments.MapViewFragment;
import com.bhavadeep.googleclustering.models.CustomClusterItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.lapism.searchview.SearchView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements MapViewFragment.OnMapFragmentInteractionListener, IViewUpdater
        , ListFragment.OnListFragmentListener {

    FloatingActionButton fabMain;
    FloatingActionButton fabList;
    FloatingActionButton fabMap;
    MainPresenter presenter;
    private boolean isFabMenuOpen = false;
    MapViewFragment mapViewFragment;
    ListFragment listFragment;
    DetailsFragment detailsFragment;
    final String TAG_MAP = "Map";
    final String TAG_LIST = "List";
    String activeFragmentTag;
    List<Result> resultList;
    SearchView searchView;
    boolean newQuery = false;
    int backCount = 0;
    private final String TAG_DETAILS = "Details";
    private int count;
    private boolean isDetailsFragment = false;
    private String query = "BBVA Compass";
    private ProgressDialog progressDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isGooglePlayServicesAvailable()){
            setContentView(R.layout.activity_main);
        }

        presenter = new MainPresenter(this);
        resultList = new ArrayList<>();
        if(savedInstanceState == null){
            mapViewFragment = MapViewFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,mapViewFragment, TAG_MAP ).commit();
            activeFragmentTag = TAG_MAP;
            presenter.getResults(query);
        } else {
            mapViewFragment = (MapViewFragment) getFragmentManager().findFragmentByTag(TAG_MAP);
            listFragment = (ListFragment) getFragmentManager().findFragmentByTag(TAG_LIST);
            isDetailsFragment = savedInstanceState.getBoolean("is_details_fragment");
            detailsFragment = (DetailsFragment) getFragmentManager().findFragmentByTag(TAG_DETAILS);
            resultList.addAll(mapViewFragment.getResultList());
        }

        searchView = findViewById(R.id.search_view);
        fabMain = findViewById(R.id.floatingActionButton);
        fabList = findViewById(R.id.fab_list);
        fabMap = findViewById(R.id.fab_map);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        View.OnClickListener fabClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.floatingActionButton){
                    if(isFabMenuOpen){
                        hideMenu();
                    } else {
                        showMenu();
                    }
                } else if (v.getId() == R.id.fab_map) {
                    hideMenu();
                    if (activeFragmentTag.equals(TAG_LIST)) {
                        getFragmentManager().popBackStack();
                        activeFragmentTag = TAG_MAP;

                    }
                } else if (v.getId() == R.id.fab_list) {
                    hideMenu();
                    if (activeFragmentTag.equals(TAG_MAP)) {
                        if (listFragment == null) {
                            listFragment = ListFragment.newInstance();
                        }
                        getFragmentManager().beginTransaction().add(R.id.fragment_container, listFragment, TAG_LIST)
                                .addToBackStack(TAG_LIST).commit();
                        activeFragmentTag = TAG_LIST;
                    }
                }
            }
        };
        fabMain.setOnClickListener(fabClickListener);
        fabList.setOnClickListener(fabClickListener);
        fabMap.setOnClickListener(fabClickListener);


        searchView.setShouldClearOnClose(true);
        searchView.setHint("Search Food, Hospitals, ATM ...");
        searchView.setArrowOnly(true);
        searchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                onBackPressed();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!MainActivity.this.query.equals(query)) {
                    mapViewFragment.setDefaultCamera();
                    MainActivity.this.query = query;
                    presenter.getResults(query);
                    newQuery = true;
                    progressDialog.show();
                }
                searchView.removeFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("active_fragment_tag", activeFragmentTag);
        outState.putBoolean("is_details_fragment", isDetailsFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        activeFragmentTag = savedInstanceState.getString("active_fragment_tag");
        isDetailsFragment = savedInstanceState.getBoolean("is_details_fragment");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDetailsFragment)
            hideFabsandSearch();
    }

    private void showMenu() {
        fabList.setClickable(true);
        fabMap.setClickable(true);
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

    @Override
    public void updateView(List<Result> results) {
        resultList.clear();
        count = results.size();
        resultList.addAll(results);
        if(newQuery) {
            Log.d("Main Activity", "New Query Update view");
            mapViewFragment.updateView(resultList);
            if (listFragment != null)
                listFragment.updateView(resultList);
            newQuery = false;
            progressDialog.dismiss();
        }
    }

    @Override
    public void updateFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void getData() {
        Log.d("MainActivity", "Sending to Map View");
            mapViewFragment.updateView(resultList);
    }

    @Override
    public void onListItemClicked(final Result result) {
        if (isFabMenuOpen)
            hideMenu();
        isDetailsFragment = true;
        Picasso.with(this).load(result.getIcon()).placeholder(R.drawable.ic_marker_placeholder).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                detailsFragment = DetailsFragment.newInstance(result.getName(), result.getGeometry().getLocation().getLat().toString(),
                        result.getGeometry().getLocation().getLng().toString(), result.getAddress(), result.getRating(), bitmap);
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, detailsFragment, TAG_DETAILS)
                        .addToBackStack(TAG_DETAILS).commit();
                hideFabsandSearch();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }

    @Override
    public void getListData() {
        Log.d("MainActivity", "Sending to List View");
        listFragment.updateView(resultList);
    }

    @Override
    public void showDetails(CustomClusterItem item) {
        if (isFabMenuOpen)
            hideMenu();
        isDetailsFragment = true;
        String latitude = String.valueOf(item.getPosition().latitude);
        String longitute = String.valueOf(item.getPosition().longitude);
        detailsFragment = DetailsFragment.newInstance(item.getTitle(), latitude, longitute, item.getAddress(),
                item.getRatings(), item.getBitmap());
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailsFragment, TAG_DETAILS).addToBackStack(TAG_DETAILS)
                .commit();
        hideFabsandSearch();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void mapUpdated(int size) {
        if (resultList.size() != size) {
            Toast.makeText(this, "Not all results were clustered. Retrying!!", Toast.LENGTH_SHORT).show();
            mapViewFragment.updateView(resultList);
        }
    }


    @Override
    public void closeFAB() {
        if (isFabMenuOpen)
            hideMenu();
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag(TAG_DETAILS) != null) {
            showFabsandSearch();
            isDetailsFragment = false;
            super.onBackPressed();
        } else {
            if (isFabMenuOpen)
                hideMenu();
            else {
                if (activeFragmentTag.equals(TAG_LIST)) {
                    activeFragmentTag = TAG_MAP;
                    super.onBackPressed();
                } else if (activeFragmentTag.equals(TAG_MAP)) {
                    if (backCount == 0) {
                        Toast.makeText(MainActivity.this, "Press again to Exit", Toast.LENGTH_SHORT).show();
                        mapViewFragment.setDefaultCamera();
                        backCount++;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                backCount = 0;
                            }
                        }, 2000);
                    } else {
                        super.onBackPressed();
                    }
                }
            }
        }
    }


    void hideFabsandSearch() {
        searchView.setVisibility(View.INVISIBLE);
        fabMain.setVisibility(View.INVISIBLE);
        fabList.setVisibility(View.INVISIBLE);
        fabMap.setVisibility(View.INVISIBLE);
    }

    void showFabsandSearch() {
        searchView.setVisibility(View.VISIBLE);
        fabMain.setVisibility(View.VISIBLE);
        fabList.setVisibility(View.VISIBLE);
        fabMap.setVisibility(View.VISIBLE);
    }

}
