package com.bhavadeep.googleclustering.Presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bhavadeep.googleclustering.Models.APIResult;
import com.bhavadeep.googleclustering.Models.Result;
import com.bhavadeep.googleclustering.Reftrofit.RetrofitClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by ${Bhavadeep} on 12/12/2017.
 */



public class Interactor implements Callback<APIResult>, IModelInteractor {

    RetrofitClass rf = new RetrofitClass();
    OnLoadFinshListener listener;
    String region = "us";
    private final String  API_KEY = "AIzaSyAgmdsFiMKjxIAxrP-DdoFO98hBWboZ12g";

    Interactor(OnLoadFinshListener loadFinshListener) {
        listener = loadFinshListener;
    }


    @Override
    public void onResponse(@NonNull Call<APIResult> call, @NonNull Response<APIResult> response) {
        if(response.body()!=null){
            APIResult apiResult = response.body();
            if (apiResult != null){
                if(apiResult.getStatus().equals("OK")) {
                    List<Result> results = apiResult.getResults();
                    listener.OnLoadFinsh(results);
                }
                else
                {
                    Log.e("API ERROR :", apiResult.getStatus());
                    listener.OnLoadFailed();
                }

            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<APIResult> call, @NonNull Throwable t) {
        Log.e("Retrofit Failure :", t.getMessage());
        listener.OnLoadFailed();
    }

    @Override
    public void loadResults(String query) {
        rf.getService().MastersinUS(query, region, API_KEY).enqueue(this);
    }
}
