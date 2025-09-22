package com.example.darkscreen.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.darkscreen.DarkScreenPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.core.graphics.toColorInt

class SettingsViewModel(private val prefs: DarkScreenPreferences) : ViewModel() {
    val brightness: Flow<Float> = prefs.brightness
    val color: Flow<Int> = prefs.color
    val mode: Flow<String> = prefs.mode

    fun setBrightness(value: Float) = viewModelScope.launch {
        prefs.setBrightness(value)
    }

    fun setColor(value: String) = viewModelScope.launch {
        prefs.setColor(value)
    }

    fun setMode(value: String) = viewModelScope.launch {
        prefs.saveMode(value)
    }
}

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val brightness by viewModel.brightness.collectAsState(initial = 0.5f)
    val color by viewModel.color.collectAsState(initial = "#7DD3FC")
    val mode by viewModel.mode.collectAsState(initial = "focus")
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Brightness: ${"%.2f".format(brightness)}")
        Slider(value = brightness, onValueChange = { viewModel.setBrightness(it) })

        Spacer(modifier = Modifier.height(16.dp))

        Text("Accent Color")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("#7DD3FC", "#F472B6", "#34D399").forEach { hex ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(hex.toColorInt()))
                        .clickable { viewModel.setColor(hex) }
                )
            }
        }
    }
}
