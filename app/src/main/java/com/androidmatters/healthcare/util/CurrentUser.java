package com.androidmatters.healthcare.util;

public class CurrentUser {
    private String username;
    private String userId;
    private static CurrentUser instance;

    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        if(instance == null){
            instance = new CurrentUser();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
