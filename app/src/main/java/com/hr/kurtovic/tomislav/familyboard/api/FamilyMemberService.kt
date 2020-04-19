package com.hr.kurtovic.tomislav.familyboard.api

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember


interface FamilyMemberService {

    fun currentMemberRef(): DocumentReference
    fun createMember(uid: String, memberName: String, urlPicture: String): Task<Void>
    fun getMember(uid: String): Task<DocumentSnapshot>
    fun updateMemberName(memberName: String, uid: String, field: String): Task<Void>
    fun deleteMember(uid: String): Task<Void>
    val currentMemberId: String
}

class FamilyMemberServiceImpl : FamilyMemberService {

    private val membersCollection
        get() = ApiUtil.MEMBERS_COLLECTION

    override val currentMemberId: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    override fun currentMemberRef(): DocumentReference {
        return ApiUtil.collection(membersCollection).document(currentMemberId)
    }

    // CREATE
    override fun createMember(uid: String, memberName: String, urlPicture: String): Task<Void> {
        val memberToCreate = FamilyMember(uid = uid, name = memberName, urlPicture = urlPicture)
        return ApiUtil.collection(membersCollection).document(uid).set(memberToCreate)
    }

    // GET
    override fun getMember(uid: String): Task<DocumentSnapshot> {
        return ApiUtil.collection(membersCollection).document(uid).get()
    }

    // UPDATE
    override fun updateMemberName(memberName: String, uid: String, field: String): Task<Void> {
        return ApiUtil.collection(membersCollection).document(uid).update(field, memberName)
    }

    // DELETE
    override fun deleteMember(uid: String): Task<Void> {
        return ApiUtil.collection(membersCollection).document(uid).delete()
    }

}
