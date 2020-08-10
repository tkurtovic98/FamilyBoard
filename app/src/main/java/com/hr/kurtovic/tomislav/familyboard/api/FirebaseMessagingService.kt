package com.hr.kurtovic.tomislav.familyboard.api

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FirebaseMessagingServiceImpl : FirebaseMessagingService() {

    private val familyMemberService: FamilyMemberService by inject()

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        retrieveTokenAndAdd()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            Log.d("FCM", it.body!!)
        }
    }

    private fun retrieveTokenAndAdd() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            val newToken = it?.token

            Log.d("FCM", "Token is $newToken")

            if (familyMemberService.currentMemberId.isNotEmpty()) {
                addTokenToFirestore(newToken)
            }

        }
    }

    private fun addTokenToFirestore(newRegistrationToken: String?) {
        if (newRegistrationToken == null) throw NullPointerException("FCM token is null")

        GlobalScope.launch {
            familyMemberService.getFCMRegistrationTokens { tokens ->

                tokens.apply {
                    if (this.contains(newRegistrationToken))
                        return@getFCMRegistrationTokens
                    this.add(newRegistrationToken)
                    familyMemberService.setFCMRegistrationTokens(this)
                }

            }
        }

    }

}