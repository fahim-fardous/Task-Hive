package com.example.taskhive.data

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.room.RoomDatabase
import com.example.taskhive.core.OnCompleteListener
import kotlinx.coroutines.runBlocking
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DatabaseBackup(
    private val context: Context,
) {
    companion object {
        private var TAG = "debug_RoomBackup"
        private lateinit var INTERNAL_BACKUP_PATH: File
        private lateinit var TEMP_BACKUP_PATH: File
        private lateinit var TEMP_BACKUP_FILE: File
        private lateinit var EXTERNAL_BACKUP_PATH: File
        private lateinit var DATABASE_FILE: File

        private var currentProcess: Int? = null
        private const val PROCESS_BACKUP = 1
        private const val PROCESS_RESTORE = 2
        private var backupFilename: String? = null

        /** Code for internal backup location, used for [backupLocation] */
        const val BACKUP_FILE_LOCATION_INTERNAL = 1

        /** Code for external backup location, used for [backupLocation] */
        const val BACKUP_FILE_LOCATION_EXTERNAL = 2

        /** Code for custom backup location dialog, used for [backupLocation] */
        const val BACKUP_FILE_LOCATION_CUSTOM_DIALOG = 3

        /** Code for custom backup file location, used for [backupLocation] */
        const val BACKUP_FILE_LOCATION_CUSTOM_FILE = 4
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbName: String

    private var roomDatabase: RoomDatabase? = null
    private var enableLogDebug: Boolean = false
    private var restartIntent: Intent? = null
    private var onCompleteListener: OnCompleteListener? = null
    private var customRestoreDialogTitle: String = "Choose file to restore"
    private var customBackupFileName: String? = null
    private var backupIsEncrypted: Boolean = false
    private var maxFileCount: Int? = null
    private var backupLocation: Int = BACKUP_FILE_LOCATION_INTERNAL
    private var backupLocationCustomFile: File? = null

    fun database(roomDatabase: RoomDatabase): DatabaseBackup {
        this.roomDatabase = roomDatabase
        return this
    }

    fun enableLogDebug(enableLogDebug: Boolean): DatabaseBackup {
        this.enableLogDebug = enableLogDebug
        return this
    }

    fun onCompleteListener(onCompleteListener: OnCompleteListener): DatabaseBackup {
        this.onCompleteListener = onCompleteListener
        return this
    }

    fun onCompleteListener(
        listener: (success: Boolean, message: String, exitCode: Int) -> Unit
    ): DatabaseBackup {
        this.onCompleteListener =
            object : OnCompleteListener {
                override fun onComplete(success: Boolean, message: String, exitCode: Int) {
                    listener(success, message, exitCode)
                }
            }
        return this
    }

    fun backupLocation(backupLocation: Int): DatabaseBackup {
        this.backupLocation = backupLocation
        return this
    }

    fun backupLocationCustomFile(backupLocationCustomFile: File): DatabaseBackup {
        this.backupLocationCustomFile = backupLocationCustomFile
        return this
    }

    fun maxFileCount(maxFileCount: Int): DatabaseBackup {
        this.maxFileCount = maxFileCount
        return this
    }

    private fun initRoomBackup(): Boolean {
        if (roomDatabase == null) {
            if (enableLogDebug) Log.d(TAG, "roomDatabase is missing")
            onCompleteListener?.onComplete(
                false,
                "roomDatabase is missing",
                OnCompleteListener.EXIT_CODE_ERROR_ROOM_DATABASE_MISSING
            )
            return false
        }

        if (backupLocation !in
            listOf(
                BACKUP_FILE_LOCATION_INTERNAL,
                BACKUP_FILE_LOCATION_EXTERNAL,
                BACKUP_FILE_LOCATION_CUSTOM_DIALOG,
                BACKUP_FILE_LOCATION_CUSTOM_FILE,
            )
        ) {
            if (enableLogDebug) Log.d(TAG, "backupLocation is missing")
            onCompleteListener?.onComplete(
                false,
                "backupLocation is missing",
                OnCompleteListener.EXIT_CODE_ERROR_BACKUP_LOCATION_MISSING
            )
            return false
        }

        if (backupLocation == BACKUP_FILE_LOCATION_CUSTOM_FILE && backupLocationCustomFile == null
        ) {
            if (enableLogDebug) {
                Log.d(
                    TAG,
                    "backupLocation is set to custom backup file, but no file is defined",
                )
                onCompleteListener?.onComplete(
                    false,
                    "backupLocation is set to custom backup file, but no file is defined",
                    OnCompleteListener.EXIT_CODE_ERROR_BACKUP_LOCATION_FILE_MISSING
                )
            }

            return false
        }

        dbName = roomDatabase!!.openHelper.databaseName!!
        INTERNAL_BACKUP_PATH = File("${context.filesDir}/databasebackup/")
        TEMP_BACKUP_PATH = File("${context.filesDir}/databasebackup-temp/")
        TEMP_BACKUP_FILE = File("$TEMP_BACKUP_PATH/tempbackup.sqlite3")
        EXTERNAL_BACKUP_PATH = File(context.getExternalFilesDir("backup")!!.toURI())
        DATABASE_FILE = File(context.getDatabasePath(dbName).toURI())

        try {
            INTERNAL_BACKUP_PATH.mkdirs()
            TEMP_BACKUP_PATH.mkdirs()
        } catch (_: FileAlreadyExistsException) {
        } catch (_: IOException) {
        }

        if (enableLogDebug) {
            Log.d(TAG, "DatabaseName: $dbName")
            Log.d(TAG, "Database Location: $DATABASE_FILE")
            Log.d(TAG, "INTERNAL_BACKUP_PATH: $INTERNAL_BACKUP_PATH")
            Log.d(TAG, "EXTERNAL_BACKUP_PATH: $EXTERNAL_BACKUP_PATH")
            if (backupLocationCustomFile != null) {
                Log.d(TAG, "backupLocationCustomFile: $backupLocationCustomFile")
            }
        }
        return true
    }

    fun backup() {
        if (enableLogDebug) Log.d(TAG, "Starting Backup ...")
        val success = initRoomBackup()
        if (!success) return

        currentProcess = PROCESS_BACKUP

        var filename =
            if (customBackupFileName == null) {
                "$dbName-${
                    getDateTimeFromMillis(
                        Calendar.getInstance().timeInMillis,
                        pattern = "yyyy-MM-dd_HH:mm",
                    )
                }.sqlite3"
            } else {
                customBackupFileName as String
            }

        if (backupIsEncrypted) filename += ".aes"
        if (enableLogDebug) Log.d(TAG, "backupFilename: $filename")

        when (backupLocation) {
            BACKUP_FILE_LOCATION_INTERNAL -> {
                val backupFile = File("$INTERNAL_BACKUP_PATH/$filename")
                doBackup(backupFile)
            }

            BACKUP_FILE_LOCATION_EXTERNAL -> {
                val backupFile = File("$EXTERNAL_BACKUP_PATH/$filename")
                doBackup(backupFile)
            }

            BACKUP_FILE_LOCATION_CUSTOM_DIALOG -> {
                backupFilename = filename
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        READ_MEDIA_IMAGES,
                        READ_MEDIA_AUDIO,
                        READ_MEDIA_VIDEO
                    )
                }
                else{
                    arrayOf(READ_EXTERNAL_STORAGE)
                }
                return
            }

            BACKUP_FILE_LOCATION_CUSTOM_FILE -> {
                doBackup(backupLocationCustomFile!!)
            }

            else -> return
        }
    }

    private fun doBackup(destination: File) {
        // Close the database
        roomDatabase!!.close()
        roomDatabase = null
        if (backupIsEncrypted) {
            val bos = BufferedOutputStream(FileOutputStream(destination, false))
            bos.flush()
            bos.close()
        } else {
            // Copy current database to save location (/files dir)
            copy(DATABASE_FILE, destination)
        }

        // If maxFileCount is set and is reached, delete oldest file
        if (maxFileCount != null) {
            val deleted = deleteOldBackup()
            if (!deleted) return
        }

        if (enableLogDebug) {
            Log.d(TAG, "Backup done, encrypted($backupIsEncrypted) and saved to $destination")
        }
    }

    fun getBackupFiles(): List<String> {
        val backupDirectory:File = INTERNAL_BACKUP_PATH
        val listOfFiles = backupDirectory.listFiles()

        val backupFiles = mutableListOf<String>()
        runBlocking {
            if (listOfFiles != null) {
                for (i in listOfFiles.indices){
                    backupFiles.add(listOfFiles[i].name)
                }
            }
        }
        return backupFiles


    }

    fun restore(){
        if (enableLogDebug) Log.d(TAG, "Starting Restore ...")
        val success = initRoomBackup()
        if (!success) return

        // Needed for storage permissions request
        currentProcess = PROCESS_RESTORE

        // Path of Backup Directory
        val backupDirectory: File

        when (backupLocation) {
            BACKUP_FILE_LOCATION_INTERNAL -> {
                backupDirectory = INTERNAL_BACKUP_PATH
            }

            BACKUP_FILE_LOCATION_EXTERNAL -> {
                backupDirectory = File("$EXTERNAL_BACKUP_PATH/")
            }

            BACKUP_FILE_LOCATION_CUSTOM_FILE -> {
                Log.d(
                    TAG,
                    "backupLocationCustomFile!!.exists()? : ${backupLocationCustomFile!!.exists()}",
                )
                doRestore(backupLocationCustomFile!!)
                return
            }

            else -> return
        }

        val arrayOfFiles = backupDirectory.listFiles()
        if (enableLogDebug) Log.d(TAG, "arrayOfFiles: $arrayOfFiles")

        if (arrayOfFiles.isNullOrEmpty()) {
            if (enableLogDebug) Log.d(TAG, "No backups available to restore")
            onCompleteListener?.onComplete(
                false,
                "No backups available",
                OnCompleteListener.EXIT_CODE_ERROR_RESTORE_NO_BACKUPS_AVAILABLE
            )
            Toast.makeText(context, "No backups available to restore", Toast.LENGTH_SHORT).show()
            return
        }

        // New empty MutableList of String
        val mutableListOfFilesAsString = mutableListOf<String>()

        // Add each filename to mutablelistOfFilesAsString
        runBlocking {
            for (i in arrayOfFiles.indices) {
                mutableListOfFilesAsString.add(arrayOfFiles[i].name)
            }
        }

        // Convert MutableList to Array
        val filesStringArray = mutableListOfFilesAsString.toTypedArray().sortedArrayDescending()
        restoreSelectedInternalExternalFile(filesStringArray[0])
    }

    private fun doRestore(source: File) {
        // Close the database
        roomDatabase!!.close()
        roomDatabase = null
        val fileExtension = source.extension
        if (backupIsEncrypted) {
            copy(source, TEMP_BACKUP_FILE)
            val bos = BufferedOutputStream(FileOutputStream(DATABASE_FILE, false))
            bos.flush()
            bos.close()
        } else {
            if (fileExtension == "aes") {
                if (enableLogDebug)
                    Log.d(
                        TAG,
                        "Cannot restore database, it is encrypted. Maybe you forgot to add the property .fileIsEncrypted(true)"
                    )
                onCompleteListener?.onComplete(
                    false,
                    "cannot restore database, see Log for more details (if enabled)",
                    OnCompleteListener.EXIT_CODE_ERROR_RESTORE_BACKUP_IS_ENCRYPTED
                )
                return
            }
            // Copy back database and replace current database
            copy(source, DATABASE_FILE)
        }

        if (enableLogDebug) {
            Log.d(TAG, "Restore done, decrypted($backupIsEncrypted) and restored from $source")
            onCompleteListener?.onComplete(true, "success", OnCompleteListener.EXIT_CODE_SUCCESS)
        }
    }

    private fun restoreSelectedInternalExternalFile(filename: String) {
        if (enableLogDebug) Log.d(TAG, "Restore selected file...")

        when (backupLocation) {
            BACKUP_FILE_LOCATION_INTERNAL -> {
                doRestore(File("$INTERNAL_BACKUP_PATH/$filename"))
            }

            BACKUP_FILE_LOCATION_EXTERNAL -> {
                doRestore(File("$EXTERNAL_BACKUP_PATH/$filename"))
            }

            else -> return
        }
    }

    private fun deleteOldBackup(): Boolean {
        // Path of Backup Directory

        val backupDirectory: File =
            when (backupLocation) {
                BACKUP_FILE_LOCATION_INTERNAL -> {
                    INTERNAL_BACKUP_PATH
                }

                BACKUP_FILE_LOCATION_EXTERNAL -> {
                    File("$EXTERNAL_BACKUP_PATH/")
                }

                BACKUP_FILE_LOCATION_CUSTOM_DIALOG -> {
                    // In custom backup location no backups will be removed
                    return true
                }

                else -> return true
            }

        // All Files in an Array of type File
        val arrayOfFiles = backupDirectory.listFiles()

        // If array is null or empty nothing to do and return
        if (arrayOfFiles.isNullOrEmpty()) {
            if (enableLogDebug) Log.d(TAG, "")
            return false
        } else if (arrayOfFiles.size > maxFileCount!!) {
            // Get count of files to delete
            val fileCountToDelete = arrayOfFiles.size - maxFileCount!!

            for (i in 1..fileCountToDelete) {
                // Delete all old files (i-1 because array starts a 0)
                arrayOfFiles[i - 1].delete()

                if (enableLogDebug) {
                    Log.d(TAG, "maxFileCount reached: ${arrayOfFiles[i - 1]} deleted")
                }
            }
        }
        return true
    }

    private fun copy(
        sourceFile: File,
        destFile: File,
    ) {
        try {
            FileInputStream(sourceFile).use { input ->
                FileOutputStream(destFile).use { output ->
                    input.channel.use { sourceChannel ->
                        output.channel.use { destChannel ->
                            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size())
                        }
                    }
                }
            }
            Log.d(TAG, "File copied from ${sourceFile.path} to ${destFile.path}")
        } catch (e: IOException) {
            Log.e(TAG, "File copy failed: ${e.message}", e)
            throw RuntimeException("Failed to copy file: ${e.message}")
        }
    }



    private fun getDateTimeFromMillis(
        millis: Long,
        pattern: String,
    ): String {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return simpleDateFormat.format(Date(millis))
    }
}
