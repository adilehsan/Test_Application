package com.example.testapplication.utility

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class Constants {
    companion object {

        const val BASE_URL = "https://api.openweathermap.org/"
        const val CURRENT_WEATHER_URL = "data/2.5/weather"
        const val APP_ID = "fcf35f519c0a92895645f6531b3c667c"

        @SuppressLint("SimpleDateFormat")
        fun convertUnixToDate(unixDate: Long): String {
            val date = Date(unixDate * 1000L)
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(date)
        }
    }


}