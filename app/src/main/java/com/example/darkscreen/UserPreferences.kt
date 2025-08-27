package com.example.darkscreen

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to access DataStore
val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPreferences {
    private val BRIGHTNESS_KEY = floatPreferencesKey("brightness_level")
    private val MODE_KEY = stringPreferencesKey("control_mode")

    fun saveBrightness(context: Context, value: Float) {
        context.dataStore.edit { prefs ->
            prefs[BRIGHTNESS_KEY] = value
        }
    }

    fun saveMode(context: Context, mode: String) {
        context.dataStore.edit { prefs ->
            prefs[MODE_KEY] = mode
        }
    }

    fun getBrightness(context: Context): Flow<Float> =
        context.dataStore.data.map { prefs ->
            prefs[BRIGHTNESS_KEY] ?: 0.75f
        }

    fun getMode(context: Context): Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[MODE_KEY] ?: "MANUAL"
        }
}