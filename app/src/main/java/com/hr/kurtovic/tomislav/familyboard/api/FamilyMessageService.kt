package com.hr.kurtovic.tomislav.familyboard.api

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.models.Message


interface FamilyMessageService {
    fun messages(familyName: String): Query
    fun postMessage(message: Message, familyName: String): Task<DocumentReference>
    fun setMessageId(familyName: String, id: String): Task<Void>
    val familiesCollection: String
}

class FamilyMessageServiceImpl : FamilyMessageService {

    override val familiesCollection
        get() = "Families"

    private fun collection(familyName: String): CollectionReference =
            ApiUtil.collection(familiesCollection)
                    .document(familyName)
                    .collection("messages")

    override fun messages(familyName: String): Query {
        2
        return this.collection(familyName)
                .orderBy("dateCreated")
                .limit(50)
    }

    override fun postMessage(message: Message, familyName: String): Task<DocumentReference> {
        return this.collection(familyName)
                .add(message)
    }


    override fun setMessageId(familyName: String, id: String): Task<Void> {
        return this.collection(familyName)
                .document(id)
                .update("id", id)
    }
}
