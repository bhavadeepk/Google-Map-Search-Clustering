package com.bhavadeep.googleclustering.reftrofit;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.bhavadeep.googleclustering.BuildConfig;
import com.bhavadeep.googleclustering.models.APIResult;
import com.bhavadeep.googleclustering.models.Geometry;
import com.bhavadeep.googleclustering.models.Location;
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
    private Realm realm;
    private RealmResults<Result> dbResults;

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
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(@NonNull Realm realm) {
                                realm.deleteAll();
                                for (int i = 0; i < results.size(); i++) {
                                    Result result = results.get(i);
                                    result.setQuery(query.toLowerCase());
                                    result.getGeometry().setuID(result.getId());
                                    result.getGeometry().getLocation().setuGID(result.getId());
                                }
                                realm.insertOrUpdate(results);
                            }
                        });
                    } else {
                        Log.e("API ERROR :", apiResult.getStatus());
                        listener.OnLoadFailed(apiResult.getStatus());
                    }
                }
            } else {
                listener.OnLoadFailed("Response Unsuccessful");
            }
            dbResults = realm.where(Result.class).equalTo("query", query).findAll();
            listener.OnLoadFinish(dbResults);
        }
    }

    @Override
    public void onFailure(@NonNull Call<APIResult> call, @NonNull Throwable t) {
        Log.e("Retrofit Failure :", t.getMessage());
        dbResults = realm.where(Result.class).equalTo("query", query).findAll();
        if (dbResults.isEmpty()) {
            listener.OnLoadFailed("No local DB Results\n" + t.getMessage());
        } else {
            listener.OnLoadFinish(dbResults);
            listener.OnLoadFailed(t.getMessage());
        }

    }

    @Override
    public void loadResults(String query) {
        this.query = query.toLowerCase();
        rf.getService().MastersinUS(query, region, API_KEY).enqueue(this);

    }
}
