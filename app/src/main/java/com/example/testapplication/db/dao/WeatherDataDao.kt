package com.example.testapplication.db.dao

import androidx.room.*
import com.example.testapplication.db.entity.WeatherData

@Dao
interface WeatherDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(weatherData: WeatherData)

    @Query("SELECT * FROM weatherData")
    fun getWeathersData(): WeatherData?

    @Update
    suspend fun update(weatherData: WeatherData)

    @Transaction
    suspend fun insertOrUpdate(weatherData: WeatherData) {
        val addData = getWeathersData()
        addData?.let {
            update(weatherData)
        }?: kotlin.run {
            insertWeatherData(weatherData)
        }
    }
}