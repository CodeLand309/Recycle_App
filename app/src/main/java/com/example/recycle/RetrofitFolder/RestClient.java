package com.example.recycle.RetrofitFolder;

import android.content.res.Resources;

import com.example.recycle.BuildConfig;
import com.example.recycle.PrivateKeys;
import com.example.recycle.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static final String BASE_URL = PrivateKeys.BASE_URL;
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
