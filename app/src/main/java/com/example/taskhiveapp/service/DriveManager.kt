package com.example.taskhiveapp.service

import android.content.Context
import com.example.taskhiveapp.utils.Constants.CLIENT_ID
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.qualifiers.ApplicationContext

class DriveManager constructor(
    @ApplicationContext private val context: Context,
) {
    private val oneTap = Identity.getSignInClient(context)
    private val signInRequest =
        BeginSignInRequest
            .builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions
                    .builder()
                    .setSupported(true)
                    .setServerClientId(CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build(),
            ).setAutoSelectEnabled(false)
            .build()

//    suspend fun signInGoogle():IntentSender{
//        return oneTap.beginSignIn(signInRequest).await().pendingIntent.intentSender
//    }
}
