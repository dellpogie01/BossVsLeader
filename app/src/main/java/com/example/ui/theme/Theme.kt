package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = LeaderBlue,
        onPrimary = androidx.compose.ui.graphics.Color.White,
        secondary = BossRed,
        onSecondary = androidx.compose.ui.graphics.Color.White,
        tertiary = TealAccent,
        background = NeutralDarkBackground,
        surface = NeutralDarkSurface,
        onBackground = TextPrimaryDark,
        onSurface = TextPrimaryDark
    )

private val LightColorScheme =
    lightColorScheme(
        primary = LeaderBlue,
        onPrimary = androidx.compose.ui.graphics.Color.White,
        secondary = BossRed,
        onSecondary = androidx.compose.ui.graphics.Color.White,
        tertiary = TealAccent,
        background = NeutralLightBackground,
        surface = NeutralLightSurface,
        onBackground = TextPrimaryLight,
        onSurface = TextPrimaryLight
    )

@Composable
fun BossVsLeaderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // default to custom branded theme
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
