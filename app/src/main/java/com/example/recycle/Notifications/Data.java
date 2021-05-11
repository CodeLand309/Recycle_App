package com.example.recycle.Notifications;

public class Data {
    private String user;
    private String name;
    private int icon;
    private String body;
    private String title;
    private String receiver;

    public Data(String user, String name, int icon, String body, String title, String receiver) {
        this.user = user;
        this.name = name;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.receiver = receiver;
    }

    public Data() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
