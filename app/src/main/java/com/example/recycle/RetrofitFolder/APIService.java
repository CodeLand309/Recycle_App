package com.example.recycle.RetrofitFolder;

import com.example.recycle.Notifications.MyResponse;
import com.example.recycle.Notifications.Sender;
import com.example.recycle.PrivateKeys;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=" + PrivateKeys.Key
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}

