package com.example.recycle.Network;

import androidx.lifecycle.LiveData;

import com.example.recycle.Model.CentreListResponse;
import com.example.recycle.Model.DataResponse;
import com.example.recycle.Model.User;
import com.example.recycle.Model.HistoryResponse;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApiInterface {

    @GET("/getProducts")
    Call<ArrayList<DataResponse>> getProducts(@Query("page_number") int page, @Query("item_count") int items, @Header("type") String type, @Header("id") Integer id, @Header("search") String search);

    @GET("/getProductData")
    Call<ArrayList<DataResponse>> getProductData(@Query("page_number") int page, @Query("item_count") int items, @Header("id") int id);

    @GET("/getProductDataNoID")
    Call<ArrayList<DataResponse>> getProductDataNoID(@Query("page_number") int page, @Query("item_count") int items);

    @GET("/getCentreData")
    Call<ArrayList<CentreListResponse>> getCentreData(@Query("page_number") int page, @Query("item_count") int items, @Query("search") String search);

    @GET("/getHistory")
    Call<ArrayList<HistoryResponse>> getHistory(@Query("id") int id);

    @GET("/getUserData")
    Call<User> getUserData(@Query("id") int id);

    @GET("/getUserProduct")
    Call<ArrayList<DataResponse>> getUserProducts(@Query("page_number") int page, @Query("item_count") int items, @Query("id") int id);

    @GET("/searchProductName")
    Call<ArrayList<DataResponse>> searchProductName(@Query("id") int id, @Query("search_word") String name);

    @GET("/searchCentreName")
    Call<ArrayList<CentreListResponse>> searchCentreName(@Query("search_word") String name);

    @FormUrlEncoded
    @POST("/registerUser")
    Call<User> registerUser(@Field("name") String name, @Field("gender") String gender, @Field("address") String address, @Field("image") String image, @Field("phone") String phone, @Field("age") int age);

    @FormUrlEncoded
    @POST("/addProduct")
    Call<JsonElement> addProduct(@Field("product") String product, @Field("description") String description, @Field("year") int year, @Field("price") int price, @Field("date") String date, @Field("image") String image, @Field("name") String name, @Field("id") int user_id);

    @FormUrlEncoded
    @PATCH("/editProduct")
    Call<JsonElement> editProduct(@Field("product") String product, @Field("description") String description, @Field("year") int year, @Field("price") int price, @Field("image") String image, @Field("product_id") int product_id, @Field("user_id") int user_id, @Field("flag") int flag);

    @PATCH("/editPhone")
    Call<JsonElement> editPhone(@Query("phone") String phone, @Query("user_id") int user_id);

    @PATCH("/markSold")
    Call<JsonElement> markSold(@Query("product_id") int product_id, @Query("user_id") int user_id);

    @PATCH("/markReceived")
    Call<JsonElement> markReceived(@Query("product_id") int product_id, @Query("user_id") int user_id);

    @FormUrlEncoded
    @PATCH("/editUser")
    Call<JsonElement> editUser(@Field("name") String name, @Field("user_id") int user_id, @Field("age") int age, @Field("address") String address, @Field("phone") String phone, @Field("image") String image, @Field("flag") int flag);

    @DELETE("/deleteUser")
    Call<JsonElement> deleteUser(@Query("user_id") int user_id, @Query("phone") String phone);

    @DELETE("/deleteProduct")
    Call<JsonElement> deleteProduct(@Query("product_id") int id, @Query("name") String name);
}
