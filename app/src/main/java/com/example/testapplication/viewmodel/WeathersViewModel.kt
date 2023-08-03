package com.example.testapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testapplication.db.entity.WeatherData
import com.example.testapplication.model.WeatherResponseModel
import com.example.testapplication.repository.WeatherRepository
import com.example.testapplication.utility.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeathersViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    application: Application
) : AndroidViewModel(application) {
    val weatherResponse: MutableLiveData<NetworkResult<WeatherResponseModel>> = MutableLiveData()
    val response: LiveData<NetworkResult<WeatherResponseModel>> = weatherResponse

    fun fetchDogResponse(latitude: Double, longitude: Double, appID: String) =
        viewModelScope.launch {
            weatherRepository.getWeathersDetail(latitude, longitude, appID).collect { values ->
                weatherResponse.value = values
            }
        }

    fun insertDataToDb(weatherData: WeatherData) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.insertWeatherDataToDB(weatherData)
        }
    }

    suspend fun getWeatherData() : WeatherData? {
        return kotlinx.coroutines.withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            weatherRepository.getWeatherData()
        }
    }
}