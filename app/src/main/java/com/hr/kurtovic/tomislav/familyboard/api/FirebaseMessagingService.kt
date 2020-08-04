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

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            val newToken = it?.token

            if (familyMemberService.currentMemberId.isNotEmpty()) {
                addTokenToFirestore(newToken)
            }

        }
    }

    private fun addTokenToFirestore(newRegistrationToken: String?) {
        if (newRegistrationToken == null) throw NullPointerException("FCM token is null")

        GlobalScope.launch {
            familyMemberService.getFCMRegistrationTokens { tokens ->
                if (tokens.contains(newRegistrationToken)) {
                    return@getFCMRegistrationTokens
                }

                tokens.add(newRegistrationToken)
                familyMemberService.setFCMRegistrationTokens(tokens)
            }
        }

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            //TODO: Show notification
            Log.d("FCM", "FCM message received")
        }
    }


}