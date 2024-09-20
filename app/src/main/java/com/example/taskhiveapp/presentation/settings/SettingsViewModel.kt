package com.example.taskhiveapp.presentation.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhiveapp.MainActivity
import com.example.taskhiveapp.data.DatabaseBackup
import com.example.taskhiveapp.data.local.AppDatabase
import com.example.taskhiveapp.utils.GoogleDriveHelper.downloadFileFromDrive
import com.example.taskhiveapp.utils.GoogleDriveHelper.getLastBackupFile
import com.example.taskhiveapp.utils.GoogleDriveHelper.uploadFileToCloud
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
            val backupFile = backup
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

        fun backupDatabaseForCloud(activity: MainActivity, context: Context) =
            viewModelScope.launch {
                val backupFile =
                    backup
                        .database(AppDatabase.getInstance(context))
                        .enableLogDebug(true)
                        .backupLocation(DatabaseBackup.BACKUP_FILE_LOCATION_CLOUD)
                        .apply {
                            onCompleteListener { success, message, exitCode ->
                                Log.d(
                                    "debug_RoomBackup",
                                    "success: $success, message: $message, exitCode: $exitCode",
                                )
                            }
                        }.backup()

                if (backupFile != null) {
                    val accountName =
                        context
                            .getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                            .getString("accountName", "")
                    uploadFileToCloud(backupFile, accountName, activity, context)
                }
            }

    fun restoreFromCloud(activity: MainActivity, context: Context) = viewModelScope.launch {
        val lastBackupFile = getLastBackupFile(context)
        val fileName = lastBackupFile?.name

        println("Last backup file:${lastBackupFile?.name}")

            if (fileName != null) {
            val downloadedFile = downloadFileFromDrive(context, lastBackupFile.id, lastBackupFile)

            if (downloadedFile != null) {
                Log.d("RestoreFromCloud", "Downloaded file: ${downloadedFile.absolutePath}")

                // Proceed with the restore process
                backup
                    .database(AppDatabase.getInstance(context))
                    .enableLogDebug(true)
                    .backupLocation(DatabaseBackup.BACKUP_FILE_LOCATION_CLOUD)
                    .apply {
                        onCompleteListener { success, message, exitCode ->
                            Log.d(
                                "debug_RoomBackup",
                                "success: $success, message: $message, exitCode: $exitCode",
                            )
                        }
                    }.restore(downloadedFile)

                activity.let {
                    val intent = Intent(it, MainActivity::class.java)
                    it.startActivity(intent)
                }

            } else {
                Log.e("RestoreFromCloud", "Downloaded file is null")
            }
        } else {
            Log.e("RestoreFromCloud", "File name is null")
        }
    }

}
