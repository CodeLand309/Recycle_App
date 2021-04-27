package com.example.recycle.MainUI;

public class ChatUsers {
    private String Name;
    private String Phone;
    private int ID;
    private String Image;

    public ChatUsers(String name, String phone, int id, String image) {
        Name = name;
        Phone = phone;
        ID = id;
        Image = image;
    }

    public ChatUsers() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getId() {
        return ID;
    }

    public void setId(int id) {
        ID = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
