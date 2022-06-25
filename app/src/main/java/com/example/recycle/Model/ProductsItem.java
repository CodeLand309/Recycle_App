package com.example.recycle.Model;

import com.google.gson.annotations.SerializedName;

public class ProductsItem {

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

    public boolean equals(ProductsItem product){
        if(product==null)
            return false;
        return  (product.getProductName().equals(this.getProductName()) ||
                product.getImage().equals(this.getImage()) ||
                product.getDescription().equals(this.getDescription()) ||
                product.getUserName().equals(this.getUserName()) ||
                product.getUserID() == this.getUserID() ||
                product.getPrice() == this.getPrice() ||
                product.getDate().equals(this.getDate()) ||
                product.getYears() == this.getYears() ||
                product.getStatus() == this.getStatus());
    }
}
