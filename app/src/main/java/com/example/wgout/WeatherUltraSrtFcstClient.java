package com.example.wgout;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherUltraSrtFcstClient {private static WeatherUltraSrtFcstClient instance = null;
    private static WeatherUltraSrtFcstInterface weatherUltraSrtFcstInterface;
    private static String baseUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/";

    private WeatherUltraSrtFcstClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        weatherUltraSrtFcstInterface = retrofit.create(WeatherUltraSrtFcstInterface.class);
    }

    public static WeatherUltraSrtFcstClient getInstance(){
        if(instance == null){
            instance = new WeatherUltraSrtFcstClient();
        }
        return instance;
    }

    public static WeatherUltraSrtFcstInterface getUltraSrtFcstInterface(){
        return weatherUltraSrtFcstInterface;
    }
}