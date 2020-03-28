package com.hr.kurtovic.tomislav.familyboard.api

import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember


interface FamilyService {

    fun addFamilyMember(familyMember: FamilyMember)
    fun deleteFamilyMember(familyMember: FamilyMember)
    fun updateFamilyName(familyName: String)
    fun changeCurrentFamilyName()

}

class FamilyServiceImpl : FamilyService {
    override fun addFamilyMember(familyMember: FamilyMember) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteFamilyMember(familyMember: FamilyMember) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateFamilyName(familyName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeCurrentFamilyName() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}