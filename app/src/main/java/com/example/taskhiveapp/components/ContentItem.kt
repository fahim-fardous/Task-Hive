package com.example.taskhiveapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhiveapp.ui.theme.appColor

@Composable
fun ContentItem(
    date: CalendarUiModel.Date,
    onDateClick: (CalendarUiModel.Date) -> Unit = {},
) {
    Card(
        modifier =
            Modifier.padding(4.dp).clickable {
                onDateClick(date)
            },
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (date.isSelected) {
                        appColor
                    } else {
                        Color.White
                    },
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .width(60.dp)
                    .height(80.dp)
                    .padding(2.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text =
                    date.date.month
                        .toString()
                        .substring(0, 3),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = if (date.isSelected) Color.White else Color.Black,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 8.sp,
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = if (date.isSelected) Color.White else Color.Black,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = if (date.isSelected) Color.White else Color.Black,
                fontSize = 8.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentItemPreview() {
//    ContentItem(date = CalendarDataSource().getData(lastSelectedDate = CalendarDataSource().today).visibleDates.first())

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow {
            items(7) {
                ContentItem(date = CalendarDataSource().getData(lastSelectedDate = CalendarDataSource().today).visibleDates.first())
            }
        }
    }
}
