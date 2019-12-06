package com.hr.kurtovic.tomislav.familyboard.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Message {

    var id: String? = null
    var message: String? = null
    @get:ServerTimestamp
    var dateCreated: Date? = null
    var userSender: User? = null
    var urlImage: String? = null
    var isAccepted = false

    constructor() {}

    constructor(message: String, userSender: User) {
        this.message = message
        this.userSender = userSender

    }

    constructor(message: String, userSender: User, urlImage: String) : this(message, userSender) {
        this.urlImage = urlImage
    }
}
