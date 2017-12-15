package com.bhavadeep.googleclustering.reftrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class RetrofitClass {

    GooglePlacesServices getService() {
        return service;
    }

    private GooglePlacesServices service;

    RetrofitClass() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/textsearch/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GooglePlacesServices.class);
    }
}
