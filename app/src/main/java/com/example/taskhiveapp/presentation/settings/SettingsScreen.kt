package com.example.taskhiveapp.presentation.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.taskhiveapp.MainActivity
import com.example.taskhiveapp.R
import com.example.taskhiveapp.components.Dialog
import com.example.taskhiveapp.components.SelectableDialog
import com.example.taskhiveapp.ui.theme.TaskHiveTheme
import com.example.taskhiveapp.utils.HelperFunctions.handleSignInResult
import com.example.taskhiveapp.utils.HelperFunctions.startGoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    goBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val activity = context as MainActivity
    SettingsScreenSkeleton(
        goBack = goBack,
        backup = {
            viewModel.backupDatabase()
        },
        restore = {
            viewModel.restoreDatabase()
        },
        backupToCloud = {
            viewModel.backupDatabaseForCloud(activity, context)
        },
        restoreFromCloud = {
            viewModel.restoreFromCloud(activity, context)
        },
        activity = activity,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenSkeleton(
    goBack: () -> Unit = {},
    backup: () -> Unit = {},
    restore: () -> Unit = {},
    backupToCloud: () -> Unit = {},
    restoreFromCloud: () -> Unit = {},
    context: Context = LocalContext.current,
    activity: MainActivity? = null,
) {
    val coroutineScope = rememberCoroutineScope()
    var backupClicked by remember { mutableStateOf(false) }
    var restoreClicked by remember { mutableStateOf(false) }
    var showRestartDialogForBackup by remember { mutableStateOf(false) }
    var showRestartDialogForRestore by remember { mutableStateOf(false) }
    var signinClicked by remember { mutableStateOf(false) }
    var backupType by remember { mutableStateOf(0) }
    var restoreType by remember { mutableStateOf(0) }
    val loginPrefs = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
    var isLoggedIn by remember { mutableStateOf(loginPrefs.getBoolean("isLoggedIn", false)) }
    var name by remember { mutableStateOf(loginPrefs.getString("name", "John Doe") ?: "John Doe") }
    var email by remember {
        mutableStateOf(
            loginPrefs.getString("email", "johndoe@gmail.com") ?: "johndoe@gmail.com",
        )
    }
    var photoUrl by remember { mutableStateOf(loginPrefs.getString("photo", null)) }

    DisposableEffect(Unit) {
        val listener =
            SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
                when (key) {
                    "isLoggedIn" -> isLoggedIn = prefs.getBoolean("isLoggedIn", false)
                    "name" -> name = prefs.getString("name", "John Doe") ?: "John Doe"
                    "email" ->
                        email =
                            prefs.getString("email", "johndoe@gmail.com") ?: "johndoe@gmail.com"

                    "photo" -> photoUrl = prefs.getString("photo", null)
                }
            }
        loginPrefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            loginPrefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
    val signInLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleSignInResult(result, activity!!)
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model =
                    photoUrl ?: R.drawable.placeholder,
                contentDescription = "user pic",
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(CircleShape),
            )
            Text(
                text =
                name,
                modifier = Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
            )
            Text(
                text = email,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )

            HorizontalDivider(modifier = Modifier.padding(top = 32.dp, bottom = 16.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            backupClicked = true
                        },
            ) {
                Text(
                    text = "Backup",
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Restore",
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                            .clickable {
                                restoreClicked = true
                            },
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            signinClicked = true
                        },
            ) {
                Text(
                    text = "Auto backup",
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            signinClicked = true
                        },
            ) {
                Text(
                    text = if (isLoggedIn) "Sign out" else "Sign in",
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                    color = if (isLoggedIn) Color.Companion.Red else MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        }
    }

    if (backupClicked) {
        SelectableDialog(
            title = "Where do you want to backup your data?",
            type = backupType,
            onTypeChange = {
                backupType = it
            },
            onDismiss = { backupClicked = false },
            onConfirm = {
                backupClicked = false
                if (backupType == 0) {
                    if (isLoggedIn) {
                        backupToCloud()
                    } else {
                        Toast.makeText(context, "Please sign in first", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    showRestartDialogForBackup = true
                }
            },
        )
    }

    if (restoreClicked) {
        SelectableDialog(
            title = "From where do you want to restore your data?",
            type = restoreType,
            onTypeChange = {
                restoreType = it
            },
            onDismiss = { restoreClicked = false },
            onConfirm = {
                restoreClicked = false
                if (restoreType == 0) {
                    if (isLoggedIn) {
                        coroutineScope.launch {
                            restoreFromCloud()
                        }
                    } else {
                        showRestartDialogForRestore = true
                    }
                }
            },
        )
    }

    if (showRestartDialogForBackup) {
        Dialog(
            showDialog = { backupClicked = it },
            title = "Please restart your app to apply changes",
            confirmText = "Restart",
            onClicked = {
                backup()

            },
        )
    }

    if (showRestartDialogForRestore) {
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

    if (signinClicked) {
        if (GoogleSignIn.getLastSignedInAccount(context) == null) {
            val googleClient = GoogleSignIn.getClient(activity!!, startGoogleSignIn())
            signInLauncher.launch(googleClient.signInIntent)
        } else {
            signinClicked = false
        }
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
