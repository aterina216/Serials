package com.example.serials.ui.components


import android.app.TimePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeDialog(
    selectedDateMillis: Long,
    onTimeSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    TimePickerDialog(
        context,
        {
            _, hourOfDay, minute ->
            val selectedDate = Instant
                .ofEpochMilli(selectedDateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            val selectedTime = LocalTime.of(hourOfDay, minute)

            val dateTime = LocalDateTime.of(selectedDate, selectedTime)

            val finalMillis = dateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            if (finalMillis >= System.currentTimeMillis()) {
                Toast.makeText(
                    context,
                    "Напоминание установлено на: $dateTime",
                    Toast.LENGTH_SHORT
                ).show()
                onTimeSelected(finalMillis)
            } else {
                Toast.makeText(
                    context,
                    "Нельзя установить напоминание в прошлом!",
                    Toast.LENGTH_SHORT
                ).show()
                onTimeSelected(null)
            }
        },
        LocalTime.now().hour,
        LocalTime.now().minute,
        true
    ).apply {
        setTitle("Выберите время")
        setButton(
            TimePickerDialog.BUTTON_POSITIVE, "Выбрать", this
        )
        setOnDismissListener {
            onDismiss()
        }
        show()
    }
}

