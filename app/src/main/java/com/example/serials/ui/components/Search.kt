package com.example.serials.ui.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.theme.PurpleGrey40

@Composable
fun Search(searchText: String,
           onTextChanged: (String) -> Unit, ) {

    var text by remember { mutableStateOf(searchText) }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onTextChanged(it)
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Поиск", tint = Purple80)
        },
        trailingIcon = {
            if (text.isNotBlank()) {
                IconButton(onClick = {
                    text =""
                    onTextChanged("")
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Очистить",
                        tint = PurpleGrey40
                    )
                }
            }
        },
        placeholder = {
            Text("Поиск сериалов...",
                color = Color.Gray.copy(alpha = 0.7f) )
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Purple80,
            focusedLeadingIconColor = Purple80,
            unfocusedLeadingIconColor = PurpleGrey40
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )
    )

}