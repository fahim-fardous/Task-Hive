package com.example.taskhive.presentation.settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.data.DatabaseBackup
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.data.remote.User
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _user = MutableStateFlow<User?>(null)
        val user: StateFlow<User?> = _user.asStateFlow()

        private val backup = DatabaseBackup(context)

    fun setSignInValue(email:String, name:String) = viewModelScope.launch{
        delay(2000)
        _user.value = User(email, name)
    }

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
