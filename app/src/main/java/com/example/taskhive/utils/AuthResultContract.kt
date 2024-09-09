package com.example.taskhive.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task

class AuthResultContract(private val googleSignInClient: GoogleSignInClient) : ActivityResultContract<Int, Task<GoogleSignInAccount>?>() {
    override fun createIntent(context: Context, input: Int): Intent {
        return googleSignInClient.signInIntent.putExtra("input", input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount>? {
        return when(resultCode){
            Activity.RESULT_OK -> GoogleSignIn.getSignedInAccountFromIntent(intent)
            else -> null
        }
    }

}