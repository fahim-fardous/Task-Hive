package com.example.taskhive.presentation.settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.data.DatabaseBackup
import com.example.taskhive.data.local.AppDatabase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
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
