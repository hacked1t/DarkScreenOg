package com.example.darkscreen

import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.darkscreen.ui.theme.DarkScreenTheme
import kotlin.math.roundToInt


@Composable
fun DarknessSlider(
    darknessLevel: Float,
    onDarknessChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            "Screen Darkness",
            color = Color.Red,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Darkness Level Display
        Text(
            text = "${(darknessLevel * 100).roundToInt()}%",
            fontSize = 48.sp,
            fontWeight = FontWeight.Light,
            color = Color.Red,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        // Icons Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bright icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\uD83D\uDD0D",
                    fontSize = 20.sp
                )
            }
            // Dark icon
            Box(
                modifier = Modifier.size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\uD83D\uDD00",
                    fontSize = 20.sp
                )
            }
        }
        // Slider
        Slider(
            value = darknessLevel,
            onValueChange = { onDarknessChange(it) },
            valueRange = 0f..1f,
            modifier = Modifier.padding(horizontal = 16.dp),
                    colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Red,
                inactiveTrackColor = Color.Red.copy(alpha = 0.5f)
            )
        )
        // Instructions
        Text(
            text = "Slider to adjust screen darkness level",
            fontSize = 14.sp,
            color = Color.Red.copy(alpha = 0.5f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
    @Preview(showBackground = true)
    @Composable
    fun DarknessSliderPreview() {
        DarkScreenTheme {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
            ) {
                DarknessSlider(
                    darknessLevel = 0.5f,
                    onDarknessChange = {}
                )
            }
        }
    }




