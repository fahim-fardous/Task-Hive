package com.example.taskhive.presentation.settings

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhive.MainActivity
import com.example.taskhive.R
import com.example.taskhive.components.AlertDialog
import com.example.taskhive.data.remote.User
import com.example.taskhive.ui.theme.TaskHiveTheme
import com.example.taskhive.ui.theme.appColor
import com.example.taskhive.utils.AuthResultContract
import com.example.taskhive.utils.getGoogleSignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    goBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val activity = context as MainActivity
    val user by viewModel.user.collectAsState()
    SettingsScreenSkeleton(
        goBack = goBack,
        backup = {
            viewModel.backupDatabase()
        },
        restore = {
            viewModel.restoreDatabase()
        },
        activity = activity,
        user = user,
        setValue = { email, name ->
            viewModel.setSignInValue(email, name)
        }
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
    user: User? = null,
    setValue: (String, String) -> Unit = { _, _ -> },
) {
    var backupClicked by remember { mutableStateOf(false) }
    var restoreClicked by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf<String?>(null) }
    val signInRequestCode = 1

    val authResultLauncher = rememberLauncherForActivityResult(
        contract = AuthResultContract(
            googleSignInClient = getGoogleSignInClient(context)
        )
    ) { task ->
        try {
            val account = task?.getResult(ApiException::class.java)
            if (account == null) {
                text = "Google sign in failed"
            } else {
                scope.launch {
                    setValue(account.email!!, account.displayName!!)

                }
            }
        }
        catch (e:ApiException){
            text = e.localizedMessage
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

    if (restoreClicked) {
        AlertDialog(
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
