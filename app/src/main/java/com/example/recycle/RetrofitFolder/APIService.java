package com.example.recycle.RetrofitFolder;

import com.example.recycle.Notifications.MyResponse;
import com.example.recycle.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAn60VH9k:APA91bEKiHgwP3nRnrPO9UjuvkdvOqkHH5I6CPlWc45GpQcOt2_y640v5FI0r-da1LKJT7-WoB72nuoTVF95uA1bmUUgjOnABUn1k6ZEGe9h3858DxH2FEkC_qlh4B07PS0ubd5GRBcZ"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
