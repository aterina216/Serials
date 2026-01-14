package com.example.serials.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.serials.data.db.entity.ReminderEntity
import com.example.serials.data.remote.dto.SerialDetails
import com.example.serials.ui.theme.DarkGray
import com.example.serials.ui.theme.DarkViolet
import com.example.serials.ui.theme.Gray
import com.example.serials.ui.theme.LightGray
import com.example.serials.ui.theme.LightViolet
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.viewmodel.SerialsViewModel
import com.example.serials.utils.FormatDate.formatReminderTime
import com.example.serials.utils.Reminder.cancelReminder
import com.example.serials.utils.Reminder.showReminder

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReminderCard(reminderEntity: ReminderEntity, viewModel: SerialsViewModel) {

    val context = LocalContext.current
    var updateDate by remember {
        mutableStateOf(0L)
    }

    var dateDialogVisible by remember { mutableStateOf(false) }
    var timeDialogVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        LightViolet.copy(alpha = 0.3f),
                        DarkViolet.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        LightGray.copy(alpha = 0.5f),
                        LightViolet.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .shadow(elevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray.copy(alpha = 0.3f))
                ) {
                    AsyncImage(
                        model = reminderEntity.Poster,
                        contentDescription = reminderEntity.Title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = reminderEntity.Title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${reminderEntity.Year} * ${reminderEntity.Type}",
                        fontSize = 14.sp,
                        color = Gray.copy(alpha = 0.8f)
                    )

                    if (reminderEntity.time != null) {
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = formatReminderTime(reminderEntity.time!!),
                            fontSize = 14.sp,
                            color = DarkViolet,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    LightViolet.copy(alpha = 0.6f),
                                    DarkViolet.copy(alpha = 0.3f)
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    LightGray, LightViolet
                                )
                            ),
                            shape = CircleShape
                        )
                        .clickable {
                            dateDialogVisible = true
                        }
                        .shadow(elevation = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Редактировать",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Purple80.copy(alpha = 0.7f),
                                    DarkViolet.copy(alpha = 0.5f)
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(LightGray, Purple80)
                            ),
                            shape = CircleShape
                        )
                        .clickable {
                            cancelReminder(context, reminderEntity.imdbID)
                            viewModel.deleteReminder(reminderEntity.imdbID)
                        }
                        .shadow(elevation = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (dateDialogVisible) {
            DateDialog(
                onDateSelected = { newDate ->
                    if (newDate != null) updateDate = newDate
                    dateDialogVisible = false
                    timeDialogVisible = true
                },
                onDismiss = { dateDialogVisible = false }
            )
        }

        if (timeDialogVisible) {
            TimeDialog(
                selectedDateMillis = updateDate,
                onTimeSelected = { newTime ->
                    if(newTime!=null) {

                        viewModel.updateReminderTime(reminderEntity.imdbID, newTime)
                        cancelReminder(context, reminderEntity.imdbID)
                        showReminder(context, reminderEntity, newTime!!)
                    }
                },
                onDismiss = { timeDialogVisible = false }
            )
        }
    }
}