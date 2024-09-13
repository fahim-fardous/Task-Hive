package com.example.taskhive.presentation.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhive.MainActivity
import com.example.taskhive.R
import com.example.taskhive.components.Dialog
import com.example.taskhive.ui.theme.TaskHiveTheme
import com.example.taskhive.ui.theme.appColor
import com.example.taskhive.utils.getGoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.InputStreamContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    goBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val activity = context as MainActivity
    SettingsScreenSkeleton(
        goBack = goBack,
        backup = { backupFileName ->
            viewModel.backupDatabase(fileName = backupFileName)
        },
        restore = {
            viewModel.restoreDatabase()
        },
        activity = activity,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenSkeleton(
    goBack: () -> Unit = {},
    backup: (String) -> Unit = {},
    restore: () -> Unit = {},
    context: Context = LocalContext.current,
    activity: MainActivity? = null,
) {
    var backupClicked by remember { mutableStateOf(false) }
    var restoreClicked by remember { mutableStateOf(false) }
    var showRestartDialog by remember { mutableStateOf(false) }
    var backupFileName by remember { mutableStateOf("") }
    var showWarning by remember { mutableStateOf("") }
    val signInLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (activity != null) {
                handleSignInResult(result, activity)
            }
        }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Settings", color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "go back",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                actions = {
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
            Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
        ) {
            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(color = Color(0xFFE7E1FA), shape = RoundedCornerShape(32.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.my_img),
                        contentDescription = "profile img",
                        Modifier
                            .size(width = 50.dp, height = 48.dp)
                            .clip(
                                CircleShape,
                            ),
                        contentScale = ContentScale.Crop,
                    )
                }
                Text(
                    text = "Fahim Mohammod Fardous",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .clickable {
                        backupClicked = true
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                    Modifier
                        .size(40.dp)
                        .background(color = appColor, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Backup,
                        contentDescription = "back up",
                        tint = Color.White,
                    )
                }
                Text(
                    text = "Backup",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp),
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = "click",
                )
            }
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .clickable {
                        restoreClicked = true
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                    Modifier
                        .size(40.dp)
                        .background(color = appColor, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Restore,
                        contentDescription = "restore",
                        tint = Color.White,
                    )
                }
                Text(
                    text = "Restore",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp),
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = "click",
                )
            }
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .clickable {
                        val existingAccount = GoogleSignIn.getLastSignedInAccount(context)
                        if (existingAccount != null) {
                            Toast
                                .makeText(context, "Already signed in", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            val googleSignInClient =
                                GoogleSignIn.getClient(context, startGoogleSignIn())
                            signInLauncher.launch(googleSignInClient.signInIntent)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                    Modifier
                        .size(40.dp)
                        .background(color = appColor, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Login,
                        contentDescription = "Google Sign in",
                        tint = Color.White,
                    )
                }
                Text(
                    text = "Google Sign in",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp),
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = "click",
                )
            }
        }
    }

    if (backupClicked) {
        AlertDialog(
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = backupFileName,
                        onValueChange = {
                            showWarning = if (it.last() == ' ') {
                                "White-space is not allowed"
                            } else {
                                ""
                            }
                            backupFileName = it
                        },
                        label = {
                            Text("Backup File name")
                        },
                        supportingText = {
                            Text(text = showWarning, color = Color.Red)
                        }
                    )
                }
            },
            onDismissRequest = {
                backupClicked = false
                backupFileName = ""
            },
            confirmButton = {
                TextButton(onClick = {
                    backupClicked = false
                    showRestartDialog = true
                }) {
                    Text(text = "Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    backupClicked = false
                    backupFileName = ""
                }) {
                    Text(text = "Cancel", color = Color.Red)
                }
            },
        )
    }

    if (restoreClicked) {
        Dialog(
            showDialog = { restoreClicked = it },
            title = "Please restart your app to apply changes",
            confirmText = "Restart",
            onClicked = {
                restore()
                activity?.let {
                    val intent = Intent(context, it::class.java)
                    it.finish()
                    context.startActivity(intent)
                }
            },
        )
    }

    if (showRestartDialog) {
        Dialog(
            showDialog = { backupClicked = it },
            title = "Please restart your app to apply changes",
            confirmText = "Restart",
            onClicked = {
                backup(backupFileName)
                activity?.let {
                    val intent = Intent(context, it::class.java)
                    it.finish()
                    context.startActivity(intent)
                }
            },
        )
    }
}

