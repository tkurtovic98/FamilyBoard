package com.hr.kurtovic.tomislav.familyboard.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hr.kurtovic.tomislav.familyboard.models.User;


public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    //Collection reference

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // CREATE

    public static Task<Void> createUser(String uid, String userName, String urlPicture){
        User userToCreate = new User(uid, userName, urlPicture);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // GET

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // UPDATE
    public static Task<Void> updateUserName(String userName, String uid){
        return  UserHelper.getUsersCollection().document(uid).update("username", userName);
    }

    // DELETE
    public static Task<Void> deleteUser(String uid){
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
