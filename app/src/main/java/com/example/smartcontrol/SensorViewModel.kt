package com.example.smartcontrol

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class SensorViewModel : ViewModel() {
    private val _sensors = MutableLiveData<Sensors>()
    val sensors: LiveData<Sensors> get() = _sensors

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchSensors(postId: Int) {
        viewModelScope.launch {
            try {
                val result = RetrofitClient.apiService.getSensors(postId)
                _sensors.value = result
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }
}