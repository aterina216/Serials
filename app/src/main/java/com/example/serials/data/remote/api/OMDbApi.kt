package com.example.serials.data.remote.api

import com.example.serials.data.remote.response.OMDbResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDbApi {

    @GET("/")
    suspend fun get2025Series(
        @Query("apikey") apiKey: String = KEY.API_KEY,
        @Query("y") year: Int = 2025,
        @Query("type") type: String = "series",
        @Query("s") search: String = "new"
    ): OMDbResponse
}