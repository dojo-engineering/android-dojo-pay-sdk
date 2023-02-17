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
import tech.dojo.pay.uisdk.entities.DarkColorPalette
import tech.dojo.pay.uisdk.entities.LightColorPalette
import tech.dojo.pay.uisdk.entities.color

internal fun lightColorPalette(lightColorPalette: LightColorPalette = LightColorPalette()) =
    DojoColors(
        primary = lightColorPalette.inputElementActiveTintColor.color,
        onPrimary = Color.White,
        background = Color.White,
        onBackground = Color.Black,
        surface = lightColorPalette.primarySurfaceBackgroundColor.color,
        onSurface = Color.Black,
        secondarySurface = Color(0xFF262626),
        onSecondarySurface = Color.White,
        success = Color(0xFF2C7B32),
        error = lightColorPalette.errorTextColor.color,
        onError = Color.White,
        primaryLabelTextColor = lightColorPalette.primaryLabelTextColor.color,
        secondaryLabelTextColor = lightColorPalette.secondaryLabelTextColor.color,
        headerTintColor = lightColorPalette.headerTintColor.color,
        headerButtonTintColor = lightColorPalette.headerButtonTintColor.color,
        primarySurfaceBackgroundColor = lightColorPalette.primarySurfaceBackgroundColor.color,
        primaryCTAButtonActiveBackgroundColor = lightColorPalette.primaryCTAButtonActiveBackgroundColor.color,
        primaryCTAButtonActiveTextColor = lightColorPalette.primaryCTAButtonActiveTextColor.color,
        primaryCTAButtonDisabledBackgroundColor = lightColorPalette.primaryCTAButtonDisabledBackgroundColor.color,
        primaryCTAButtonDisableTextColor = lightColorPalette.primaryCTAButtonDisableTextColor.color,
        secondaryCTAButtonActiveBorderColor = lightColorPalette.secondaryCTAButtonActiveBorderColor.color,
        secondaryCTAButtonActiveTextColor = lightColorPalette.secondaryCTAButtonActiveTextColor.color,
        separatorColor = lightColorPalette.separatorColor.color,
        errorTextColor = lightColorPalette.errorTextColor.color,
        loadingIndicatorColor = lightColorPalette.loadingIndicatorColor.color,
        inputFieldPlaceholderColor = lightColorPalette.inputFieldPlaceholderColor.color,
        inputFieldBackgroundColor = lightColorPalette.inputFieldBackgroundColor.color,
        inputFieldDefaultBorderColor = lightColorPalette.inputFieldDefaultBorderColor.color,
        inputFieldSelectedBorderColor = lightColorPalette.inputFieldSelectedBorderColor.color,
        inputElementActiveTintColor = lightColorPalette.inputElementActiveTintColor.color,
        inputElementDefaultTintColor = lightColorPalette.inputElementDefaultTintColor.color
    )

internal fun darkColorPalette(darkColorPalette: DarkColorPalette = DarkColorPalette()) = DojoColors(
    primary = darkColorPalette.inputElementActiveTintColor.color,
    onPrimary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = darkColorPalette.primarySurfaceBackgroundColor.color,
    onSurface = Color.White,
    secondarySurface = Color.White,
    onSecondarySurface = Color.Black,
    success = Color(0xFF88B484),
    error = darkColorPalette.errorTextColor.color,
    onError = Color.White,
    primaryLabelTextColor = darkColorPalette.primaryLabelTextColor.color,
    secondaryLabelTextColor = darkColorPalette.secondaryLabelTextColor.color,
    headerTintColor = darkColorPalette.headerTintColor.color,
    headerButtonTintColor = darkColorPalette.headerButtonTintColor.color,
    primarySurfaceBackgroundColor = darkColorPalette.primarySurfaceBackgroundColor.color,
    primaryCTAButtonActiveBackgroundColor = darkColorPalette.primaryCTAButtonActiveBackgroundColor.color,
    primaryCTAButtonActiveTextColor = darkColorPalette.primaryCTAButtonActiveTextColor.color,
    primaryCTAButtonDisabledBackgroundColor = darkColorPalette.primaryCTAButtonDisabledBackgroundColor.color,
    primaryCTAButtonDisableTextColor = darkColorPalette.primaryCTAButtonDisableTextColor.color,
    secondaryCTAButtonActiveBorderColor = darkColorPalette.secondaryCTAButtonActiveBorderColor.color,
    secondaryCTAButtonActiveTextColor = darkColorPalette.secondaryCTAButtonActiveTextColor.color,
    separatorColor = darkColorPalette.separatorColor.color,
    errorTextColor = darkColorPalette.errorTextColor.color,
    loadingIndicatorColor = darkColorPalette.loadingIndicatorColor.color,
    inputFieldPlaceholderColor = darkColorPalette.inputFieldPlaceholderColor.color,
    inputFieldBackgroundColor = darkColorPalette.inputFieldBackgroundColor.color,
    inputFieldDefaultBorderColor = darkColorPalette.inputFieldDefaultBorderColor.color,
    inputFieldSelectedBorderColor = darkColorPalette.inputFieldSelectedBorderColor.color,
    inputElementActiveTintColor = darkColorPalette.inputElementActiveTintColor.color,
    inputElementDefaultTintColor = darkColorPalette.inputElementDefaultTintColor.color
)

