package com.example.smartcontrol

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class ControlViewModel : ViewModel() {
    private val _control = MutableLiveData<ControlPost>()

    private val _controlMode = MutableLiveData<ControlMode>()
    val controlMode: LiveData<ControlMode> get() = _controlMode

    private val _pump = MutableLiveData<PumpMode>()

    private val _message = MutableLiveData<String>()


    fun setControl(uuid: String, controlPost: ControlPost) {
        RetrofitClient.apiService.setControl(uuid, controlPost).enqueue(object : Callback<ControlPost> {
            override fun onResponse(call: Call<ControlPost>, response: Response<ControlPost>) {
                if (response.isSuccessful) {
                    _control.value = response.body()
                } else {
                    _message.value = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<ControlPost>, t: Throwable) {
                _message.value = "Error: ${t.message}"
            }
        })
    }

    fun startPump(uuid: String, pumpMode: PumpMode) {
        RetrofitClient.apiService.startPump(uuid, pumpMode).enqueue(object : Callback<PumpMode> {
            override fun onResponse(call: Call<PumpMode>, response: Response<PumpMode>) {
                if (response.isSuccessful) {
                    _pump.value = response.body()
                } else {
                    _message.value = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<PumpMode>, t: Throwable) {
                _message.value = "Error: ${t.message}"
            }
        })
    }

    fun getControlMode(uuid: String, intervalMillis: Long){
        viewModelScope.launch {
            while (true) {
                try {
                    val result = RetrofitClient.apiService.getControlMode(uuid)
                    _controlMode.value = result
                    _message.value = "Updated data"
                } catch (e: Exception) {
                    _message.value = "Error: ${e.message}"
                }

                delay(intervalMillis)
            }
        }

    }
}