fun startGoogleSignIn(): GoogleSignInOptions {
    return GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("404142682364-6b9g9sa56ndaofufemkrm2u7a8in18v5.apps.googleusercontent.com")
        .requestEmail()
        .requestScopes(Scope(Scopes.DRIVE_FILE))
        .build()
}

fun handleSignInResult(
    result: ActivityResult,
    activity: MainActivity
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
                account?.let {
                    onGoogleSignInSuccess(it, activity)
                }
            } else {
                Log.w("GoogleSignIn", "signInWithCredential:failure", task.exception)
            }
        }
}

private fun onGoogleSignInSuccess(
    account: GoogleSignInAccount,
    activity: MainActivity
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val driveService = getDriveService(account, activity)
            runGoogleDriveOperations(driveService, activity)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("GoogleDrive", "Error setting up Google Drive: ${e.message}")
            }
        }
    }
}

private suspend fun runGoogleDriveOperations(
    driveService: Drive,
    activity: MainActivity
) {
    try {
        createFolderInGoogleDrive(driveService, activity)
        withContext(Dispatchers.Main) {
            Log.d("GoogleDrive", "Folder creation successful")
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Log.e("GoogleDrive", "Error creating folder: ${e.message}")
        }
    }
}

private fun createFolderInGoogleDrive(
    driveService: Drive,
    context: Context,
) {
    val metadata = File()
    metadata.name = "Backup Folder"
    metadata.mimeType = "application/vnd.google-apps.folder"

    try {
        val folder = driveService
            .files()
            .create(metadata)
            .setFields("id")
            .execute()

        val folderId = folder.id
        Log.d("GoogleDrive", "Folder ID: $folderId")

        // Save the folder ID to SharedPreferences
        saveFolderIdToSharedPreferences(context, folderId)

        uploadPhotoToGoogleDrive(
            driveService,
            folderId,
            "profile_pic.jpg",
            Uri.parse("android.resource://com.example.googledriveintegration/drawable/profile_pic"),
            context
        )
    } catch (e: Exception) {
        Log.e("GoogleDrive", "Error creating folder: ${e.message}")
    }
}

private fun saveFolderIdToSharedPreferences(
    context: Context,
    folderId: String
) {
    val sharedPreferences =
        context.getSharedPreferences("GoogleDrivePrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("folder_id", folderId).apply()
    Log.d("GoogleDrive", "Folder ID saved to SharedPreferences")
}

fun getFolderIdFromSharedPreferences(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("GoogleDrivePrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("folder_id", null)
}


private fun uploadPhotoToGoogleDrive(
    driveService: Drive,
    folderId: String,
    fileName: String,
    imageUri: Uri,
    context: Context,
) {
    val fileMetadata = File()
    fileMetadata.name = fileName
    fileMetadata.parents = listOf(folderId)
    fileMetadata.mimeType = "image/jpg"

    val inputStream = context.contentResolver.openInputStream(imageUri)
    val fileContent = InputStreamContent("image/jpg", inputStream)

    val file = driveService
        .files()
        .create(fileMetadata, fileContent)
        .setFields("id")
        .execute()

    Log.d("GoogleDrive", "Photo uploaded with ID: ${file.id}")
}

fun getDriveService(account: GoogleSignInAccount, context: Context): Drive {
    val googleAccountCredential = getGoogleAccountCredential(account, context)
    return Drive.Builder(
        NetHttpTransport(),
        GsonFactory(),
        googleAccountCredential
    ).setApplicationName("Google Drive Integration")
        .build()
}

// Function to get Google Account Credential
private fun getGoogleAccountCredential(
    account: GoogleSignInAccount,
    context: Context
): GoogleAccountCredential {
    return GoogleAccountCredential.usingOAuth2(
        context,
        listOf(Scopes.DRIVE_FILE)
    ).apply {
        selectedAccount = account.account
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenSkeletonPreview() {
    TaskHiveTheme {
        SettingsScreenSkeleton()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenSkeletonPreviewDark() {
    TaskHiveTheme {
        SettingsScreenSkeleton()
    }
}
