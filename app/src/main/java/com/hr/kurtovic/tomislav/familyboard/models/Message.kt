package com.hr.kurtovic.tomislav.familyboard.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    @DocumentId
    val id: String? = null,
    val category: String? = null,
    @get:ServerTimestamp
    val dateCreated: Date? = null,
    val memberRef: DocumentReference? = null,
    val content: Map<String, String>? = null
)
