package com.example.smartcontrol

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ControlViewModel : ViewModel() {
    private val _control = MutableLiveData<Control>()
    val control: LiveData<Control> get() = _control

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchControl() {
        viewModelScope.launch {
            try {
                val result = RetrofitClient.apiService.getPost(1)
                _control.value = result
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }
}