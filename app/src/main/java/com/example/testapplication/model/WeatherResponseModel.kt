package com.example.testapplication.model

import com.google.gson.annotations.SerializedName

data class WeatherResponseModel(
    @SerializedName("dt")
    val date: Long? = 0,
    @SerializedName("main")
    val mainTemperature: MainTemperature? = null,
)

data class MainTemperature(
    @SerializedName("temp")
    val temperature: Double? = 0.0,
)
