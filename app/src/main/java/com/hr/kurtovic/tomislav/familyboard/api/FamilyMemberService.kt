package com.hr.kurtovic.tomislav.familyboard.api

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember


interface FamilyMemberService {
    fun currentMemberRef(): DocumentReference
    fun createMember(uid: String, memberName: String, urlPicture: String): Task<Void>
    fun getMember(uid: String): Task<DocumentSnapshot>
    fun updateMemberName(memberName: String, uid: String, field: String): Task<Void>
    fun deleteMember(uid: String): Task<Void>
    fun addFamily(uid: String, familyName: String): Task<Void>
    fun families(): CollectionReference
    val currentMemberId: String
}

class FamilyMemberServiceImpl : FamilyMemberService {

    private val membersCollection
        get() = ApiUtil.MEMBERS_COLLECTION

    override val currentMemberId: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    override fun currentMemberRef(): DocumentReference = ApiUtil.rootCollection(membersCollection)
            .document(currentMemberId)


    // CREATE
    override fun createMember(uid: String, memberName: String, urlPicture: String): Task<Void> {
        val memberToCreate = FamilyMember(uid = uid, name = memberName, urlPicture = urlPicture)
        return ApiUtil.rootCollection(membersCollection).document(uid).set(memberToCreate)
    }

    // GET
    override fun getMember(uid: String): Task<DocumentSnapshot> =
            ApiUtil.rootCollection(membersCollection).document(uid).get()


    // UPDATE
    override fun updateMemberName(memberName: String, uid: String, field: String): Task<Void> =
            ApiUtil.rootCollection(membersCollection).document(uid).update(field, memberName)


    // DELETE
    override fun deleteMember(uid: String): Task<Void> = ApiUtil.rootCollection(membersCollection)
            .document(uid).delete()


    override fun addFamily(uid: String, familyName: String): Task<Void> =
            ApiUtil.rootCollection(membersCollection).document(uid)
                    .collection(ApiUtil.FAMILIES_COLLECTION)
                    .document(familyName).set(mapOf("familyName" to familyName))

    override fun families(): CollectionReference = ApiUtil.rootCollection(membersCollection)
            .document(currentMemberId).collection(ApiUtil.FAMILIES_COLLECTION)


}
