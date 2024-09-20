package com.example.taskhiveapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import com.example.taskhiveapp.MainActivity
import com.example.taskhiveapp.utils.HelperFunctions.getGoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException

object GoogleDriveHelper {
    private fun checkOrCreateFolderInGoogleDrive(
        driveService: Drive,
        context: MainActivity,
    ): String {
        var folderId = getFolderIdFromSharedPreferences(context)

        if (folderId != null && !checkIfFolderExists(driveService)) {
            clearFolderIdFromSharedPreferences(context)
            folderId = null
        }

        if (folderId == null) {
            folderId = getFolderIdFromDrive(driveService)
            if (folderId == null) {
                folderId = createFolderInGoogleDrive(driveService)
                println("Folder created in Google Drive with ID: $folderId")
            }
            saveFolderIdToSharedPreferences(context, folderId)
        }
        return folderId
    }

    private fun clearFolderIdFromSharedPreferences(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences("GoogleDrivePrefs", MODE_PRIVATE)
        sharedPreferences.edit().remove("folderId").apply()
    }

    private fun createFolderInGoogleDrive(driveService: Drive): String {
        val metadata =
            File().apply {
                name = "Backup Folder"
                mimeType = "application/vnd.google-apps.folder"
            }

        return try {
            val folder =
                driveService
                    .files()
                    .create(metadata)
                    .setFields("id")
                    .execute()

            folder.id
        } catch (e: Exception) {
            Log.e("GoogleDrive", "Error creating folder: ${e.message}")
            throw e
        }
    }

    private fun getFolderIdFromDrive(driveService: Drive): String? =
        try {
            val result =
                driveService
                    .files()
                    .list()
                    .setQ("mimeType = 'application/vnd.google-apps.folder' and name = 'Backup Folder'")
                    .setSpaces("drive")
                    .setFields("files(id, name)")
                    .execute()

            val files = result.files
            if (files != null && files.isNotEmpty()) {
                files[0].id
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("GoogleDrive", "Error searching for folder: ${e.message}")
            null
        }

    private fun saveFolderIdToSharedPreferences(
        context: Context,
        folderId: String,
    ) {
        val sharedPreferences =
            context.getSharedPreferences("GoogleDrivePrefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("folderId", folderId).apply()
    }

    private fun getFolderIdFromSharedPreferences(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences("GoogleDrivePrefs", MODE_PRIVATE)
        return sharedPreferences.getString("folderId", null)
    }

    suspend fun uploadFileToCloud(
        file: java.io.File,
        accountName: String?,
        activity: MainActivity,
        context: Context,
    ) {
        val googleAccountCredential = getGoogleAccountCredential(accountName, activity)

        val driveService =
            Drive
                .Builder(
                    NetHttpTransport(),
                    GsonFactory(),
                    googleAccountCredential,
                ).setApplicationName("Task Hive")
                .build()
        withContext(Dispatchers.IO) {
            checkOrCreateFolderInGoogleDrive(driveService, activity)
        }
        val folderId = getFolderIdFromSharedPreferences(activity)
        val fileMetadata =
            File().apply {
                name = file.name
                parents = listOf(folderId)
            }

        val mediaContent = FileContent("application/x-sqlite3", file)
        try {
            withContext(Dispatchers.IO) {
                driveService
                    .files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute()
            }

        } catch (e: Exception) {
            Log.e("GoogleDrive", "Error uploading file: ${e.message}")
        }
    }

    suspend fun getLastBackupFile(context: Context): File? {
        val drive = getDriveService(context)
        return withContext(Dispatchers.IO) {
            val result =
                drive
                    .files()
                    .list()
                    .setSpaces("drive")
                    .setFields("*")
                    .execute()
            val files = result.files
            println("Last file: ${files.last().name}")
            files.lastOrNull()
        }
    }

    suspend fun getAllBackupFiles(
        context: Context,
        outputStream: FileOutputStream,
    ) {
        val drive = getDriveService(context)
        return withContext(Dispatchers.IO) {
            val result =
                drive
                    .files()
                    .list()
                    .setSpaces("drive")
                    .setFields("*")
                    .execute()

            val files = result.files
            if (files.isNullOrEmpty()) {
                throw RuntimeException("No backup file found in Google Drive")
            }

            val lastBackupFile = files.last()
            drive.files().get(lastBackupFile.id).executeMediaAndDownloadTo(outputStream)

            outputStream.flush()
            outputStream.close()

            Log.d("debug_RoomBackup", "Backup file downloaded successfully")
        }
    }

    private fun checkIfFolderExists(driveService: Drive): Boolean {
        val query =
            "mimeType = 'application/vnd.google-apps.folder' and name = 'Backup Folder' and trashed = false"

        val result: FileList =
            driveService
                .files()
                .list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute()

        return result.files.size > 0
    }

    fun getDriveService(context: Context): Drive =
        Drive
            .Builder(
                NetHttpTransport(),
                GsonFactory(),
                getGoogleAccountCredential(
                    context
                        .getSharedPreferences(
                            "LoginPrefs",
                            Context.MODE_PRIVATE,
                        ).getString("accountName", ""),
                    context,
                ),
            ).setApplicationName("Task Hive")
            .build()

    suspend fun downloadFileFromDrive(
        context: Context,
        fileId: String,
        destinationFile: File,
    ): java.io.File? {
        val drive = getDriveService(context)
        var downloadedFile: java.io.File? = null
        withContext(Dispatchers.IO) {
            try {
                val outputStream = ByteArrayOutputStream()
                val request = drive.files().get(fileId).executeMediaAndDownloadTo(outputStream)

                val filePath =
                    context.getExternalFilesDir(null)?.absolutePath + "/" + destinationFile.name
                val file = java.io.File(filePath)
                val fileOutputStream = FileOutputStream(filePath)
                fileOutputStream.write(outputStream.toByteArray())
                fileOutputStream.close()
                Log.d("debug_RoomBackup", "File downloaded to: $filePath")
                downloadedFile = file
            } catch (e: IOException) {
                e.printStackTrace()
                println("Failed to download file: ${e.message}")
            }
        }
        return downloadedFile
    }
}
