package com.grama.angana.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = Color(0xFF1B5E20),
    secondary = Color(0xFF1976D2),
    tertiary = Color(0xFF42A5F5),
    background = Color(0xFFF5F7FA),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE7EBF1),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF101828),
    onSurface = Color(0xFF111827)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF1B5E20),
    secondary = Color(0xFF1976D2),
    tertiary = Color(0xFF64B5F6),
    background = Color(0xFF0A1017),
    surface = Color(0xFF131C27),
    onPrimary = Color.White,
    onSecondary = Color.White
)

private val GramaTypography = Typography(
    headlineLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 30.sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 26.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 16.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 15.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 14.sp)
)

@Composable
fun GramaAnganaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = GramaTypography,
        content = content
    )
}
