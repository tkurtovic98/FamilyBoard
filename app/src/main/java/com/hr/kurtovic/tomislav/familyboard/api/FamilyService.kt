package com.hr.kurtovic.tomislav.familyboard.api

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.hr.kurtovic.tomislav.familyboard.models.Family
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember


interface FamilyService {

    fun addFamily(family: Family): Task<Void>
    fun addFamilyMember(familyName: String, familyMember: FamilyMember): Task<Void>
    fun addFamilyMember(familyName: String, familyMemberId: String): Task<Void>
    fun deleteFamilyMember(familyName: String, familyMember: FamilyMember): Task<Void>
    fun showFamilies(): CollectionReference

}

class FamilyServiceImpl : FamilyService {

    private val familiesCollection = ApiUtil.rootCollection(ApiUtil.FAMILIES_COLLECTION)


    override fun showFamilies(): CollectionReference {
        return familiesCollection
    }

    override fun addFamily(family: Family): Task<Void> =
            familiesCollection.document(family.name!!).set(family)

    override fun addFamilyMember(
        familyName: String,
        familyMember: FamilyMember
    ): Task<Void> =
            membersCollection(familyName, familyMember.uid!!).set(familyMember)

    override fun addFamilyMember(familyName: String, familyMemberId: String): Task<Void> =
            membersCollection(familyName, familyMemberId).set(mapOf("memberId" to familyMemberId))



    override fun deleteFamilyMember(familyName: String, familyMember: FamilyMember): Task<Void> =
            membersCollection(familyName, familyMember.uid!!).delete()

    private fun membersCollection(
        familyName: String,
        familyMemberId: String
    ): DocumentReference =
            familiesCollection.document(familyName).collection(ApiUtil.MEMBERS_COLLECTION)
                    .document(familyMemberId)

}