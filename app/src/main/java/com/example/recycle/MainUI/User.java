package com.example.recycle.MainUI;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("Image")
    private String mUserImage;

    @SerializedName("User ID")
    private int mUserID;

    @SerializedName("User Name")
    private String mUserName;

    @SerializedName("Gender")
    private String mGender;

    @SerializedName("Address")
    private String mAddress;

    @SerializedName("Phone Number")
    private String mPhone;

    @SerializedName("Age")
    private int mAge;

    public User(String user_image, int userID, String name, String phone, String gender, String address, int age) {
        mUserImage = user_image;
        mUserName = name;
        mGender = gender;
        mAddress = address;
        mUserID = userID;
        mPhone = phone;
        mAge = age;
    }
    public String getImage() {
        return mUserImage;
    }
    public String getGender() {
        return mGender;
    }
    public String getUserName(){
        return mUserName;
    }
    public String getAddress() {
        return mAddress;
    }
    public String getPhone(){
        return mPhone;
    }
    public int getUserID(){
        return mUserID;
    }
    public int getAge(){
        return mAge;
    }
}
