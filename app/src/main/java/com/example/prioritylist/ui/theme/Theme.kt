package com.example.prioritylist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable


import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme as material3
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

enum class ThemeID(val value: Int, val themeName: String) {
    FIRST(1, "theme 1"),
    SECOND(2, "theme 2"),
    THIRD(3, "theme 3"),
}

val aThemeID = ThemeID.THIRD

@Composable
fun PriorityListTheme(
    themeId: ThemeID,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val lightColors = resolveLightColors(themeId)
    val darkColors = resolveDarkColors(themeId)

    val colors = if (!useDarkTheme) {
        lightColors
    } else {
        darkColors
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = colors.primaryContainer
    )

    material3(
        colorScheme = colors,
        content = content
    )
}

fun resolveLightColors(theme: ThemeID): ColorScheme {
    return when (theme) {
        ThemeID.FIRST -> lightColorScheme(
            primary = md_theme1_light_primary,
            onPrimary = md_theme1_light_onPrimary,
            primaryContainer = md_theme1_light_primaryContainer,
            onPrimaryContainer = md_theme1_light_onPrimaryContainer,
            secondary = md_theme1_light_secondary,
            onSecondary = md_theme1_light_onSecondary,
            secondaryContainer = md_theme1_light_secondaryContainer,
            onSecondaryContainer = md_theme1_light_onSecondaryContainer,
            tertiary = md_theme1_light_tertiary,
            onTertiary = md_theme1_light_onTertiary,
            tertiaryContainer = md_theme1_light_tertiaryContainer,
            onTertiaryContainer = md_theme1_light_onTertiaryContainer,
            error = md_theme1_light_error,
            errorContainer = md_theme1_light_errorContainer,
            onError = md_theme1_light_onError,
            onErrorContainer = md_theme1_light_onErrorContainer,
            background = md_theme1_light_background,
            onBackground = md_theme1_light_onBackground,
            surface = md_theme1_light_surface,
            onSurface = md_theme1_light_onSurface,
            surfaceVariant = md_theme1_light_surfaceVariant,
            onSurfaceVariant = md_theme1_light_onSurfaceVariant,
            outline = md_theme1_light_outline,
            inverseOnSurface = md_theme1_light_inverseOnSurface,
            inverseSurface = md_theme1_light_inverseSurface,
            inversePrimary = md_theme1_light_inversePrimary,
            surfaceTint = md_theme1_light_surfaceTint,
            outlineVariant = md_theme1_light_outlineVariant,
            scrim = md_theme1_light_scrim,
        )
        ThemeID.SECOND -> lightColorScheme(
            primary = md_theme2_light_primary,
            onPrimary = md_theme2_light_onPrimary,
            primaryContainer = md_theme2_light_primaryContainer,
            onPrimaryContainer = md_theme2_light_onPrimaryContainer,
            secondary = md_theme2_light_secondary,
            onSecondary = md_theme2_light_onSecondary,
            secondaryContainer = md_theme2_light_secondaryContainer,
            onSecondaryContainer = md_theme2_light_onSecondaryContainer,
            tertiary = md_theme2_light_tertiary,
            onTertiary = md_theme2_light_onTertiary,
            tertiaryContainer = md_theme2_light_tertiaryContainer,
            onTertiaryContainer = md_theme2_light_onTertiaryContainer,
            error = md_theme2_light_error,
            errorContainer = md_theme2_light_errorContainer,
            onError = md_theme2_light_onError,
            onErrorContainer = md_theme2_light_onErrorContainer,
            background = md_theme2_light_background,
            onBackground = md_theme2_light_onBackground,
            surface = md_theme2_light_surface,
            onSurface = md_theme2_light_onSurface,
            surfaceVariant = md_theme2_light_surfaceVariant,
            onSurfaceVariant = md_theme2_light_onSurfaceVariant,
            outline = md_theme2_light_outline,
            inverseOnSurface = md_theme2_light_inverseOnSurface,
            inverseSurface = md_theme2_light_inverseSurface,
            inversePrimary = md_theme2_light_inversePrimary,
            surfaceTint = md_theme2_light_surfaceTint,
            outlineVariant = md_theme2_light_outlineVariant,
            scrim = md_theme2_light_scrim,
        )
        ThemeID.THIRD -> lightColorScheme(
            primary = md_theme3_light_primary,
            onPrimary = md_theme3_light_onPrimary,
            primaryContainer = md_theme3_light_primaryContainer,
            onPrimaryContainer = md_theme3_light_onPrimaryContainer,
            secondary = md_theme3_light_secondary,
            onSecondary = md_theme3_light_onSecondary,
            secondaryContainer = md_theme3_light_secondaryContainer,
            onSecondaryContainer = md_theme3_light_onSecondaryContainer,
            tertiary = md_theme3_light_tertiary,
            onTertiary = md_theme3_light_onTertiary,
            tertiaryContainer = md_theme3_light_tertiaryContainer,
            onTertiaryContainer = md_theme3_light_onTertiaryContainer,
            error = md_theme3_light_error,
            errorContainer = md_theme3_light_errorContainer,
            onError = md_theme3_light_onError,
            onErrorContainer = md_theme3_light_onErrorContainer,
            background = md_theme3_light_background,
            onBackground = md_theme3_light_onBackground,
            surface = md_theme3_light_surface,
            onSurface = md_theme3_light_onSurface,
            surfaceVariant = md_theme3_light_surfaceVariant,
            onSurfaceVariant = md_theme3_light_onSurfaceVariant,
            outline = md_theme3_light_outline,
            inverseOnSurface = md_theme3_light_inverseOnSurface,
            inverseSurface = md_theme3_light_inverseSurface,
            inversePrimary = md_theme3_light_inversePrimary,
            surfaceTint = md_theme3_light_surfaceTint,
            outlineVariant = md_theme3_light_outlineVariant,
            scrim = md_theme3_light_scrim,
        )
    }
}

