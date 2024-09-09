package com.example.taskhive.utils

import android.content.Context
import com.example.taskhive.utils.Constants.CLIENT_ID
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

fun getGoogleSignInClient(context: Context):GoogleSignInClient{
    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(CLIENT_ID)
        .build()

    return GoogleSignIn.getClient(context, signInOptions)
}