package com.bhavadeep.googleclustering.reftrofit;

import com.bhavadeep.googleclustering.models.APIResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface GooglePlacesServices {
    @GET("json")
    Call<APIResult> MastersinUS(@Query("query") String searchString, @Query("region") String region,  @Query("key") String apiKey);


}
