package com.example.taskhive.presentation.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.taskhive.utils.BackupRestoreDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor():ViewModel() {

    fun backupDatabase(context: Context){
        BackupRestoreDatabase.createDatabaseBackup(context)
    }

    fun restoreDatabase(context: Context){
        BackupRestoreDatabase.restoreDatabase(context)
    }
}