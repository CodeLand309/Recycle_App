package com.example.recycle.RetrofitFolder;

import com.example.recycle.MainUI.CentreListResponse;
import com.example.recycle.MainUI.DataResponse;
import com.example.recycle.MainUI.User;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApiInterface {

    @GET("/getProductData")
    Call<ArrayList<DataResponse>> getProductData(@Query("page_number") int page, @Query("item_count") int items, @Query("id") int id);

    @GET("/getCentreData")
    Call<ArrayList<CentreListResponse>> getCentreData(@Query("page_number") int page, @Query("item_count") int items);

    @GET("/getUserData")
    Call<User> getUserData(@Query("id") int id);

    @FormUrlEncoded
    @POST("/registerUser")
    Call<User> registerUser(@Field("name") String name, @Field("gender") String gender, @Field("address") String address, @Field("image") String image, @Field("phone") String phone, @Field("age") int age);

//    @FormUrlEncoded
//    @POST("/post")
//    Call<Student> postData(@Field("roll") int roll, @Field("name") String name, @Field("image") String image);
//
//    @FormUrlEncoded
//    @PUT("/put")
//    Call<Student> putData(@Field("id") int id, @Field("roll") int roll, @Field("name") String name, @Field("image") String image);
//
//    @FormUrlEncoded
//    @PATCH("patch")
//    Call<Student> patchData(@Field("roll") int roll, @Field("value") String value, @Field("key") int key);
//
//    @DELETE("delete")
//    Call<void> deleteUser(@Query("id") int id);
}
