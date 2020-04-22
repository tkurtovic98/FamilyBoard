package com.hr.kurtovic.tomislav.familyboard.api

import com.google.firebase.firestore.CollectionReference

interface LocationService {

    fun retrieveLocation(name: String)
    fun setLocation(name: String)
    fun setMembersReference(collectionReference: CollectionReference)

}


class LocationServiceImpl() : LocationService {

    override fun retrieveLocation(name: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLocation(name: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setMembersReference(collectionReference: CollectionReference) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}