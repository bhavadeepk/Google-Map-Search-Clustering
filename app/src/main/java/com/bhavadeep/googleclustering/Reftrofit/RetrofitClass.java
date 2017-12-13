package com.bhavadeep.googleclustering.Reftrofit;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ${Bhavadeep} on 12/11/2017.
 */

public class RetrofitClass {

    Retrofit retrofit;

    public GooglePlacesServices getService() {
        return service;
    }

    GooglePlacesServices service;

    public RetrofitClass() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/textsearch/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GooglePlacesServices.class);
    }
}
