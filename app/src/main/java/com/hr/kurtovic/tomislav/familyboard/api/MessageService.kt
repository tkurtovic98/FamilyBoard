package com.hr.kurtovic.tomislav.familyboard.api

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.models.Message


interface MessageService {

    fun messages(familyName: String): Query
    fun postMessage(message: Message): Task<DocumentReference>
    fun setMessageId(familyName: String, id: String): Task<Void>
    val collection: String
}

class MessageServiceImpl : MessageService {

    override val collection: String
        get() = "messages"

    override fun messages(familyName: String): Query {
        return ChatService.chatCollection
                .document(familyName)
                .collection(collection)
                .orderBy("dateCreated")
                .limit(50)
    }

    override fun postMessage(message: Message): Task<DocumentReference> {
        return ChatService.chatCollection
                .document("main")
                .collection(collection)
                .add(message)
    }


    override fun setMessageId(familyName: String, id: String): Task<Void> {
        return ChatService.chatCollection
                .document(familyName)
                .collection(collection)
                .document(id)
                .update("id", id)
    }
}
