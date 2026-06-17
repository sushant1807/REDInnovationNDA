package com.redinnovationlabs.redinnovationnda.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = RedNdaRed,
    onPrimary = RedNdaWhite,
    secondary = RedNdaBlack,
    onSecondary = RedNdaWhite,
    tertiary = RedNdaDarkGray,
    onTertiary = RedNdaWhite,
    background = RedNdaWhite,
    onBackground = RedNdaBlack,
    surface = RedNdaWhite,
    onSurface = RedNdaBlack,
    surfaceVariant = RedNdaLightGray,
    outline = RedNdaBorderGray,
    outlineVariant = RedNdaBorderGray,
    error = RedNdaRed,
    onError = RedNdaWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = RedNdaRed,
    onPrimary = RedNdaWhite,
    secondary = RedNdaWhite,
    onSecondary = RedNdaBlack,
    tertiary = RedNdaBorderGray
)

private val RedNdaShapes = androidx.compose.material3.Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun RedNdaTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = RedNdaTypography,
        shapes = RedNdaShapes,
        content = content
    )
}
