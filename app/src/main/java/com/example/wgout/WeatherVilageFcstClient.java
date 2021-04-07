package com.example.wgout;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherVilageFcstClient {private static WeatherVilageFcstClient instance = null;
    private static WeatherVilageFcstInterface weatherVilageFcstInterface;
    private static String baseUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/";

    private WeatherVilageFcstClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        weatherVilageFcstInterface = retrofit.create(WeatherVilageFcstInterface.class);
    }

    public static WeatherVilageFcstClient getInstance(){
        if(instance == null){
            instance = new WeatherVilageFcstClient();
        }
        return instance;
    }

    public static WeatherVilageFcstInterface getVilageFcstInterface(){
        return weatherVilageFcstInterface;
    }
}