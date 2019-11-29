package com.hr.kurtovic.tomislav.familyboard.models;

import javax.annotation.Nullable;

public class User {

    private String uid;
    private String name;
    private String role;
    @Nullable private String urlPicture;


    public User(){}

    public User(String uid, String name, @Nullable String urlPicture) {
        this.uid = uid;
        this.name = name;
        this.urlPicture = urlPicture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
