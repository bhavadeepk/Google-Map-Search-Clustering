package com.bhavadeep.googleclustering.reftrofit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bhavadeep.googleclustering.BuildConfig;
import com.bhavadeep.googleclustering.models.APIResult;
import com.bhavadeep.googleclustering.models.Result;
import com.bhavadeep.googleclustering.presenter.IModelInteractor;
import com.bhavadeep.googleclustering.presenter.OnLoadFinishListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class Interactor implements Callback<APIResult>, IModelInteractor {

    private RetrofitClass rf = new RetrofitClass();
    private OnLoadFinishListener listener;
    private final String region = "us";
    private final String  API_KEY = BuildConfig.GOOGLE_MAPS_API_KEY_UNREGISTERED ;

    public Interactor(OnLoadFinishListener loadFinshListener) {
        listener = loadFinshListener;
    }


    @Override
    public void onResponse(@NonNull Call<APIResult> call, @NonNull Response<APIResult> response) {
        if(response.body()!=null){
            APIResult apiResult = response.body();
            if (apiResult != null){
                if(apiResult.getStatus().equals("OK")) {
                    List<Result> results = apiResult.getResults();
                    listener.OnLoadFinish(results);
                }
                else
                {
                    Log.e("API ERROR :", apiResult.getStatus());
                    listener.OnLoadFailed(apiResult.getStatus());
                }

            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<APIResult> call, @NonNull Throwable t) {
        Log.e("Retrofit Failure :", t.getMessage());
        listener.OnLoadFailed(t.getMessage());
    }

    @Override
    public void loadResults(String query) {
        rf.getService().MastersinUS(query, region, API_KEY).enqueue(this);
    }
}
