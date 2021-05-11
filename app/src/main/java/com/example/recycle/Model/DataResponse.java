package com.example.recycle.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("Products")
    ArrayList<ProductsItem> products;

    public String getStatus() {
        return status;
    }

    public ArrayList<ProductsItem> getItems() {
        return products;
    }

}


