package dev.sergiosabater.rickmortypedia

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sergiosabater.rickmortypedia.core.navigation.AppNavHost
import dev.sergiosabater.rickmortypedia.core.navigation.AppNavigator
import dev.sergiosabater.rickmortypedia.core.ui.theme.RickMortyPediaTheme
import dev.sergiosabater.rickmortypedia.core.ui.theme.ThemeManager
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val navigator: AppNavigator by inject()
    private val themeManager: ThemeManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkTheme by themeManager.isDarkTheme.collectAsStateWithLifecycle()
            val systemDarkTheme = isSystemInDarkTheme()
            val useDarkTheme = isDarkTheme ?: systemDarkTheme

            RickMortyPediaTheme(darkTheme = useDarkTheme) {
                // Dynamic status bar color configuration
                val view = LocalView.current
                SideEffect {
                    if (!view.isInEditMode) {
                        val window = (view.context as Activity).window
                        WindowCompat.getInsetsController(window, view).apply {
                            isAppearanceLightStatusBars = !useDarkTheme
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavHost(
                        navigator = navigator,
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = { themeManager.toggleTheme() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RickMortyPediaTheme {
    }
}