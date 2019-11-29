package com.hr.kurtovic.tomislav.familyboard.models;

public class ChatRoom {

    private String ID;
    private String name;

    public ChatRoom(){

    }

    public ChatRoom(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
