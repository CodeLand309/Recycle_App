package com.example.recycle.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("search")
    private String search;

    @SerializedName("size")
    private Integer searchResultSize;

    @SerializedName("Products")
    ArrayList<ProductsItem> products;

    public String getStatus() {
        return status;
    }

    public String getSearch() {
        return search;
    }

    public Integer getSearchResultSize() {
        return searchResultSize;
    }

    public ArrayList<ProductsItem> getItems() {
        return products;
    }

}


