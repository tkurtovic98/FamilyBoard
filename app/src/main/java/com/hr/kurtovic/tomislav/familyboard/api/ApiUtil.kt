package com.hr.kurtovic.tomislav.familyboard.api

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class ApiUtil {

    companion object {

        const val FAMILIES_COLLECTION = "Families"
        const val MEMBERS_COLLECTION = "Members"
        const val MESSAGES_COLLECTION = "Messages"

        fun collection(name: String): CollectionReference = FirebaseFirestore.getInstance().collection(
            name
        )
    }


}
