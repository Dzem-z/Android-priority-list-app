package com.example.prioritylist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Theme1.primary,
    primaryVariant = Theme1.secondary,
    secondary = Theme1.tertiary
)

private val LightColorPalette = lightColors(
    primary = Theme2.primary,
    primaryVariant = Theme2.primary,
    secondary = Theme2.tertiary,


    background = Theme2.secondary,
    surface = Theme2.secondary,
    onPrimary = Theme2.tertiary,
    onSecondary = Theme2.quaternary,
    onBackground = Theme2.tertiary,
    onSurface = Theme2.tertiary,

)

@Composable
fun PriorityListTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}