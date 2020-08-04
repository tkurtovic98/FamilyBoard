package com.hr.kurtovic.tomislav.familyboard.models

import com.google.firebase.firestore.DocumentId

data class FamilyMember(
    @DocumentId
    var uid: String? = null,
    var name: String? = null,
    var role: String? = null,
    var urlPicture: String? = null,
    var registrationTokens: MutableList<String>? = null
)

