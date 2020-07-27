package com.hr.kurtovic.tomislav.familyboard.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    @DocumentId
    var id: String? = null,
    var category: String? = null,
    @get:ServerTimestamp
    var dateCreated: Date? = null,
    var memberSenderRef: DocumentReference? = null,
    var content: Map<String, String>? = null,
    var accepted: Boolean = false,
    var memberWhoAcceptedId: String? = null
)