package com.example.wgout;

import com.example.wgout.Data.UltraSrtFcst;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherUltraSrtFcstInterface {
    @GET("getUltraSrtFcst")
    Call<UltraSrtFcst>
    getData(@Query("serviceKey") String serviceKey,
            @Query("numOfRows") int numOfRows,
            @Query("dataType") String dataType,
            @Query("base_date") String base_date,
            @Query("base_time") String base_time,
            @Query("nx") int nx,
            @Query("ny") int ny);
}
