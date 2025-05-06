package com.example.rcmobapp.api

data class Current(
    val interval: Int,
    val precipitation: Double,
    val rain: Double,
    val snowfall: Double,
    val temperature_2m: Double,
    val time: String,
    val weather_code: Int,
    val wind_speed_10m: Double,
)