package com.example.recycle.MainUI;

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

class DisposeCentre {

    @SerializedName("Image")
    private String mCentreImage;

    @SerializedName("Centre ID")
    private int mCentreID;

    @SerializedName("Centre Name")
    private String mCentreName;

    @SerializedName("Centre Address")
    private String mCentreAddress;

    @SerializedName("Phone")
    private String mCentrePhone;

    public DisposeCentre(String image, String phone, String name,  String address, int centreID) {
        mCentreImage = image;
        mCentreName = name;
        mCentreAddress = address;
        mCentreID = centreID;
        mCentrePhone = phone;
    }
    public String getCentreImage() {
        return mCentreImage;
    }
    public String getCentreName() {
        return mCentreName;
    }
    public String getCentrePhone(){
        return mCentrePhone;
    }
    public String getCentreAddress() {
        return mCentreAddress;
    }
    public int getCentreID(){
        return mCentreID;
    }
}
