package com.example.testapplication.network

import com.example.testapplication.model.WeatherResponseModel
import com.example.testapplication.utility.Constants
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET(Constants.CURRENT_WEATHER_URL)
    suspend fun getWeathersDetail(@Query("lat") latitude: Double,@Query("lon") longitude: Double,@Query("appid") appID: String) : Response<WeatherResponseModel>
}