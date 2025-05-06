package com.example.rcmobapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rcmobapp.api.NetworkResponse
import com.example.rcmobapp.api.RetrofitInstance
import com.example.rcmobapp.api.WeatherModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val uiState = MutableStateFlow(LocationState())
    val state: StateFlow<LocationState> = uiState

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun update(address: String, latitude: Double, longitude: Double) {
        uiState.update { it.copy(address = address, latitude = latitude, longitude = longitude) }
    }

    fun getWeatherData() {
        val currentState = uiState.value

        val latitude = currentState.latitude
        val longitude = currentState.longitude

        if(!latitude.isNaN() || !longitude.isNaN()) {
            fetchWeatherData(latitude, longitude)
        }

    }

    fun fetchWeatherData(latitude: Double, longitude: Double) {
        val lat = latitude.toString()
        val lon = longitude.toString()

        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(
                    latitude = lat,
                    longitude = lon,
                    current = "temperature_2m,precipitation,rain,snowfall,weather_code,wind_speed_10m"
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Не удалось загрузить данные")
                }
            } catch (e : Exception) {
                _weatherResult.value = NetworkResponse.Error("Не удалось загрузить данные")
            }
        }
    }
}