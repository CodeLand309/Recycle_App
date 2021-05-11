package com.example.recycle.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CentreListResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("Centre")
    ArrayList<DisposeCentre>  centres;

    public String getStatus() {
        return status;
    }

    public ArrayList<DisposeCentre> getCentres() {
        return centres;
    }
}
