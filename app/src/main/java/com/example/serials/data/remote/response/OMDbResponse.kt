package com.example.serials.data.remote.response

import com.example.serials.data.remote.dto.SerialOMDb

data class OMDbResponse(
    val Response: String,
    val Search: List<SerialOMDb>,
    val totalResults: String
)