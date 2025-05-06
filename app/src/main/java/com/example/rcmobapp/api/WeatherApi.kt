package com.example.rcmobapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude : String,
        @Query("longitude") longitude : String,
        @Query("current") current : String
    ): retrofit2.Response<WeatherModel>
}