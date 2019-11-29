package com.hr.kurtovic.tomislav.familyboard.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {

    private String ID;
    private String message;
    private Date dateCreated;
    private User userSender;
    private String urlImage;
    private boolean isAccepted = false;

    public Message() {
    }

    public Message(String message, User userSender) {
        this.message = message;
        this.userSender = userSender;

    }

    public Message(String message, User userSender, String urlImage) {
        this(message, userSender);
        this.urlImage = urlImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage){
        this.urlImage = urlImage;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
