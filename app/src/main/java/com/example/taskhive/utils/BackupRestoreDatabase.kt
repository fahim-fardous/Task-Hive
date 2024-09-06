package com.example.taskhive.utils

import android.content.Context
import android.util.Log
import androidx.room.Room
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

    // Function to backup the Room database
    fun createDatabaseBackup(context: Context) {
        // Close any open database instance
        val db = AppDatabase.getInstance(context)
        db.close()

        // Get the database file
        val dbFile: File = context.getDatabasePath(DATABASE_NAME)

        // Ensure the backup directory exists
        val backupDir = File(context.filesDir, BACKUP_DIR)
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }

        // Create a backup file with a timestamp
        val backupFile = File(backupDir, "Backup_${System.currentTimeMillis()}.db")
        try {
            val inputStream: InputStream = FileInputStream(dbFile)
            val outputStream: OutputStream = FileOutputStream(backupFile)
            copyStream(inputStream, outputStream)
            outputStream.flush()
            inputStream.close()
            outputStream.close()

            println("Database backup saved to: ${backupFile.absolutePath}")
        } catch (e: IOException) {
            println("Error during backup: ${e.message}")
        }
    }

    // Function to restore the Room database from the latest backup
    fun restoreDatabase(context: Context) {
        // Get the database file and backup directory
        val dbFile: File = context.getDatabasePath(DATABASE_NAME)
        val backupDir = File(context.filesDir, BACKUP_DIR)

        // Check if the backup directory exists
        if (!backupDir.exists()) {
            println("Backup directory not found: ${backupDir.absolutePath}")
            return
        }

        // Find backup files
        val backupFiles = backupDir.listFiles { _, name -> name.startsWith("Backup_") } ?: arrayOf()

        if (backupFiles.isEmpty()) {
            println("No backup files found in directory: ${backupDir.absolutePath}")
            return
        }

        // Get the most recent backup file
        val mostRecentBackup = backupFiles.maxByOrNull { it.lastModified() } ?: return

        println("Restoring from backup: ${mostRecentBackup.absolutePath}")

        try {
            // Close and delete the current database instance
            AppDatabase.getInstance(context).close()
            AppDatabase.clearInstance() // Clears singleton cache (if you have this method)

            if (dbFile.exists()) {
                dbFile.delete()
            }

            // Copy data from backup file to the new database file
            val inputStream: InputStream = FileInputStream(mostRecentBackup)
            val outputStream: OutputStream = FileOutputStream(dbFile)

            copyStream(inputStream, outputStream)

            outputStream.flush()
            inputStream.close()
            outputStream.close()

            // Reinitialize the database (avoid allowMainThreadQueries in production)
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration() // Handles version changes if schema updates
                .build()

            println("Database restored successfully from ${mostRecentBackup.absolutePath}")
        } catch (e: Exception) {
            println("Error during restoration: ${e.message}")
        }
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
