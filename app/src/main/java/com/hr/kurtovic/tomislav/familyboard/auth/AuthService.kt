package com.hr.kurtovic.tomislav.familyboard.auth

import com.google.firebase.auth.FirebaseAuth


interface AuthService {

    fun login(email: String?, password: String?)

    fun logout()

    fun changeProfile(email: String, role: String)

}


class AuthServiceImpl : AuthService {

    private var authInstance = FirebaseAuth.getInstance()


    override fun login(email: String?, password: String?) {

        if (email != null && password != null) {
            authInstance.signInWithEmailAndPassword(email, password)
        }

    }

    override fun logout() {
        authInstance.signOut()
    }

    override fun changeProfile(email: String, role: String) {

    }

}