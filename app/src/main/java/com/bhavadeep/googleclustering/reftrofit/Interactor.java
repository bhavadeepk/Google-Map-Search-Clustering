package com.bhavadeep.googleclustering.reftrofit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bhavadeep.googleclustering.BuildConfig;
import com.bhavadeep.googleclustering.models.APIResult;
import com.bhavadeep.googleclustering.models.Result;
import com.bhavadeep.googleclustering.presenter.IModelInteractor;

import java.util.List;

import de.greenrobot.event.EventBus;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class Interactor implements Callback<APIResult>, IModelInteractor {

    private RetrofitClass rf = new RetrofitClass();
    private final String region = "us";
    private final String  API_KEY = BuildConfig.GOOGLE_MAPS_API_KEY_UNREGISTERED ;
    private String query = "";
    private Realm realm;
    private RealmResults<Result> dbResults;
    private EventBus bus;

    public Interactor() {
        realm = Realm.getDefaultInstance();
        bus = EventBus.getDefault();
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
                        bus.post(apiResult.getStatus());
                    }
                }
            } else {
                bus.post("Response Unsuccessful");
            }
            dbResults = realm.where(Result.class).equalTo("query", query).findAll();
            bus.post(dbResults);
        }
    }

    @Override
    public void onFailure(@NonNull Call<APIResult> call, @NonNull Throwable t) {
        dbResults = realm.where(Result.class).equalTo("query", query).findAll();
        if (dbResults.isEmpty()) {
            bus.post("No local DB Results\n" + t.getMessage());
        } else {
            bus.post(dbResults);
            bus.post(t.getMessage());
        }

    }

    @Override
    public void loadResults(String query) {
        this.query = query.toLowerCase();
        rf.getService().MastersinUS(query, region, API_KEY).enqueue(this);

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        realm.close();
    }
}
