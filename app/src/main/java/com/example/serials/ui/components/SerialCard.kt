package com.example.serials.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import coil.compose.AsyncImage
import com.example.serials.data.db.entity.SerialEntity
import com.example.serials.data.remote.dto.SerialOMDb
import com.example.serials.ui.viewmodel.SerialsViewModel

@Composable
fun SerialCard(serial: SerialEntity) {
    
    Card(modifier = Modifier.fillMaxWidth()
        .padding(15.dp),
        elevation = CardDefaults.cardElevation(8.dp))
    {
        Row (modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceEvenly)
        {
            AsyncImage(
                model = serial.Poster,
                contentDescription = serial.Title,
                modifier = Modifier.size(250.dp)
            )
            Column(modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center)
            {
                Text(text = "${serial.Title}")
                Text(text = "${serial.Year}")
            }
        }
    }
}