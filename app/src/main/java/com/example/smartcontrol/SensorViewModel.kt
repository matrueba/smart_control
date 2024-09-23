package com.example.smartcontrol

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SensorViewModel : ViewModel() {
    private val _sensors = MutableLiveData<Sensors>()
    private val _globalStatus = MutableLiveData<GlobalStatus>()
    val sensors: LiveData<Sensors> get() = _sensors
    val globalStatus: LiveData<GlobalStatus> get() = _globalStatus

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun getSensorsStatus(uuid: String, intervalMillis: Long) {
        viewModelScope.launch {
            while (true) {
                try {
                    val result = RetrofitClient.apiService.getSensors(uuid)
                    _sensors.value = result
                    _message.value = "Updated data"
                } catch (e: Exception) {
                    _message.value = "Error: ${e.message}"
                }

                delay(intervalMillis)
            }
        }
    }

    fun getGlobalStatus(uuid: String, intervalMillis: Long) {
        viewModelScope.launch {
            while (true) {
                try {
                    val result = RetrofitClient.apiService.getGlobalStatus(uuid)
                    _globalStatus.value = result
                    _message.value = "Updated data"
                } catch (e: Exception) {
                    _message.value = "Error: ${e.message}"
                }

                delay(intervalMillis)
            }
        }
    }
}