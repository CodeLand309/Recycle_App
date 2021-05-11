package com.example.recycle.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HistoryResponse{
    @SerializedName("status")
    private String status;

    @SerializedName("Products")
    ArrayList<HistoryItem> products;

    public String getStatus() {
        return status;
    }

    public ArrayList<HistoryItem> getItems() {
        return products;
    }
}

