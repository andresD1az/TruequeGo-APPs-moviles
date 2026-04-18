package com.example.truequego_apps_moviles.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryNavy,
    primaryContainer = PrimaryContainer,
    onPrimary = OnPrimary,
    secondary = TertiaryAccent,
    secondaryContainer = TertiaryContainer,
    onSecondary = OnPrimary,
    background = OnSurface,
    surface = OnSurfaceVariant,
    onBackground = SurfaceContainerLowest,
    onSurface = SurfaceContainerLowest,
    error = Error,
    errorContainer = ErrorContainer,
    onError = OnError
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryNavy,
    primaryContainer = PrimaryContainer,
    onPrimary = OnPrimary,
    secondary = TertiaryAccent,
    secondaryContainer = TertiaryContainer,
    onSecondary = OnPrimary,
    background = SurfaceContainerLow,
    surface = SurfaceContainerLowest,
    onBackground = OnSurface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    error = Error,
    errorContainer = ErrorContainer,
    onError = OnError
)

@Composable
fun TruequeGoAPPsmovilesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable dynamic colors to enforce the brand colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}