package com.example.recycle.MainUI;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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

class ProductsItem {

    @SerializedName("Image")
    private String mProductImage;

    @SerializedName("Product ID")
    private int mProductID;

    @SerializedName("Product Name")
    private String mProductName;

    @SerializedName("user")
    private String mUserName;

    @SerializedName("Description")
    private String mDescription;

    @SerializedName("User ID")
    private int mUserID;

    @SerializedName("Status")
    private int mStatus;

    @SerializedName("Price")
    private int mPrice;

    @SerializedName("Years")
    private int mYears;

    @SerializedName("Date")
    private String mDate;

    public ProductsItem(String image, int productID, String product, String user,  String description, int userID, int price, int years, int status, String date) {
        mProductImage = image;
        mProductName = product;
        mDescription = description;
        mProductID = productID;
        mUserID = userID;
        mUserName = user;
        mPrice = price;
        mYears = years;
        mStatus = status;
        mDate = date;
    }

    public String getImage() {
        return mProductImage;
    }
    public String getProductName() {
        return mProductName;
    }
    public String getUserName(){
        return mUserName;
    }
    public String getDescription() {
        return mDescription;
    }
    public int getProductID(){
        return mProductID;
    }
    public int getUserID(){
        return mUserID;
    }
    public int getPrice(){
        return mPrice;
    }
    public int getYears(){
        return mYears;
    }
    public int getStatus(){return mStatus;}
    public String getDate() {
        return mDate;
    }
}


