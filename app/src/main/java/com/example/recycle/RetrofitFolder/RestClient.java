package com.example.recycle.RetrofitFolder;

import android.content.Context;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

//    http://52.66.92.73:5000/
//    http://ec2-13-234-120-52.ap-south-1.compute.amazonaws.com:5000/

    public static final String BASE_URL = "http://ec2-13-234-120-52.ap-south-1.compute.amazonaws.com:5000/";
    public static Retrofit retrofit = null;
    public static Retrofit getRetrofit() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
