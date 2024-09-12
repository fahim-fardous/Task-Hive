package com.example.taskhive.utils

import android.content.Context
import com.example.taskhive.utils.Constants.CLIENT_ID
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope

fun getGoogleSignInClient(context: Context): GoogleSignInClient{
    val gso =
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
            .requestEmail()
            .requestScopes(Scope(Scopes.DRIVE_FILE))
            .build()
    return GoogleSignIn.getClient(context, gso)
}