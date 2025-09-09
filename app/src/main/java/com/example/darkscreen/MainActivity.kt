package com.example.darkscreen

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.darkscreen.ui.theme.DarkScreenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DarkScreenTheme {
                MainScreen()
            }
        }
    }
}
@Composable
fun MainScreen() {
    val context = LocalActivity.current as Activity
    val appContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    // State variables
    var darknessLevel by remember { mutableFloatStateOf(0.5f) }
    var selectedPreset by remember { mutableStateOf<PresetData?>(null) }
    var isInitialized by remember { mutableStateOf(false) }
    
    // Create preferences instance with safer initialization
    val presetPreferences = remember { PresetPreferences(appContext) }
    
    // Collect saved preferences only after a brief initialization delay
    val savedPresetName by presetPreferences.selectedPresetName.collectAsState(initial = null)
    val savedDarknessLevel by presetPreferences.darknessLevel.collectAsState(initial = 0.5f)
    
    // Initialize DataStore safely
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(200) // Allow DataStore to initialize
        isInitialized = true
        android.util.Log.d("MainActivity", "DataStore initialized")
    }
    
    // Function to apply brightness changes
    val applyBrightness = { level: Float ->
        val brightnessLevel = 1.0f - level
        val adjustedBrightness = brightnessLevel.coerceAtLeast(0.01f)
        val layoutParams = context.window.attributes
        layoutParams.screenBrightness = adjustedBrightness
        context.window.attributes = layoutParams
    }
    
    // Restore saved settings on startup (only after initialization)
    LaunchedEffect(isInitialized, savedPresetName, savedDarknessLevel) {
        if (isInitialized) {
            android.util.Log.d("MainActivity", "Restoring settings: preset=$savedPresetName, darkness=$savedDarknessLevel")
            darknessLevel = savedDarknessLevel
            selectedPreset = savedPresetName?.let { name ->
                val preset = DarkPresets.getPresetByName(name)
                android.util.Log.d("MainActivity", "Found preset: $preset")
                preset
            }
        }
    }
    
    // Apply brightness when darkness level changes (including on startup)
    LaunchedEffect(darknessLevel) {
        applyBrightness(darknessLevel)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            // Main content area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Preset Selector
                PresetSelector(
                    selectedPreset = selectedPreset,
                    onPresetSelected = { preset ->
                        android.util.Log.d("MainActivity", "Preset selected: $preset")
                        selectedPreset = preset
                        if (preset != null) {
                            darknessLevel = preset.darknessLevel
                            applyBrightness(preset.darknessLevel)
                            // Save preset selection to DataStore
                            if (isInitialized) {
                                coroutineScope.launch {
                                    android.util.Log.d("MainActivity", "Saving preset: ${preset.name}")
                                    kotlinx.coroutines.delay(50) // Small delay to prevent concurrent access
                                    presetPreferences.savePresetSettings(
                                        presetName = preset.name,
                                        darknessLevel = preset.darknessLevel
                                    )
                                }
                            }
                        } else {
                            // Custom mode selected - save null preset but keep current darkness level
                            if (isInitialized) {
                                coroutineScope.launch {
                                    android.util.Log.d("MainActivity", "Clearing preset, saving darkness: $darknessLevel")
                                    kotlinx.coroutines.delay(50) // Small delay to prevent concurrent access
                                    presetPreferences.savePresetSettings(
                                        presetName = null,
                                        darknessLevel = darknessLevel
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Darkness Slider
                DarknessSlider(
                    darknessLevel = darknessLevel,
                    onDarknessChange = { newLevel ->
                        android.util.Log.d("MainActivity", "Slider changed to: $newLevel")
                        darknessLevel = newLevel
                        selectedPreset = null // Clear preset when manual adjustment is made
                        applyBrightness(newLevel)
                        // Save manual darkness level to DataStore
                        if (isInitialized) {
                            coroutineScope.launch {
                                android.util.Log.d("MainActivity", "Saving manual darkness: $newLevel")
                                kotlinx.coroutines.delay(50) // Small delay to prevent concurrent access
                                presetPreferences.savePresetSettings(
                                    presetName = null, // No preset when manually adjusted
                                    darknessLevel = newLevel
                                )
                            }
                        }
                    }
                )
            }
        }
        // Darkness overlay
        if (darknessLevel > 0f) {
            val overlayColor = selectedPreset?.overlayColor ?: Color.Black
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        overlayColor.copy(
                            alpha = darknessLevel * 0.8f
                        )
                    )
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DarkScreenTheme {
        MainScreen()
    }
}
