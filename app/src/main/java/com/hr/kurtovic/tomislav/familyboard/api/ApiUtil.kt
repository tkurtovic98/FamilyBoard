package com.hr.kurtovic.tomislav.familyboard.api

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class ApiUtil {

    companion object {

        const val FAMILIES_COLLECTION = "Families"
        const val MEMBERS_COLLECTION = "Members"
        const val MESSAGES_COLLECTION = "Messages"
        private const val VERSIONS = "Versions"
        private const val DB_VERSION = "v1"

        fun rootCollection(name: String): CollectionReference = FirebaseFirestore.getInstance()
                .collection(VERSIONS)
                .document(
                    DB_VERSION
                ).collection(
                    name
                )
    }


}
