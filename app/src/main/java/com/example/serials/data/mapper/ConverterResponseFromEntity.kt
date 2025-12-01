package com.example.serials.data.mapper

import com.example.serials.data.db.entity.SerialEntity
import com.example.serials.data.remote.dto.SerialOMDb

class ConverterResponseFromEntity {

    fun convertSerialOMDBFromEntity(serial: SerialOMDb): SerialEntity {
        return SerialEntity(
            Poster = serial.Poster,
            Title = serial.Title,
            Type = serial.Type,
            Year = serial.Year,
            imdbID = serial.imdbID)
    }
}