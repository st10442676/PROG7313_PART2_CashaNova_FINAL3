package com.cashanova.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Cashanova Colour Palette ──────────────────────────────────────────────────
val Gold          = Color(0xFFFFD700)
val GoldDark      = Color(0xFFB8860B)
val GoldLight     = Color(0xFFFFF0A0)
val GoldButton    = Color(0xFFCDA434)
val Black         = Color(0xFF000000)
val BlackCard     = Color(0xFF1A1A1A)
val BlackSurface  = Color(0xFF0D0D0D)
val DarkGray      = Color(0xFF2A2A2A)
val MediumGray    = Color(0xFF3A3A3A)
val TextWhite     = Color(0xFFFFFFFF)
val TextGray      = Color(0xFFAAAAAA)
val ErrorRed      = Color(0xFFFF4444)
val SuccessGreen  = Color(0xFF4CAF50)
val WarningOrange = Color(0xFFFF9800)

private val CashanovaDarkColors = darkColorScheme(
    primary        = Gold,
    onPrimary      = Black,
    secondary      = GoldDark,
    onSecondary    = TextWhite,
    background     = Black,
    onBackground   = TextWhite,
    surface        = BlackCard,
    onSurface      = TextWhite,
    error          = ErrorRed,
    onError        = TextWhite,
    outline        = Gold
)

@Composable
fun CashanovaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CashanovaDarkColors,
        typography  = Typography,
        content     = content
    )
}