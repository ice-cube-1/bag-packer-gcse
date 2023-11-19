/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.a202310212.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LightColors1 = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


val DarkColors1 = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

val LightColors2 = lightColorScheme(
    primary = md2_theme_light_primary,
    onPrimary = md2_theme_light_onPrimary,
    primaryContainer = md2_theme_light_primaryContainer,
    onPrimaryContainer = md2_theme_light_onPrimaryContainer,
    secondary = md2_theme_light_secondary,
    onSecondary = md2_theme_light_onSecondary,
    secondaryContainer = md2_theme_light_secondaryContainer,
    onSecondaryContainer = md2_theme_light_onSecondaryContainer,
    tertiary = md2_theme_light_tertiary,
    onTertiary = md2_theme_light_onTertiary,
    tertiaryContainer = md2_theme_light_tertiaryContainer,
    onTertiaryContainer = md2_theme_light_onTertiaryContainer,
    error = md2_theme_light_error,
    errorContainer = md2_theme_light_errorContainer,
    onError = md2_theme_light_onError,
    onErrorContainer = md2_theme_light_onErrorContainer,
    background = md2_theme_light_background,
    onBackground = md2_theme_light_onBackground,
    surface = md2_theme_light_surface,
    onSurface = md2_theme_light_onSurface,
    surfaceVariant = md2_theme_light_surfaceVariant,
    onSurfaceVariant = md2_theme_light_onSurfaceVariant,
    outline = md2_theme_light_outline,
    inverseOnSurface = md2_theme_light_inverseOnSurface,
    inverseSurface = md2_theme_light_inverseSurface,
    inversePrimary = md2_theme_light_inversePrimary,
    surfaceTint = md2_theme_light_surfaceTint,
    outlineVariant = md2_theme_light_outlineVariant,
    scrim = md2_theme_light_scrim,
)


val DarkColors2 = darkColorScheme(
    primary = md2_theme_dark_primary,
    onPrimary = md2_theme_dark_onPrimary,
    primaryContainer = md2_theme_dark_primaryContainer,
    onPrimaryContainer = md2_theme_dark_onPrimaryContainer,
    secondary = md2_theme_dark_secondary,
    onSecondary = md2_theme_dark_onSecondary,
    secondaryContainer = md2_theme_dark_secondaryContainer,
    onSecondaryContainer = md2_theme_dark_onSecondaryContainer,
    tertiary = md2_theme_dark_tertiary,
    onTertiary = md2_theme_dark_onTertiary,
    tertiaryContainer = md2_theme_dark_tertiaryContainer,
    onTertiaryContainer = md2_theme_dark_onTertiaryContainer,
    error = md2_theme_dark_error,
    errorContainer = md2_theme_dark_errorContainer,
    onError = md2_theme_dark_onError,
    onErrorContainer = md2_theme_dark_onErrorContainer,
    background = md2_theme_dark_background,
    onBackground = md2_theme_dark_onBackground,
    surface = md2_theme_dark_surface,
    onSurface = md2_theme_dark_onSurface,
    surfaceVariant = md2_theme_dark_surfaceVariant,
    onSurfaceVariant = md2_theme_dark_onSurfaceVariant,
    outline = md2_theme_dark_outline,
    inverseOnSurface = md2_theme_dark_inverseOnSurface,
    inverseSurface = md2_theme_dark_inverseSurface,
    inversePrimary = md2_theme_dark_inversePrimary,
    surfaceTint = md2_theme_dark_surfaceTint,
    outlineVariant = md2_theme_dark_outlineVariant,
    scrim = md2_theme_dark_scrim,
)


val LightColors3 = lightColorScheme(
    primary = md3_theme_light_primary,
    onPrimary = md3_theme_light_onPrimary,
    primaryContainer = md3_theme_light_primaryContainer,
    onPrimaryContainer = md3_theme_light_onPrimaryContainer,
    secondary = md3_theme_light_secondary,
    onSecondary = md3_theme_light_onSecondary,
    secondaryContainer = md3_theme_light_secondaryContainer,
    onSecondaryContainer = md3_theme_light_onSecondaryContainer,
    tertiary = md3_theme_light_tertiary,
    onTertiary = md3_theme_light_onTertiary,
    tertiaryContainer = md3_theme_light_tertiaryContainer,
    onTertiaryContainer = md3_theme_light_onTertiaryContainer,
    error = md3_theme_light_error,
    errorContainer = md3_theme_light_errorContainer,
    onError = md3_theme_light_onError,
    onErrorContainer = md3_theme_light_onErrorContainer,
    background = md3_theme_light_background,
    onBackground = md3_theme_light_onBackground,
    surface = md3_theme_light_surface,
    onSurface = md3_theme_light_onSurface,
    surfaceVariant = md3_theme_light_surfaceVariant,
    onSurfaceVariant = md3_theme_light_onSurfaceVariant,
    outline = md3_theme_light_outline,
    inverseOnSurface = md3_theme_light_inverseOnSurface,
    inverseSurface = md3_theme_light_inverseSurface,
    inversePrimary = md3_theme_light_inversePrimary,
    surfaceTint = md3_theme_light_surfaceTint,
    outlineVariant = md3_theme_light_outlineVariant,
    scrim = md3_theme_light_scrim,
)


val DarkColors3 = darkColorScheme(
    primary = md3_theme_dark_primary,
    onPrimary = md3_theme_dark_onPrimary,
    primaryContainer = md3_theme_dark_primaryContainer,
    onPrimaryContainer = md3_theme_dark_onPrimaryContainer,
    secondary = md3_theme_dark_secondary,
    onSecondary = md3_theme_dark_onSecondary,
    secondaryContainer = md3_theme_dark_secondaryContainer,
    onSecondaryContainer = md3_theme_dark_onSecondaryContainer,
    tertiary = md3_theme_dark_tertiary,
    onTertiary = md3_theme_dark_onTertiary,
    tertiaryContainer = md3_theme_dark_tertiaryContainer,
    onTertiaryContainer = md3_theme_dark_onTertiaryContainer,
    error = md3_theme_dark_error,
    errorContainer = md3_theme_dark_errorContainer,
    onError = md3_theme_dark_onError,
    onErrorContainer = md3_theme_dark_onErrorContainer,
    background = md3_theme_dark_background,
    onBackground = md3_theme_dark_onBackground,
    surface = md3_theme_dark_surface,
    onSurface = md3_theme_dark_onSurface,
    surfaceVariant = md3_theme_dark_surfaceVariant,
    onSurfaceVariant = md3_theme_dark_onSurfaceVariant,
    outline = md3_theme_dark_outline,
    inverseOnSurface = md3_theme_dark_inverseOnSurface,
    inverseSurface = md3_theme_dark_inverseSurface,
    inversePrimary = md3_theme_dark_inversePrimary,
    surfaceTint = md3_theme_dark_surfaceTint,
    outlineVariant = md3_theme_dark_outlineVariant,
    scrim = md3_theme_dark_scrim,
)

@Composable
fun WoofTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    customDarkColorScheme: ColorScheme = LightColors1, // Specify your custom dark color scheme
    customLightColorScheme: ColorScheme = DarkColors1, // Specify your custom light color scheme
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> customDarkColorScheme
        else -> customLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}