@Composable
fun DojoTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkColorPalette() else lightColorPalette()

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

@Suppress("LongParameterList")
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
    error: Color,
    onError: Color,
    primaryLabelTextColor: Color,
    secondaryLabelTextColor: Color,
    headerTintColor: Color,
    headerButtonTintColor: Color,
    primarySurfaceBackgroundColor: Color,
    primaryCTAButtonActiveBackgroundColor: Color,
    primaryCTAButtonActiveTextColor: Color,
    primaryCTAButtonDisabledBackgroundColor: Color,
    primaryCTAButtonDisableTextColor: Color,
    secondaryCTAButtonActiveBorderColor: Color,
    secondaryCTAButtonActiveTextColor: Color,
    separatorColor: Color,
    errorTextColor: Color,
    loadingIndicatorColor: Color,
    inputFieldPlaceholderColor: Color,
    inputFieldBackgroundColor: Color,
    inputFieldDefaultBorderColor: Color,
    inputFieldSelectedBorderColor: Color,
    inputElementActiveTintColor: Color,
    inputElementDefaultTintColor: Color
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
    var error by mutableStateOf(error)
        private set
    var onError by mutableStateOf(onError)
        private set
    var primaryLabelTextColor by mutableStateOf(primaryLabelTextColor)
        private set
    var secondaryLabelTextColor by mutableStateOf(secondaryLabelTextColor)
        private set
    var headerTintColor by mutableStateOf(headerTintColor)
        private set
    var headerButtonTintColor by mutableStateOf(headerButtonTintColor)
        private set
    var primarySurfaceBackgroundColor by mutableStateOf(primarySurfaceBackgroundColor)
        private set
    var primaryCTAButtonActiveBackgroundColor by mutableStateOf(
        primaryCTAButtonActiveBackgroundColor
    )
        private set
    var primaryCTAButtonActiveTextColor by mutableStateOf(primaryCTAButtonActiveTextColor)
        private set
    var primaryCTAButtonDisabledBackgroundColor by mutableStateOf(
        primaryCTAButtonDisabledBackgroundColor
    )
        private set

    var primaryCTAButtonDisableTextColor by mutableStateOf(primaryCTAButtonDisableTextColor)
        private set

    var secondaryCTAButtonActiveBorderColor by mutableStateOf(secondaryCTAButtonActiveBorderColor)
        private set
    var secondaryCTAButtonActiveTextColor by mutableStateOf(secondaryCTAButtonActiveTextColor)
        private set

    var separatorColor by mutableStateOf(separatorColor)
        private set
    var errorTextColor by mutableStateOf(errorTextColor)
        private set
    var loadingIndicatorColor by mutableStateOf(loadingIndicatorColor)
        private set
    var inputFieldPlaceholderColor by mutableStateOf(inputFieldPlaceholderColor)
        private set
    var inputFieldBackgroundColor by mutableStateOf(inputFieldBackgroundColor)
        private set

    var inputFieldDefaultBorderColor by mutableStateOf(inputFieldDefaultBorderColor)
        private set
    var inputFieldSelectedBorderColor by mutableStateOf(inputFieldSelectedBorderColor)
        private set
    var inputElementActiveTintColor by mutableStateOf(inputElementActiveTintColor)
        private set
    var inputElementDefaultTintColor by mutableStateOf(inputElementDefaultTintColor)
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
