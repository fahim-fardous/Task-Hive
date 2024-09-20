package com.example.taskhiveapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.activity.result.ActivityResult
import com.example.taskhiveapp.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale

object HelperFunctions {
    fun convert24HourTo12Hour(time24: String): String {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat("HH:mm")
        val outputFormat = SimpleDateFormat("hh:mm a")

        // Parse the input time string to a Date object
        val date: Date? = inputFormat.parse(time24)

        // Format the Date object to the desired output format
        return outputFormat.format(date)
    }

    fun isAfter12AM(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Create a calendar instance representing 12:00 AM of the same day
        val midnightCalendar = Calendar.getInstance()
        midnightCalendar.time = date
        midnightCalendar.set(Calendar.HOUR_OF_DAY, 0)
        midnightCalendar.set(Calendar.MINUTE, 0)
        midnightCalendar.set(Calendar.SECOND, 0)
        midnightCalendar.set(Calendar.MILLISECOND, 0)

        // Compare the given time with 12:00 AM
        return calendar.after(midnightCalendar)
    }

    fun getDateTimeFromMillis(
        millis: Long,
        pattern: String,
    ): String {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return simpleDateFormat.format(Date(millis))
    }

    fun toLocalDate(date: Date?): LocalDate =
        LocalDateTime
            .ofInstant(
                date?.toInstant() ?: Date().toInstant(),
                ZoneOffset.UTC,
            ).toLocalDate()

    fun getGoogleAccountCredential(
        accountName: String?,
        context: Context,
    ): GoogleAccountCredential? =
        if (accountName != null) {
            GoogleAccountCredential
                .usingOAuth2(
                    context,
                    listOf(Scopes.DRIVE_FILE),
                ).apply {
                    selectedAccountName = accountName
                }
        } else {
            null
        }

    fun startGoogleSignIn(): GoogleSignInOptions =
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1074005987071-3rdjb67fok88qqtnnvejghgpr5bs377f.apps.googleusercontent.com")
            .requestEmail()
            .requestScopes(Scope(Scopes.DRIVE_FILE))
            .build()

    fun handleSignInResult(
        result: ActivityResult,
        activity: MainActivity,
    ) {
        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                Log.d("GoogleSignIn", "Account: ${account.email}")
                firebaseAuthWithGoogle(account.idToken!!, activity)
            } else {
                Log.w("GoogleSignIn", "Account is null")
            }
        } catch (e: ApiException) {
            Log.w("GoogleSignIn", "Google sign-in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String,
        activity: MainActivity,
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth
            .getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val account = GoogleSignIn.getLastSignedInAccount(activity)
                    val sharedPreferences =
                        activity.getSharedPreferences("LoginPrefs", MODE_PRIVATE)

                    account?.let {
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        sharedPreferences.edit().putString("email", account.email).apply()
                        sharedPreferences.edit().putString("name", account.displayName).apply()
                        sharedPreferences.edit().putString("accountName", account.email).apply()
                        sharedPreferences
                            .edit()
                            .putString("photo", account.photoUrl.toString())
                            .apply()
                    }
                } else {
                    Log.w("GoogleSignIn", "signInWithCredential:failure", task.exception)
                }
            }
    }
}
