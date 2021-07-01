package com.example.wgout;

import com.example.wgout.Data.Navigation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NavigationInterface {

    @GET("map-direction/v1/driving")
    Call<Navigation>
    getNavigation(@Query("start") String start,
                  @Query("goal") String goal,
                  @Query("X-NCP-APIGW-API-KEY-ID") String KEY_ID,
                  @Query("X-NCP-APIGW-API-KEY") String KEY);
}
