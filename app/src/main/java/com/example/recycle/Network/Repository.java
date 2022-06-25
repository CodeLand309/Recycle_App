package com.example.recycle.Network;

import androidx.lifecycle.LiveData;

import com.example.recycle.Model.CentreListResponse;
import com.example.recycle.Model.DataResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private static RestApiInterface restApiInterface;
    private static Repository repository;

    private Repository(){
        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);
    }

    public static Repository getRepository(){
        if(repository==null) {
            repository = new Repository();
        }
        return repository;
    }

    public Call<ArrayList<DataResponse>> getProducts(int page_number, int item_count, String type, Integer id, String search){
        return restApiInterface.getProducts(page_number, item_count, type, id, search);
    }

    public Call<ArrayList<DataResponse>> getProductData(int page_number, int item_count, int id){
        return restApiInterface.getProductData(page_number, item_count, id);
    }

    public Call<ArrayList<DataResponse>> getProductDataNoID(int page_number, int item_count){
        return restApiInterface.getProductDataNoID(page_number, item_count);
    }

//    public void requestCentreData(int page_number, int item_count){
//        restApiInterface.getCentreData(page_number, item_count).removeObserve;
//    }

    public Call<ArrayList<CentreListResponse>> getCentreData(int page_number, int item_count, String search){
        return restApiInterface.getCentreData(page_number, item_count, search);
    }

//    public static LiveData<HistoryResponse> getHistory(int id){
//
//    }

//    public LiveData<User> getUserData(int id){
//
//    }

    public Call<ArrayList<DataResponse>> getUserProducts(int page, int items, int id){
        return restApiInterface.getUserProducts(page, items, id);
    }

    public static Call<ArrayList<DataResponse>> searchProductName( int id, String name){
        return restApiInterface.searchProductName(id, name);
    }

    public Call<ArrayList<CentreListResponse>> searchCentreName(String name){
        return restApiInterface.searchCentreName(name);
    }
//
//    public void registerUser(String name, String gender, String address, String image, String phone, int age){
//        //<User>
//    }
//
//    public void addProduct(String product, String description, int year, int price, String date, String image, String name, int user_id){
//        //<JsonElement>
//    }
//
//    public void editProduct(tring product, String description, int year, int price, String image, int product_id, int user_id, int flag){
//        //JsonElement
//    }
//
//    public void editPhone(String phone, int user_id){
//        //JsonElement
//    }
//
//    public void markSold(int product_id, int user_id){
//        //JsonElement
//    }
//
//    public void markReceived(int product_id, int user_id){
//        //JsonElement
//    }
//
//    public void editUser(String name, int user_id, int age, String address, String phone, String image, int flag){
//        //JsonElement
//    }
//
//    public void deleteUser(int user_id, String phone){
//        //JsonElement
//    }
//
//    public void deleteProduct(int id, String name){
//        //JsonElement
//    }

}
