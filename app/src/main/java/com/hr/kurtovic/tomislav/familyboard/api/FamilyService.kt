package com.hr.kurtovic.tomislav.familyboard.api

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.hr.kurtovic.tomislav.familyboard.models.Family
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember
import kotlinx.coroutines.tasks.await


interface FamilyService {

    suspend fun addFamily(family: Family): Void?
    fun addFamilyMember(familyName: String, familyMember: FamilyMember): Task<Void>
    suspend fun addFamilyMember(family: Family, familyMemberId: String): Void?
    fun deleteFamilyMember(familyName: String, familyMember: FamilyMember): Task<Void>
    fun showFamilies(): CollectionReference

}

class FamilyServiceImpl : FamilyService {

    private val familiesCollection = ApiUtil.rootCollection(ApiUtil.FAMILIES_COLLECTION)


    override fun showFamilies(): CollectionReference {
        return familiesCollection
    }

    override suspend fun addFamily(family: Family): Void? =
            familiesCollection.document(family.name!!).set(family).await()

    override fun addFamilyMember(
        familyName: String,
        familyMember: FamilyMember
    ): Task<Void> =
            membersCollection(familyName, familyMember.uid!!).set(familyMember)

    override suspend fun addFamilyMember(family: Family, familyMemberId: String): Void? =
            membersCollection(
                family.name!!,
                familyMemberId
            ).set(mapOf("memberId" to familyMemberId)).await()


    override fun deleteFamilyMember(familyName: String, familyMember: FamilyMember): Task<Void> =
            membersCollection(familyName, familyMember.uid!!).delete()

    private fun membersCollection(
        familyName: String,
        familyMemberId: String
    ): DocumentReference =
            familiesCollection.document(familyName).collection(ApiUtil.MEMBERS_COLLECTION)
                    .document(familyMemberId)

}