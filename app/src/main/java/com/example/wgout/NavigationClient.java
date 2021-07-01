package com.example.wgout;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class  NavigationClient {
    private static NavigationClient instance = null;
    private static NavigationInterface navigationInterface;
    private static String baseUrl = "https://naveropenapi.apigw.ntruss.com/";

    private NavigationClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        navigationInterface = retrofit.create(NavigationInterface.class);
    }

    public static NavigationClient getInstance(){
        if(instance == null){
            instance = new NavigationClient();
        }
        return instance;
    }

    public static NavigationInterface getNavigationInterface(){
        return navigationInterface;
    }
}
