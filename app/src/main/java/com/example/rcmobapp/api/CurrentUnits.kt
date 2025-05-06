package com.example.rcmobapp.api

data class CurrentUnits(
    val interval: String,
    val precipitation: String,
    val rain: String,
    val snowfall: String,
    val temperature_2m: String,
    val time: String,
    val weather_code: String,
    val wind_speed_10m: String
)