fun resolveDarkColors(theme: ThemeID): ColorScheme {
    return when(theme) {
        ThemeID.FIRST -> darkColorScheme(
            primary = md_theme1_dark_primary,
            onPrimary = md_theme1_dark_onPrimary,
            primaryContainer = md_theme1_dark_primaryContainer,
            onPrimaryContainer = md_theme1_dark_onPrimaryContainer,
            secondary = md_theme1_dark_secondary,
            onSecondary = md_theme1_dark_onSecondary,
            secondaryContainer = md_theme1_dark_secondaryContainer,
            onSecondaryContainer = md_theme1_dark_onSecondaryContainer,
            tertiary = md_theme1_dark_tertiary,
            onTertiary = md_theme1_dark_onTertiary,
            tertiaryContainer = md_theme1_dark_tertiaryContainer,
            onTertiaryContainer = md_theme1_dark_onTertiaryContainer,
            error = md_theme1_dark_error,
            errorContainer = md_theme1_dark_errorContainer,
            onError = md_theme1_dark_onError,
            onErrorContainer = md_theme1_dark_onErrorContainer,
            background = md_theme1_dark_background,
            onBackground = md_theme1_dark_onBackground,
            surface = md_theme1_dark_surface,
            onSurface = md_theme1_dark_onSurface,
            surfaceVariant = md_theme1_dark_surfaceVariant,
            onSurfaceVariant = md_theme1_dark_onSurfaceVariant,
            outline = md_theme1_dark_outline,
            inverseOnSurface = md_theme1_dark_inverseOnSurface,
            inverseSurface = md_theme1_dark_inverseSurface,
            inversePrimary = md_theme1_dark_inversePrimary,
            surfaceTint = md_theme1_dark_surfaceTint,
            outlineVariant = md_theme1_dark_outlineVariant,
            scrim = md_theme1_dark_scrim,
        )
        ThemeID.SECOND -> darkColorScheme(
            primary = md_theme2_dark_primary,
            onPrimary = md_theme2_dark_onPrimary,
            primaryContainer = md_theme2_dark_primaryContainer,
            onPrimaryContainer = md_theme2_dark_onPrimaryContainer,
            secondary = md_theme2_dark_secondary,
            onSecondary = md_theme2_dark_onSecondary,
            secondaryContainer = md_theme2_dark_secondaryContainer,
            onSecondaryContainer = md_theme2_dark_onSecondaryContainer,
            tertiary = md_theme2_dark_tertiary,
            onTertiary = md_theme2_dark_onTertiary,
            tertiaryContainer = md_theme2_dark_tertiaryContainer,
            onTertiaryContainer = md_theme2_dark_onTertiaryContainer,
            error = md_theme2_dark_error,
            errorContainer = md_theme2_dark_errorContainer,
            onError = md_theme2_dark_onError,
            onErrorContainer = md_theme2_dark_onErrorContainer,
            background = md_theme2_dark_background,
            onBackground = md_theme2_dark_onBackground,
            surface = md_theme2_dark_surface,
            onSurface = md_theme2_dark_onSurface,
            surfaceVariant = md_theme2_dark_surfaceVariant,
            onSurfaceVariant = md_theme2_dark_onSurfaceVariant,
            outline = md_theme2_dark_outline,
            inverseOnSurface = md_theme2_dark_inverseOnSurface,
            inverseSurface = md_theme2_dark_inverseSurface,
            inversePrimary = md_theme2_dark_inversePrimary,
            surfaceTint = md_theme2_dark_surfaceTint,
            outlineVariant = md_theme2_dark_outlineVariant,
            scrim = md_theme2_dark_scrim,
        )
        ThemeID.THIRD -> darkColorScheme(
            primary = md_theme3_dark_primary,
            onPrimary = md_theme3_dark_onPrimary,
            primaryContainer = md_theme3_dark_primaryContainer,
            onPrimaryContainer = md_theme3_dark_onPrimaryContainer,
            secondary = md_theme3_dark_secondary,
            onSecondary = md_theme3_dark_onSecondary,
            secondaryContainer = md_theme3_dark_secondaryContainer,
            onSecondaryContainer = md_theme3_dark_onSecondaryContainer,
            tertiary = md_theme3_dark_tertiary,
            onTertiary = md_theme3_dark_onTertiary,
            tertiaryContainer = md_theme3_dark_tertiaryContainer,
            onTertiaryContainer = md_theme3_dark_onTertiaryContainer,
            error = md_theme3_dark_error,
            errorContainer = md_theme3_dark_errorContainer,
            onError = md_theme3_dark_onError,
            onErrorContainer = md_theme3_dark_onErrorContainer,
            background = md_theme3_dark_background,
            onBackground = md_theme3_dark_onBackground,
            surface = md_theme3_dark_surface,
            onSurface = md_theme3_dark_onSurface,
            surfaceVariant = md_theme3_dark_surfaceVariant,
            onSurfaceVariant = md_theme3_dark_onSurfaceVariant,
            outline = md_theme3_dark_outline,
            inverseOnSurface = md_theme3_dark_inverseOnSurface,
            inverseSurface = md_theme3_dark_inverseSurface,
            inversePrimary = md_theme3_dark_inversePrimary,
            surfaceTint = md_theme3_dark_surfaceTint,
            outlineVariant = md_theme3_dark_outlineVariant,
            scrim = md_theme3_dark_scrim,
        )
    }
}