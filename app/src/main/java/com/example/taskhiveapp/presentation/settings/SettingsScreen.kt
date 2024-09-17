package com.example.taskhiveapp.presentation.settings

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.taskhiveapp.MainActivity
import com.example.taskhiveapp.R
import com.example.taskhiveapp.components.Dialog
import com.example.taskhiveapp.ui.theme.TaskHiveTheme
import com.example.taskhiveapp.utils.HelperFunctions.handleSignInResult
import com.example.taskhiveapp.utils.HelperFunctions.onGoogleSignInSuccess
import com.example.taskhiveapp.utils.HelperFunctions.startGoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn

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
        activity = activity,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenSkeleton(
    goBack: () -> Unit = {},
    backup: () -> Unit = {},
    restore: () -> Unit = {},
    context: Context = LocalContext.current,
    activity: MainActivity? = null,
) {
    var backupClicked by remember { mutableStateOf(false) }
    var restoreClicked by remember { mutableStateOf(false) }
    var showRestartDialog by remember { mutableStateOf(false) }
    var signinClicked by remember { mutableStateOf(false) }
    var backupType by remember { mutableStateOf(0) }
    val loginPrefs = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
    var isLoggedIn by remember { mutableStateOf(loginPrefs.getBoolean("isLoggedIn", false)) }
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
                    if (isLoggedIn) {
                        loginPrefs.getString(
                            "photo",
                            "",
                        )
                    } else {
                        R.drawable.my_img
                    },
                contentDescription = "user pic",
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(CircleShape),
            )
            Text(
                text =
                    if (isLoggedIn) {
                        loginPrefs.getString(
                            "name",
                            "",
                        )!!
                    } else {
                        "John Doe"
                    },
                modifier = Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
            )
            Text(
                text = if (isLoggedIn) loginPrefs.getString("email", "")!! else "johndoe@gmail.com",
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
        AlertDialog(
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "Where do you want to backup your data?",
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = backupType == 0,
                            onCheckedChange = {
                                backupType = 0
                            },
                        )
                        Text(text = "Google Drive", color = MaterialTheme.colorScheme.onBackground)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = backupType == 1,
                            onCheckedChange = {
                                backupType = 1
                            },
                        )
                        Text(text = "Local Storage", color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            },
            onDismissRequest = {
                backupClicked = false
            },
            confirmButton = {
                TextButton(onClick = {
                    backupClicked = false
                    if (backupType == 0) {
                        onGoogleSignInSuccess(activity!!)
                    } else {
                        showRestartDialog = true
                    }
                }) {
                    Text(text = "Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    backupClicked = false
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
                backup()
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
