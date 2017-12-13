package com.bhavadeep.googleclustering.Reftrofit;

import com.bhavadeep.googleclustering.Models.APIResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ${Bhavadeep} on 12/11/2017.
 */

public interface GooglePlacesServices {
    @GET("json")
    Call<APIResult> listMasters(@Query("query") String searchString, @Query("location") String latLng, @Query("radius") int radius, @Query("key") String apiKey);
    @GET("json")
    Call<APIResult> MastersinUS(@Query("query") String searchString, @Query("region") String region,  @Query("key") String apiKey);


}
