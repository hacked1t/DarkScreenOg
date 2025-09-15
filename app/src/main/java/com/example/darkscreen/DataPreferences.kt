package com.example.darkscreen

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlin.collections.emptyList

class DarkScreenPreferences(private val context: Context) {
    private val Context.dataStore by preferencesDataStore("user_prefs")
    private val brightnessKey = floatPreferencesKey("brightness_level")
    private val colorKey = stringPreferencesKey("color")
    private val modeKey = stringPreferencesKey("mode")
    suspend fun saveBrightness(brightness: Float) {
        context.dataStore.edit { preferences ->
            preferences[brightnessKey] = brightness
        }
    }

    suspend fun saveColor(color: String) {
        context.dataStore.edit { preferences ->
            preferences[colorKey] = color
        }
    }

    suspend fun saveMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[modeKey] = mode
        }
    }

    private val customPresetsKey = stringPreferencesKey("custom_presets")

    suspend fun saveCustomPresets(presets: List<CustomPreset>) {
        val json = Gson().toJson(presets)
        context.dataStore.edit { it[customPresetsKey] = json }
    }

    val customPresets: Flow<List<CustomPreset>> = context.dataStore.data
        .map { prefs ->
            prefs[customPresetsKey]?.let {
                Gson().fromJson(it, object : TypeToken<List<CustomPreset>>() {}.type)
            } ?: emptyList()
        }
}


