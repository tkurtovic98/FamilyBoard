package com.hr.kurtovic.tomislav.familyboard.api

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.models.Message


interface FamilyMessageService {
    fun messages(): Query
    fun postMessage(message: Message): Task<DocumentReference>
    fun setMessageId(familyName: String, id: String): Task<Void>
}

class FamilyMessageServiceImpl(private val context: Context) : FamilyMessageService {

    private val familyName: String?
        get() = context.getSharedPreferences(
            context.getString(R.string.family_shared_prefs),
            Context.MODE_PRIVATE
        ).getString(R.string.family_name_key.toString(), "")

    private fun collection(): CollectionReference =
            ApiUtil.rootCollection(ApiUtil.FAMILIES_COLLECTION)
                    .document(familyName!!)
                    .collection(ApiUtil.MESSAGES_COLLECTION)

    override fun messages(): Query {
        return this.collection()
                .orderBy("dateCreated")
                .limit(50)
    }


    override fun postMessage(message: Message): Task<DocumentReference> {
        return this.collection()
                .add(message)
    }


    override fun setMessageId(familyName: String, id: String): Task<Void> {
        return this.collection()
                .document(id)
                .update("id", id)
    }
}
