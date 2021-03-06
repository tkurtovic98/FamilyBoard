package com.hr.kurtovic.tomislav.familyboard.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import io.reactivex.disposables.CompositeDisposable

data class Response(
    val success: Boolean = false,
    val error: String? = null
)


sealed class Event {
    data class SignInWithGoogle(val acct: GoogleSignInAccount) : Event()
    data class LoginSubmit(val email: String, val password: String) : Event()
    object LogoutSubmit : Event()
}

class AuthViewModel(
    private val authService: AuthService,
    private val familyMemberService: FamilyMemberService,
    private val firebaseMessagingService: FirebaseMessagingService
) : ViewModel() {

    //TODO(Extract webclient to strings)
    private val webClient = "225090068697-1uaoqec2b1sedrf2fcron5sm30f799ug.apps.googleusercontent.com"

    val authInstance = FirebaseAuth.getInstance()

    val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClient)
            .requestEmail()
            .build()

    private val compositeDisposable = CompositeDisposable()

    private var internalResponseObserver = MutableLiveData<Response>().apply { value = Response() }

    val responseObserver: LiveData<Response> = internalResponseObserver

    fun onEvent(event: Event) {

        when (event) {
            is Event.LoginSubmit -> authService.login(
                event.email,
                event.password
            ).addOnCompleteListener {
                internalResponseObserver.postValue(Response(it.isSuccessful, it.exception?.message))
            }
            is Event.LogoutSubmit -> {
                authService.logout()
                internalResponseObserver.postValue(Response(true))
            }
            is Event.SignInWithGoogle -> {
                familyMemberService.createMember(
                    uid = familyMemberService.currentMemberId,
                    memberName = event.acct.displayName!!,
                    urlPicture = event.acct.photoUrl.toString()
                )
                firebaseMessagingService.onNewToken("DUMMY")
                internalResponseObserver.postValue(Response(true))
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}