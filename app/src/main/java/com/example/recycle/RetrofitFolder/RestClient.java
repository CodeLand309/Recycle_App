package com.example.recycle.RetrofitFolder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static final String BASE_URL = "http://192.168.29.202:5000/";
    public static Retrofit retrofit = null;
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            //OkHttpClient.Builder client = new OkHttpClient.Builder();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    //             .client(client.build())
                    .build();
        }
        return retrofit;
    }
}
