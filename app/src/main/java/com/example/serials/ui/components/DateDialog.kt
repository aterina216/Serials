package com.example.serials.ui.components

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    title: String = "Выберите дату напоминания"
) {
    val context = LocalContext.current
    val datePickerState = rememberDatePickerState()

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val date = calendar.timeInMillis

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedDateMillis = datePickerState.selectedDateMillis

                    if(selectedDateMillis!=null) {
                        val selectedDate = Instant
                            .ofEpochMilli(selectedDateMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()

                        if(selectedDateMillis >= date) {
                            onDateSelected(selectedDateMillis)
                            onDismiss
                        }
                        else {
                            Toast.makeText(
                                context,
                                "Нельзя установить напоминание на прошедшую дату!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else {
                        Toast.makeText(
                            context,
                            "Пожалуйста, выберите дату",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text("Установить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}