package com.example.recycle.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CentreListResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("search")
    private String search;

    @SerializedName("size")
    private Integer searchResultSize;

    @SerializedName("Centre")
    ArrayList<DisposeCentre>  centres;

    public String getStatus() {
        return status;
    }

    public String getSearch() {
        return search;
    }

    public Integer getSearchResultSize() {
        return searchResultSize;
    }

    public ArrayList<DisposeCentre> getCentres() {
        return centres;
    }
}
