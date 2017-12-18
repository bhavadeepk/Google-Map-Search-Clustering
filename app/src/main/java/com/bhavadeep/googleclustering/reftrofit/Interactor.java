package com.bhavadeep.googleclustering.reftrofit;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.bhavadeep.googleclustering.BuildConfig;
import com.bhavadeep.googleclustering.models.APIResult;
import com.bhavadeep.googleclustering.models.Result;
import com.bhavadeep.googleclustering.presenter.IModelInteractor;
import com.bhavadeep.googleclustering.presenter.OnLoadFinishListener;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class Interactor implements Callback<APIResult>, IModelInteractor {

    private RetrofitClass rf = new RetrofitClass();
    private OnLoadFinishListener listener;
    private final String region = "us";
    private final String  API_KEY = BuildConfig.GOOGLE_MAPS_API_KEY_UNREGISTERED ;
    private String query = "";
    Realm realm;

    public Interactor(OnLoadFinishListener loadFinshListener) {
        listener = loadFinshListener;
        realm = Realm.getDefaultInstance();
    }


    @Override
    public void onResponse(@NonNull Call<APIResult> call, @NonNull Response<APIResult> response) {
        if(response.body()!=null){
            if (response.isSuccessful()) {
                APIResult apiResult = response.body();
                if (apiResult != null) {
                    if (apiResult.getStatus().equals("OK")) {
                        final List<Result> results = apiResult.getResults();
                        for (Result result : results) {
                            result.setQuery(query);
                        }

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(@NonNull Realm realm) {
                                realm.insertOrUpdate(results);
                            }
                        });
                        listener.OnLoadFinish(results);
                    } else {
                        Log.e("API ERROR :", apiResult.getStatus());
                        listener.OnLoadFailed(apiResult.getStatus());
                    }
                }
            } else {
                listener.OnLoadFailed("Response Unsuccessful");
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
        this.query = query;
        RealmResults<Result> results = realm.where(Result.class).equalTo("query", query).findAll();
        if (results.isEmpty()) {
            rf.getService().MastersinUS(query, region, API_KEY).enqueue(this);
        } else {
            listener.OnLoadFinish(results);
        }
    }
}
