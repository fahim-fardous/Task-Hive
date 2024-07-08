package com.example.taskhive.presentation.task.add

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhive.R
import com.example.taskhive.components.CardWithRow
import com.example.taskhive.components.CommonCard
import com.example.taskhive.components.CustomButton
import com.example.taskhive.ui.theme.TaskHiveTheme
import com.example.taskhive.ui.theme.appColor

@Composable
fun TaskAddScreen() {
    TaskAddScreenSkeleton()
}

@Preview(showBackground = true)
@Composable
private fun TaskAddScreenSkeletonPreview() {
    TaskHiveTheme {
        TaskAddScreenSkeleton()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAddScreenSkeleton() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Add Project",
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                        )
                    }
                },
                actions = {
                    BadgedBox(modifier = Modifier.padding(end = 8.dp), badge = {
                        Badge(
                            containerColor = appColor,
                            modifier = Modifier.size(10.dp),
                        )
                    }) {
                        Icon(
                            Icons.Filled.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.Black,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                },
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            CustomButton(text = "Add Project", trailingIcon = null)
        },
    ) { innerPadding ->
        Column(
            modifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            CardWithRow(
                icon = Icons.Filled.Person,
                label = "Task Group",
                withCard = true,
                iconColor =
                    Color(
                        0xFFF378B7,
                    ),
                borderColor = Color(0xFFFEE3F1),
            )
            CommonCard(label = "Project Name", lines = 1)
            CommonCard(label = "Description", lines = 5)
            CardWithRow(
                icon = Icons.Filled.DateRange,
                label = "Start Date",
                withCard = false,
                iconColor = appColor,
                borderColor = null,
            )
            CardWithRow(
                icon = Icons.Filled.DateRange,
                label = "End Date",
                withCard = false,
                iconColor = appColor,
                borderColor = null,
            )
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors =
                    CardDefaults.elevatedCardColors(
                        containerColor = Color.White,
                    ),
            ) {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    val painter = painterResource(id = R.drawable.change_logo)
                    Image(
                        painter = painter,
                        contentDescription = "logo",
                        modifier =
                            Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .weight(1f, fill = false)
                                .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height),
                        contentScale = ContentScale.Fit,
                    )
                    Box(
                        modifier =
                            Modifier
                                .padding(16.dp)
                                .background(color = Color(0xFFECE7FE), shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp),
                    ) {
                        Text(
                            text = "Change Logo",
                            color = appColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        )
                    }
                }
            }
        }
    }
}
