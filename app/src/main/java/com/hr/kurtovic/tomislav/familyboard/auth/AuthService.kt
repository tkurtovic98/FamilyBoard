package com.hr.kurtovic.tomislav.familyboard.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


interface AuthService {
    fun login(email: String?, password: String?): Task<AuthResult>
    fun logout()
}


class AuthServiceImpl : AuthService {

    private val authInstance = FirebaseAuth.getInstance()

    override fun login(email: String?, password: String?) =
            authInstance.signInWithEmailAndPassword(email!!, password!!)

    override fun logout() {
        authInstance.signOut()
    }

}