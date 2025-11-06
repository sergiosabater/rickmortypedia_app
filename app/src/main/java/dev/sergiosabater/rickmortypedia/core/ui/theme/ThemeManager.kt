package dev.sergiosabater.rickmortypedia.core.ui.theme

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeManager(
    private val themePreferences: ThemePreferences
) {
    // Scope that lives throughout the application
    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    val isDarkTheme: StateFlow<Boolean?> = themePreferences.isDarkTheme
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly, // Always active
            initialValue = null
        )

    fun toggleTheme() {
        applicationScope.launch {
            themePreferences.toggleTheme()
        }
    }

    fun setDarkTheme(isDark: Boolean) {
        applicationScope.launch {
            themePreferences.setDarkTheme(isDark)
        }
    }
}