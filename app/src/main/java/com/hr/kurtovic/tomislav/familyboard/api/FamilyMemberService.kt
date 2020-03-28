package com.hr.kurtovic.tomislav.familyboard.api

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember


interface FamilyMemberService {

    fun currentUserRef(): DocumentReference
    fun createUser(uid: String, userName: String, urlPicture: String): Task<Void>
    fun getUser(uid: String): Task<DocumentSnapshot>
    fun updateUserName(userName: String, uid: String, field: String): Task<Void>
    fun deleteUser(uid: String): Task<Void>
    val currentMember: FamilyMember
    val membersCollection: String
}

class FamilyMemberServiceImpl : FamilyMemberService {

    override val membersCollection
        get() = "Members"

    override val currentMember: FamilyMember
        get() =
            getUser(FirebaseAuth.getInstance().currentUser!!.uid).result.let {
                it?.toObject(
                    FamilyMember::class.java
                )!!
            }

    override fun currentUserRef(): DocumentReference {
        return ApiUtil.collection(membersCollection).document(currentMember.uid!!)
    }

    // CREATE
    override fun createUser(uid: String, userName: String, urlPicture: String): Task<Void> {
        val userToCreate = FamilyMember(uid, userName, urlPicture)
        return ApiUtil.collection(membersCollection).document(uid).set(userToCreate)
    }

    // GET
    override fun getUser(uid: String): Task<DocumentSnapshot> {
        return ApiUtil.collection(membersCollection).document(uid).get()
    }

    // UPDATE
    override fun updateUserName(userName: String, uid: String, field: String): Task<Void> {
        return ApiUtil.collection(membersCollection).document(uid).update(field, userName)
    }

    // DELETE
    override fun deleteUser(uid: String): Task<Void> {
        return ApiUtil.collection(membersCollection).document(uid).delete()
    }

}
