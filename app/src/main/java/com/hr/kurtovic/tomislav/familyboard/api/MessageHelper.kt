package com.hr.kurtovic.tomislav.familyboard.api

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.models.Message
import com.hr.kurtovic.tomislav.familyboard.models.User


object MessageHelper {

    private const val COLLECTION_NAME = "message"

    fun getAllMessageForChat(chat: String): Query {
        return ChatHelper.chatCollection
                .document(chat)
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated")
                .limit(50)
    }

    fun createMessageForChat(textMessage: String, chat: String, userSender: User): Task<DocumentReference> {
        //Create message object
        val message = Message(textMessage, userSender)

        return ChatHelper.chatCollection
                .document(chat)
                .collection(COLLECTION_NAME)
                .add(message)
    }

    fun setMessageAccepted(message: Message, chat: String): Task<Void> {

        return ChatHelper.chatCollection
                .document(chat)
                .collection(COLLECTION_NAME)
                .document(message.id!!)
                .update("accepted", true)
    }

    fun createMessageWithImageForChat(urlImage: String, textMessage: String, chat: String, userSender: User): Task<DocumentReference> {
        val message = Message(textMessage, userSender, urlImage)

        return ChatHelper.chatCollection
                .document(chat)
                .collection(COLLECTION_NAME)
                .add(message)
    }

    fun setMessageId(message: Message, id: String, chat: String): Task<Void> {
        return ChatHelper.chatCollection
                .document(chat)
                .collection(COLLECTION_NAME)
                .document(id)
                .update("id", id)
    }
}
