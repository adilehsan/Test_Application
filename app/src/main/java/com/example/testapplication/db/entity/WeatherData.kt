package com.example.testapplication.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "weatherData")
data class WeatherData(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "temperature")
    var temperature: String? = null,
    @ColumnInfo(name = "date")
    var date: String? = null
)
