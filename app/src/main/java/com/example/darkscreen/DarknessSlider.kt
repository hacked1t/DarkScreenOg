package com.example.darkscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun DarknessSlider(
    brightness: Float,
    onBrightnessChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Darkness", color = Color.Red, fontSize = 20.sp)
        Slider(
            value = brightness,
            onValueChange = onBrightnessChange,
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Red,
                inactiveTrackColor = Color.Red.copy(alpha = 0.5f)
            )
        )
        Text("${(brightness * 100).toInt()}%", color = Color.Red)
    }
}