package com.example.serials.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serials.ui.WatchStatus
import com.example.serials.ui.theme.Purple40
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.theme.PurpleGrey40
import com.example.serials.ui.theme.PurpleGrey80
import com.example.serials.ui.theme.Violet
import com.example.serials.ui.theme.lightBlue

@Composable
fun StatusDropdownButton(
    currentStatus: String?,
    onStatusSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val buttonText = when (currentStatus) {
        WatchStatus.WANT_TO_WATCH -> "Хочу посмотреть"
        WatchStatus.WATCHING -> "Смотрю"
        WatchStatus.WATCHED -> "Просмотрено"
        else -> "Добавить в избранное"
    }

    Box(modifier = modifier
        .wrapContentSize(Alignment.TopStart))
    {
        Button(
            {expanded = true},
            colors = ButtonDefaults.buttonColors(
                containerColor = Violet.copy(alpha = 0.5f),
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, PurpleGrey80.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
            )
        ) {
            Text(buttonText, fontSize = 14.sp)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false},
            modifier = Modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Purple40.copy(alpha = 0.95f),
                        PurpleGrey40.copy(alpha = 0.9f)
                    )
                )
            )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(lightBlue, Purple80)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            DropdownMenuItem(
                text = {
                    Text("Не выбрано")
                },
                onClick = {
                    Log.d("DEBUG_UI", "Выбран статус: null")
                    onStatusSelected(null)
                    expanded = false
                }
            )

            Divider(
                color = Color.White.copy(alpha = 0.2f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            DropdownMenuItem(
                text = {
                    Text("Хочу посмотреть")
                },
                onClick = {
                    Log.d("DEBUG_UI", "Выбран статус: WANT_TO_WATCH")
                    onStatusSelected(WatchStatus.WANT_TO_WATCH)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = {
                    Text("Смотрю")
                },
                onClick = {
                    Log.d("DEBUG_UI", "Выбран статус: WATCHING")
                    onStatusSelected(WatchStatus.WATCHING)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = {
                    Text("Просмотрено")
                },
                onClick = {
                    Log.d("DEBUG_UI", "Выбран статус: WATCHED")
                    onStatusSelected(WatchStatus.WATCHED)
                    expanded = false
                }
            )
        }
    }
}