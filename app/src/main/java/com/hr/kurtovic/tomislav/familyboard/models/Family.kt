package com.hr.kurtovic.tomislav.familyboard.models

import com.google.firebase.firestore.CollectionReference

data class Family(
    val name: String? = null,
    val members: List<FamilyMember>? = null,
    val messages: CollectionReference? = null
)

