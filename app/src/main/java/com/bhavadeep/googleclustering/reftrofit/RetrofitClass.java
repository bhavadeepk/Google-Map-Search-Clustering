package com.bhavadeep.googleclustering.reftrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class RetrofitClass {

    GooglePlacesServices getService() {
        return service;
    }

    private GooglePlacesServices service;
    Gson gson = new GsonBuilder()
            .create();

    OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

    RetrofitClass() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/textsearch/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(httpClientBuilder.build())
                .build();
        service = retrofit.create(GooglePlacesServices.class);
    }
}
