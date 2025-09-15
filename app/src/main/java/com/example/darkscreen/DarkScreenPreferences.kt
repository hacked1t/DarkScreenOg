package com.example.darkscreen

import kotlinx.coroutines.flow.Flow
import kotlin.collections.emptyList

private val customPresetskey = stringPreferenceKey("custom.presets")

suspend fun saveCustomPresests(presets: List<CustomPresets>) {
    val json = Gson().toJson(presets)
    context.dataStore.edit { it[customPresetsKey] = json }
}

val customPresets: Flow<List<CustomPreset>> context.dataStore.data
        .map { prefs ->
            prefs[customPresetKey]?.let {
                Gson().fromJson(it, object : TypeToken<List<CustomPreset>>() {}
            } ?: emptyList()
}
}