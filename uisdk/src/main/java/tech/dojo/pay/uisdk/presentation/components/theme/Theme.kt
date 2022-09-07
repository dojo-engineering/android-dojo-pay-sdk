package tech.dojo.pay.uisdk.presentation.components.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import tech.dojo.pay.uisdk.entities.DojoThemeSettings
import tech.dojo.pay.uisdk.entities.color

internal fun lightColorPalette(dojoThemeSettings: DojoThemeSettings? = null) = DojoColors(
    primary = Color(0xFF262626),
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color(0xFFF5F5F5),
    onSurface = Color.Black,
    secondarySurface = Color(0xFF262626),
    primaryLabelTextColor = dojoThemeSettings?.primaryLabelTextColor?.color ?: Color(0xFF262626),
    headerButtonTintColor = dojoThemeSettings?.headerButtonTintColor?.color ?: Color(0xDE000000),
    headerTintColor = dojoThemeSettings?.headerTintColor?.color ?: Color(0xFF262626),
    secondaryLabelTextColor = dojoThemeSettings?.secondaryLabelTextColor?.color
        ?: Color(0xDE000000),
    primaryCTAButtonActiveBackgroundColor = dojoThemeSettings?.primaryCTAButtonActiveBackgroundColor?.color
        ?: Color.Black,
    onSecondarySurface = Color.White,
    success = Color(0xFF2C7B32),
    onSuccess = Color.White,
    error = Color(0xFFB00020),
    onError = Color.White,
    honey = Color(0xFFFF8D02)
)
private val DarkColorPalette = DojoColors(
    primary = Color(0xFF262626),
    onPrimary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1e1e1e),
    onSurface = Color.White,
    headerButtonTintColor = Color.White,
    headerTintColor = Color.White,
    secondaryLabelTextColor = Color.White,
    primaryLabelTextColor = Color.White,
    primaryCTAButtonActiveBackgroundColor = Color.Black,
    secondarySurface = Color.White,
    onSecondarySurface = Color.Black,
    success = Color(0xFF88B484),
    onSuccess = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.White,
    honey = Color(0xFFFF8D02)
)

@Composable
fun DojoTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else lightColorPalette()

    CompositionLocalProvider(LocalDojoColors provides colors) {
        MaterialTheme(
            colors = materialColors(colors, darkTheme),
            typography = DojoTypography,
            content = content
        )
    }
}

object DojoTheme {
    val colors: DojoColors
        @Composable
        get() = LocalDojoColors.current

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes
}

@Stable
class DojoColors(
    primary: Color,
    onPrimary: Color,
    background: Color,
    onBackground: Color,
    surface: Color,
    onSurface: Color,
    secondarySurface: Color,
    onSecondarySurface: Color,
    success: Color,
    onSuccess: Color,
    error: Color,
    onError: Color,
    headerButtonTintColor: Color,
    headerTintColor: Color,
    secondaryLabelTextColor: Color,
    primaryLabelTextColor: Color,
    primaryCTAButtonActiveBackgroundColor: Color,
    honey: Color
) {
    var primary by mutableStateOf(primary)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var background by mutableStateOf(background)
        private set
    var onBackground by mutableStateOf(onBackground)
        private set
    var surface by mutableStateOf(surface)
        private set
    var onSurface by mutableStateOf(onSurface)
        private set
    var secondarySurface by mutableStateOf(secondarySurface)
        private set
    var onSecondarySurface by mutableStateOf(onSecondarySurface)
        private set
    var success by mutableStateOf(success)
        private set
    var onSuccess by mutableStateOf(onSuccess)
        private set
    var error by mutableStateOf(error)
        private set
    var onError by mutableStateOf(onError)
        private set
    var honey by mutableStateOf(honey)
        private set
    var headerButtonTintColor by mutableStateOf(headerButtonTintColor)
        private set
    var headerTintColor by mutableStateOf(headerTintColor)
        private set
    var secondaryLabelTextColor by mutableStateOf(secondaryLabelTextColor)
        private set
    var primaryLabelTextColor by mutableStateOf(primaryLabelTextColor)
        private set
    var primaryCTAButtonActiveBackgroundColor by mutableStateOf(
        primaryCTAButtonActiveBackgroundColor
    )
        private set
}

internal val LocalDojoColors = staticCompositionLocalOf<DojoColors> {
    error("No DojoColorPalette provided")
}

private fun materialColors(
    dojoColors: DojoColors,
    darkTheme: Boolean
) = Colors(
    primary = dojoColors.primary,
    onPrimary = dojoColors.onPrimary,
    primaryVariant = dojoColors.primary,
    secondary = dojoColors.primary,
    onSecondary = dojoColors.onPrimary,
    secondaryVariant = dojoColors.primary,
    background = dojoColors.background,
    onBackground = dojoColors.onBackground,
    surface = dojoColors.surface,
    onSurface = dojoColors.onSurface,
    error = dojoColors.error,
    onError = dojoColors.onError,
    isLight = !darkTheme
)
