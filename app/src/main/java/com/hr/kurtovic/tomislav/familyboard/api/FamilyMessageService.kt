package com.hr.kurtovic.tomislav.familyboard.api

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.models.Message
import kotlinx.coroutines.tasks.await


interface FamilyMessageService {
    fun messages(): Query
    suspend fun getMessage(messageId: String): Message?
    fun postMessage(message: Message): Task<DocumentReference>
    fun setMessageId(familyName: String, id: String): Task<Void>
    suspend fun setMessageAccepted(messageId: String, memberWhoAcceptedId: String): Void?
    suspend fun deleteMessage(messageId: String): Void?
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

    override suspend fun getMessage(messageId: String): Message? =
            this.collection().document(messageId).get().await().toObject<Message>()


    override fun postMessage(message: Message): Task<DocumentReference> {
        return this.collection()
                .add(message)
    }


    override fun setMessageId(familyName: String, id: String): Task<Void> {
        return this.collection()
                .document(id)
                .update("id", id)
    }

    override suspend fun setMessageAccepted(
        messageId: String,
        memberWhoAcceptedId: String
    ): Void? =
            this.collection()
                    .document(messageId)
                    .update(mapOf("accepted" to true, "memberWhoAcceptedId" to memberWhoAcceptedId))
                    .await()

    override suspend fun deleteMessage(messageId: String): Void? =
            this.collection()
                    .document(messageId)
                    .delete()
                    .await()
}
