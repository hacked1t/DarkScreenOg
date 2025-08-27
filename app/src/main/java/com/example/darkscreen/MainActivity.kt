package com.example.darkscreen

import android.content.Intent
import android.graphics.Color.argb
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {

    enum class ControlMode {
        MANUAL,
        PRESET
    }

    private val presets = listOf(
        Preset("Midnight Ritual", 0.2f, argb(200, 0, 0, 50), 0xFF001133.toInt()),
        Preset("Haunted Library", 0.4f, argb(180, 30, 10, 0), 0xFF330000.toInt()),
        Preset("Cyber Abyss", 0.3f, argb(220, 40, 0, 60), 0xFF000033.toInt()),
        Preset("Blood Moon", 0.1f, argb(230, 80, 0, 0), 0xFF330000.toInt())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request overlay permission
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                "package:$packageName".toUri()
            )
            startActivity(intent)
        }

        val brightnessFlow = UserPreferences.getBrightness(this)
        val modeFlow = UserPreferences.getMode(this)

        setContent {
            val brightness by brightnessFlow.collectAsState(initial = 0.75f)
            val modeString by modeFlow.collectAsState(initial = "MANUAL")
            var mode by remember { mutableStateOf(ControlMode.valueOf(modeString)) }

            Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // 🔘 Mode Toggle
                    Row {
                        Button(
                            onClick = {
                                mode = ControlMode.MANUAL
                                UserPreferences.saveMode(this@MainActivity, mode.name)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (mode == ControlMode.MANUAL) Color.Red else Color.DarkGray
                            )
                        ) {
                            Text("Manual", color = Color.White)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                mode = ControlMode.PRESET
                                UserPreferences.saveMode(this@MainActivity, mode.name)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (mode == ControlMode.PRESET) Color.Red else Color.DarkGray
                            )
                        ) {
                            Text("Presets", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 🎛️ Manual Mode
                    if (mode == ControlMode.MANUAL) {
                        DarknessSlider(
                            brightness = brightness,
                            onBrightnessChange = { newBrightness ->
                                UserPreferences.saveBrightness(this@MainActivity, newBrightness)
                                val overlayAlpha = 1.0f - newBrightness
                                if (Settings.canDrawOverlays(this@MainActivity)) {
                                    OverlayManager.applyOverlay(this@MainActivity, overlayAlpha)
                                } else {
                                    Toast.makeText(this@MainActivity, "Overlay permission required", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }

                    // 🎨 Preset Mode
                    if (mode == ControlMode.PRESET) {
                        presets.forEach { preset ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(animationSpec = tween(500))
                            ) {
                                PresetCard(
                                    preset = preset,
                                    onApplyClicked = { selected ->
                                        UserPreferences.saveBrightness(this@MainActivity, selected.brightness)
                                        if (Settings.canDrawOverlays(this@MainActivity)) {
                                            OverlayManager.applyOverlay(this@MainActivity, selected.brightness)
                                            Toast.makeText(this@MainActivity, "Applied ${selected.name}", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this@MainActivity, "Overlay permission required", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}