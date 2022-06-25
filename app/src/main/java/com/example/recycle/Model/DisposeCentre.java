package com.example.recycle.Model;

import com.google.gson.annotations.SerializedName;

public class DisposeCentre {
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

    public boolean equals(DisposeCentre centre){
        if(centre == null)
            return false;
        return (centre.getCentreName().equals(this.getCentreName()) ||
                centre.getCentreAddress().equals(this.getCentreAddress()) ||
                centre.getCentrePhone().equals(this.getCentrePhone())) ||
                centre.getCentreImage().equals(this.getCentreImage());
    }
}
