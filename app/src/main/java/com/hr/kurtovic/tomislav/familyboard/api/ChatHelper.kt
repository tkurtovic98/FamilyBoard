package com.hr.kurtovic.tomislav.familyboard.api

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object ChatHelper {

    private const val COLLECTION_NAME = "chats"

    val chatCollection: CollectionReference
        get() = FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
}
