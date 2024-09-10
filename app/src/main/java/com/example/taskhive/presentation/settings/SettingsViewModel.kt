package com.example.taskhive.presentation.settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.taskhive.data.DatabaseBackup
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.utils.Constants.CLIENT_ID
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val oneTap = Identity.getSignInClient(context)
        private val signInRequest =
            BeginSignInRequest
                .builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions
                        .builder()
                        .setSupported(true)
                        .setServerClientId(
                            CLIENT_ID,
                        ).setFilterByAuthorizedAccounts(false)
                        .build(),
                ).setAutoSelectEnabled(true)
                .build()

        private val backup = DatabaseBackup(context)

        fun backupDatabase() {
            backup
                .database(AppDatabase.getInstance(context))
                .enableLogDebug(true)
                .backupLocation(DatabaseBackup.BACKUP_FILE_LOCATION_INTERNAL)
                .maxFileCount(1000)
                .apply {
                    onCompleteListener { success, message, exitCode ->
                        Log.d(
                            "debug_RoomBackup",
                            "success: $success, message: $message, exitCode: $exitCode",
                        )
                    }
                }.backup()
        }

        fun restoreDatabase() {
            backup
                .database(AppDatabase.getInstance(context))
                .enableLogDebug(true)
                .backupLocation(DatabaseBackup.BACKUP_FILE_LOCATION_INTERNAL)
                .apply {
                    onCompleteListener { success, message, exitCode ->
                        Log.d(
                            "debug_RoomBackup",
                            "success: $success, message: $message, exitCode: $exitCode",
                        )
                    }
                }.restore()
        }
    }
