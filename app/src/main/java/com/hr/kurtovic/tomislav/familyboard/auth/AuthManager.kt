package com.hr.kurtovic.tomislav.familyboard.auth

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.hr.kurtovic.tomislav.familyboard.api.UserHelper

class AuthManager {

    companion object {
        val authInstance = FirebaseAuth.getInstance()
        private const val RC_SIGN_IN = 123
    }

    fun isUserLogged() = authInstance.currentUser != null

    fun listOfProviders(): List<AuthUI.IdpConfig> =
            listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
//                new AuthUI.IdpConfig.FacebookBuilder().build()
            )

    private fun createUserInFirestore() {
        val currentUser = authInstance.currentUser
        if (currentUser != null) {
            val urlPicture = if (currentUser.photoUrl != null) currentUser.photoUrl!!.toString() else null
            val username = currentUser.displayName
            val uid = currentUser.uid

            UserHelper.createUser(uid, username!!, urlPicture!!).addOnFailureListener { }
        }
    }

}