package com.hr.kurtovic.tomislav.familyboard.api

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hr.kurtovic.tomislav.familyboard.models.User


object UserHelper {
    private const val COLLECTION_NAME = "users"

    //Collection reference

    private val usersCollection: CollectionReference
        get() = FirebaseFirestore.getInstance().collection(COLLECTION_NAME)

    // CREATE
    fun createUser(uid: String, userName: String, urlPicture: String): Task<Void> {
        val userToCreate = User(uid, userName, urlPicture)
        return usersCollection.document(uid).set(userToCreate)
    }

    // GET
    fun getUser(uid: String): Task<DocumentSnapshot> {
        return usersCollection.document(uid).get()
    }

    // UPDATE
    fun updateUserName(userName: String, uid: String): Task<Void> {
        return usersCollection.document(uid).update("username", userName)
    }

    // DELETE
    fun deleteUser(uid: String): Task<Void> {
        return usersCollection.document(uid).delete()
    }

}
