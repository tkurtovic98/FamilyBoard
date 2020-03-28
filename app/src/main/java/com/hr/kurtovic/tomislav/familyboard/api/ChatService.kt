package com.hr.kurtovic.tomislav.familyboard.api

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object ChatService {

    private const val COLLECTION_NAME = "chats"

    val chatCollection: CollectionReference
        get() = FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
}
