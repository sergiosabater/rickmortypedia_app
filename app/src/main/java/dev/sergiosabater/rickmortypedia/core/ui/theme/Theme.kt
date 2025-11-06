package dev.sergiosabater.rickmortypedia.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import dev.sergiosabater.rickmortypedia.R

val LightColorScheme = lightColorScheme(
    // Main
    primary = PortalGreen,
    onPrimary = Color.White,
    primaryContainer = PortalGreenDark,
    onPrimaryContainer = TextPrimaryLight,

    // Secondary
    secondary = SpacePurple,
    onSecondary = Color.White,
    secondaryContainer = SpacePurpleLight,
    onSecondaryContainer = TextPrimaryLight,

    // Tertiary
    tertiary = LabGreen,
    onTertiary = Color.White,
    tertiaryContainer = LabGreenVariant,
    onTertiaryContainer = TextPrimaryLight,

    // Backgrounds and Surfaces
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = CardLight,
    onSurfaceVariant = TextSecondaryLight,

    // Utilities
    error = ErrorColor,
    onError = Color.White,
    outline = Color(0xFFE0E0E0),
    outlineVariant = Color(0xFFF5F5F5)
)

val DarkColorScheme = darkColorScheme(
    // Main - Green Neon Portal
    primary = PortalGreenNeon,
    onPrimary = SpaceBlack,
    primaryContainer = PortalGreenNeon,
    onPrimaryContainer = TextPrimaryDark,

    // Secondary - Purple Neon
    secondary = NeonPurple,
    onSecondary = SpaceBlack,
    secondaryContainer = NeonPurpleDark,
    onSecondaryContainer = TextPrimaryDark,

    // Tertiary
    tertiary = LabGreen,
    onTertiary = SpaceBlack,
    tertiaryContainer = LabGreenVariant,
    onTertiaryContainer = TextPrimaryDark,

    // Backgrounds and Surfaces
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = CardDark,
    onSurfaceVariant = TextSecondaryDark,

    // Utilities
    error = ErrorColor,
    onError = Color.White,
    outline = Color(0xFF2A2F4A),
    outlineVariant = Color(0xFF1F2437)
)

val RickAndMortyFontFamily = FontFamily(
    Font(R.font.rick_and_morty)
)

@Composable
fun RickMortyPediaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val targetColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val themeTransition = updateTransition(targetState = darkTheme, label = "theme_transition")

    val background by themeTransition.animateColor(label = "background") { isDark ->
        if (isDark) targetColorScheme.background else targetColorScheme.background
    }
    val surface by themeTransition.animateColor(label = "surface") { isDark ->
        if (isDark) targetColorScheme.surface else targetColorScheme.surface
    }
    val onBackground by themeTransition.animateColor(label = "onBackground") { isDark ->
        if (isDark) targetColorScheme.onBackground else targetColorScheme.onBackground
    }
    val onSurface by themeTransition.animateColor(label = "onSurface") { isDark ->
        if (isDark) targetColorScheme.onSurface else targetColorScheme.onSurface
    }
    val primary by themeTransition.animateColor(label = "primary") { isDark ->
        if (isDark) targetColorScheme.primary else targetColorScheme.primary
    }

    val animatedColorScheme = targetColorScheme.copy(
        primary = primary,
        background = background,
        surface = surface,
        onBackground = onBackground,
        onSurface = onSurface
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        LaunchedEffect(background) {
            val window = (view.context as Activity).window
            window.statusBarColor = background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = Typography,
        content = content
    )
}