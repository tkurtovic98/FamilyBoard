package com.hr.kurtovic.tomislav.familyboard.api

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember


interface FamilyMemberService {

    //    fun currentUserRef(): DocumentReference
    fun createUser(uid: String, userName: String, urlPicture: String): Task<Void>

    fun getUser(uid: String): Task<DocumentSnapshot>
    fun updateUserName(userName: String, uid: String, field: String): Task<Void>
    fun deleteUser(uid: String): Task<Void>
    val currentUser: FamilyMember
    val collection: String
}

class FamilyMemberServiceImpl : FamilyMemberService {

    override val collection
        get() = "members"

    //Collection reference
    private val usersCollection: CollectionReference
        get() = FirebaseFirestore.getInstance().collection(collection)

    override val currentUser: FamilyMember
        get() =
            getUser(FirebaseAuth.getInstance().currentUser!!.uid).result.let {
                it?.toObject(
                    FamilyMember::class.java
                )!!
            }

//    override fun currentUserRef(): DocumentReference {
//        return
//    }

    // CREATE
    override fun createUser(uid: String, userName: String, urlPicture: String): Task<Void> {
        val userToCreate = FamilyMember(uid, userName, urlPicture)
        return usersCollection.document(uid).set(userToCreate)
    }

    // GET
    override fun getUser(uid: String): Task<DocumentSnapshot> {
        return usersCollection.document(uid).get()
    }

    // UPDATE
    override fun updateUserName(userName: String, uid: String, field: String): Task<Void> {
        return usersCollection.document(uid).update(field, userName)
    }

    // DELETE
    override fun deleteUser(uid: String): Task<Void> {
        return usersCollection.document(uid).delete()
    }

}
