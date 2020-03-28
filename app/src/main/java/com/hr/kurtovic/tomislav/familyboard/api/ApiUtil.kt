package com.hr.kurtovic.tomislav.familyboard.api

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


object ApiUtil {

    fun collection(name: String): CollectionReference = FirebaseFirestore.getInstance().collection(
        name
    )

}
