package com.example.darkscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(private val prefs: DarkScreenPreferences) : ViewModel() {
    val brightness: Flow<Float> = prefs.brightness
    val color: Flow<Int> = prefs.color
    val mode: Flow<String> = prefs.mode

    fun setBrightness(value: Float) = viewModelScope.launch {
        prefs.setBrightness(value)
    }

    fun setColor(value: String) = viewModelScope.launch {
        prefs.saveColor(value)
    }

    fun setMode(value: String) = viewModelDcope.launch {
        prefs.saveMode(value)
    }
}
}