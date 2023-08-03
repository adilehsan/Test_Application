package com.example.testapplication.network

import com.example.testapplication.db.AppDatabase
import com.example.testapplication.db.dao.WeatherDataDao
import com.example.testapplication.db.entity.WeatherData
import javax.inject.Inject

class DataSource @Inject constructor(private val retrofitService: RetrofitService,private val appDatabase: AppDatabase) {
    suspend fun getWeathersDetail(latitude: Double, longitude: Double, appID: String) =
        retrofitService.getWeathersDetail(latitude, longitude, appID)

    suspend fun insertWeatherDataToDB(weatherData: WeatherData){
        appDatabase.getAllWeatherDataDao().insertOrUpdate(weatherData)
    }

    suspend fun getWeatherData() : WeatherData?{
      return appDatabase.getAllWeatherDataDao().getWeathersData()
    }
}