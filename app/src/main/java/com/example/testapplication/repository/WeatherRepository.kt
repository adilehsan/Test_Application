package com.example.testapplication.repository

import com.example.testapplication.db.entity.WeatherData
import com.example.testapplication.model.BaseApiResponse
import com.example.testapplication.model.WeatherResponseModel
import com.example.testapplication.network.DataSource
import com.example.testapplication.network.RetrofitService
import com.example.testapplication.utility.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class WeatherRepository @Inject constructor(
    private val remoteDataSource: DataSource
) : BaseApiResponse() {

    suspend fun getWeathersDetail(
        latitude: Double,
        longitude: Double,
        appID: String
    ): Flow<NetworkResult<WeatherResponseModel>> {
        return flow{
            emit(safeApiCall { remoteDataSource.getWeathersDetail(latitude, longitude, appID) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insertWeatherDataToDB(weatherData: WeatherData) {
        remoteDataSource.insertWeatherDataToDB(weatherData)
    }

    suspend fun getWeatherData(): WeatherData? {
        return remoteDataSource.getWeatherData()
    }

}