package com.hr.kurtovic.tomislav.familyboard.api

import com.google.android.gms.tasks.Task
import com.hr.kurtovic.tomislav.familyboard.models.Family
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember


interface FamilyService {

    fun addFamily(family: Family): Task<Void>
    fun addFamilyMember(familyName: String, familyMember: FamilyMember): Task<Void>
    fun deleteFamilyMember(familyName: String, familyMember: FamilyMember): Task<Void>

}

class FamilyServiceImpl : FamilyService {

    private val familiesCollection = ApiUtil.collection(ApiUtil.FAMILIES_COLLECTION)

    override fun addFamily(family: Family): Task<Void> = familiesCollection.document(family.name!!).set(
        family
    )

    override fun addFamilyMember(
        familyName: String,
        familyMember: FamilyMember
    ): Task<Void> =
            familiesCollection.document(familyName).collection(ApiUtil.MEMBERS_COLLECTION)
                    .document(familyMember.uid!!).set(familyMember)


    override fun deleteFamilyMember(familyName: String, familyMember: FamilyMember): Task<Void> =
            familiesCollection.document(familyName).collection(ApiUtil.MEMBERS_COLLECTION)
                    .document(familyMember.uid!!).delete()

}