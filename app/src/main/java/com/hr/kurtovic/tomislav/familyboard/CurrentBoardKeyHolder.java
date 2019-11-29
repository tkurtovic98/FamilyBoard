package com.hr.kurtovic.tomislav.familyboard;

import com.hr.kurtovic.tomislav.familyboard.models.User;

public class CurrentBoardKeyHolder {
    private static CurrentBoardKeyHolder instance;

    private String currentKey = "main";

    private User currentUser;

    public static CurrentBoardKeyHolder  getInstance(){
        if(instance == null){
            instance = new CurrentBoardKeyHolder ();
        }

        return instance;
    }

    public String getCurrentKey() {
        return currentKey;
    }

    public void setCurrentKey(String currentKey) {
        this.currentKey = currentKey;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
