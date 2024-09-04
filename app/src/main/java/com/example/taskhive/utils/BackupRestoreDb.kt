package com.example.taskhive.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.utils.HelperFunctions.getDateTimeFromMillis
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object BackupRestoreDatabase {
    private const val DATABASE_NAME = "TaskHive.db"
    private const val BACKUP_DIR = "Backup"
    private const val DATE_PATTERN = "dd-MM-yyyy-hh:mm"

    fun createDatabaseBackup(context: Context) {
        val db = AppDatabase(context)
        db.close()

        val dbFile: File = context.getDatabasePath(DATABASE_NAME)
        val backupDir = File(context.getExternalFilesDir(null), BACKUP_DIR)
        val fileName =
            "Backup_${getDateTimeFromMillis(System.currentTimeMillis(), DATE_PATTERN)}.db"
        val backupFile = File(backupDir, fileName)

        if (!backupDir.exists()) {
            if (!backupDir.mkdirs()) {
                println("Failed to create backup directory: ${backupDir.absolutePath}")
                return
            }
        }

        try {
            if (backupFile.createNewFile()) {
                FileInputStream(dbFile).use { inputChannel ->
                    FileOutputStream(backupFile).use { outputChannel ->
                        copyStream(inputChannel, outputChannel)
                    }
                }
                Log.d("LOGGER", "Backup created successfully at ${backupFile.absolutePath}")
            } else {
                Log.d("LOGGER", "Failed to create backup file: ${backupFile.absolutePath}")
            }
        } catch (e: Exception) {
            Log.d("LOGGER", "Error during backup: ${e.message}")
        }
    }

    fun restoreDatabase(context: Context) {
        // Close the Room database instance
        val db = AppDatabase(context)
        db.close()

        // Get the database file path
        val dbFile: File = context.getDatabasePath("TaskHive.db")

        // Define the backup directory within the app's external files directory
        val backupDir = File(context.getExternalFilesDir(null), BACKUP_DIR)

        // Check if the backup directory exists
        if (!backupDir.exists()) {
            println("Backup directory not found: ${backupDir.absolutePath}")
            return
        }

        // List all backup files in the directory
        val backupFiles = backupDir.listFiles { _, name -> name.startsWith("Backup_") } ?: arrayOf()

        if (backupFiles.isEmpty()) {
            println("No backup files found in directory: ${backupDir.absolutePath}")
            return
        }

        // Sort files by last modified date in descending order and pick the most recent one
        val mostRecentBackup = backupFiles.maxByOrNull { it.lastModified() } ?: return

        println("Restoring from backup: ${mostRecentBackup.absolutePath}")

        try {
            // Delete the current database file
            if (dbFile.exists()) {
                dbFile.delete()
            }

            // Use a buffer to copy the backup file to the database location
            val bufferSize = 8 * 1024
            val buffer = ByteArray(bufferSize)
            var bytesRead: Int
            val inputStream: InputStream = FileInputStream(mostRecentBackup)
            val outputStream: OutputStream = FileOutputStream(dbFile)
            do {
                bytesRead = inputStream.read(buffer, 0, bufferSize)
                if (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead)
                }
            } while (bytesRead > 0)

            outputStream.flush()
            inputStream.close()
            outputStream.close()
            AppDatabase(context)

            println("Database restored successfully from ${mostRecentBackup.absolutePath}")
        } catch (e: Exception) {
            println("Error during restoration: ${e.message}")
        }
    }

    private fun generateBackupFilePath(): String {
        val backupDir = File(Environment.getExternalStorageDirectory(), BACKUP_DIR)
        val fileName = "Backup (${getDateTimeFromMillis(System.currentTimeMillis(), DATE_PATTERN)}"
        return File(backupDir, fileName).path
    }

    @Throws(IOException::class)
    private fun copyStream(
        inputStream: InputStream,
        outputStream: OutputStream,
    ) {
        val buffer = ByteArray(8 * 1024)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        outputStream.flush()
    }
}
