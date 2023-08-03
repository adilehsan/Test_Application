package com.example.testapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testapplication.db.dao.WeatherDataDao
import com.example.testapplication.db.entity.WeatherData


@Database(
    entities = [
        WeatherData::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getAllWeatherDataDao(): WeatherDataDao
}