package com.hr.kurtovic.tomislav.familyboard.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


interface AuthService {

    fun login(email: String?, password: String?): Task<AuthResult>

    fun logout()

    fun changeProfile(email: String, role: String)

}


class AuthServiceImpl : AuthService {

    private var authInstance = FirebaseAuth.getInstance()


    override fun login(email: String?, password: String?) =
            authInstance.signInWithEmailAndPassword(email!!, password!!)

    override fun logout() {
        authInstance.signOut()
    }

    override fun changeProfile(email: String, role: String) {

    }

}