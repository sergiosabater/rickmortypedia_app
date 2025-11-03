package dev.sergiosabater.rickmortypedia.core.ui.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

class ThemePreferences(
    private val context: Context
) {
    private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")

    val isDarkTheme: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_THEME_KEY]
        }

    suspend fun toggleTheme() {
        context.dataStore.edit { preferences ->
            val current = preferences[DARK_THEME_KEY] ?: false
            preferences[DARK_THEME_KEY] = !current
        }
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }
}