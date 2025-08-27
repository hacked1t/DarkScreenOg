package com.example.darkscreen

import android.R
import android.R.attr.mode
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

