package com.example.darkscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Assume Preset data class is defined elsewhere, e.g.:
// data class Preset(val name: String, val brightness: Float, val color: Int, /*... other properties ...*/)

@Composable
fun PresetButton(preset: Preset, onClick: (Preset) -> Unit) { // onClick parameter is correctly defined
    androidx.compose.material3.Button(
        onClick = { onClick(preset) }, // Use the passed-in onClick lambda here
        colors = ButtonDefaults.buttonColors(containerColor = Color(preset.color)), // Use preset.color
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(preset.name) // Display the preset name or some other relevant text
    }
}
