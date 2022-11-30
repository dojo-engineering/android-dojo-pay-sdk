package tech.dojo.pay.uisdk.entities

import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import java.io.Serializable

data class DojoThemeSettings(
    val lightColorPalette: LightColorPalette = LightColorPalette(),
    val DarkColorPalette: DarkColorPalette = DarkColorPalette()
) : Serializable

data class LightColorPalette(
    @ColorRes
    val primaryLabelTextColor: String = "#FF000000",
    @ColorRes
    val secondaryLabelTextColor: String = "#99000000",
    @ColorRes
    val headerTintColor: String = "#FF000000",
    @ColorRes
    val headerButtonTintColor: String = "#FF000000",
    @ColorRes
    val primarySurfaceBackgroundColor: String = "#FFFFFFFF",
    @ColorRes
    val primaryCTAButtonActiveBackgroundColor: String = "#FF000000",
    @ColorRes
    val primaryCTAButtonActiveTextColor: String = "#FFFFFFFF",
    @ColorRes
    val primaryCTAButtonDisabledBackgroundColor: String = "#FFE5E5E5",
    @ColorRes
    val primaryCTAButtonDisableTextColor: String = "#61000000",
    @ColorRes
    val secondaryCTAButtonActiveBorderColor: String = "#FF000000",
    @ColorRes
    val secondaryCTAButtonActiveTextColor: String = "#FF000000",
    @ColorRes
    val separatorColor: String = "#33000000",
    @ColorRes
    val errorTextColor: String = "#FFB00020",
    @ColorRes
    val loadingIndicatorColor: String = "#FF000000",
    @ColorRes
    val inputFieldPlaceholderColor: String = "#FF000000",
    @ColorRes
    val inputFieldBackgroundColor: String = "#FFF5F5F5",
    @ColorRes
    val inputFieldDefaultBorderColor: String = "#FF000000",
    @ColorRes
    val inputFieldSelectedBorderColor: String = "#FF00857D",
    @ColorRes
    val inputElementActiveTintColor: String = "#FF00857D",
    @ColorRes
    val inputElementDefaultTintColor: String = "#FF262626"
) : Serializable

data class DarkColorPalette(
    @ColorRes
    val primaryLabelTextColor: String = "#FFFFFFFF",
    @ColorRes
    val secondaryLabelTextColor: String = "#FFFFFFFF",
    @ColorRes
    val headerTintColor: String = "#FFFFFFFF",
    @ColorRes
    val headerButtonTintColor: String = "#FFFFFFFF",
    @ColorRes
    val primarySurfaceBackgroundColor: String = "#FF1e1e1e",
    @ColorRes
    val primaryCTAButtonActiveBackgroundColor: String = "#FFFFFFFF",
    @ColorRes
    val primaryCTAButtonActiveTextColor: String = "#FF000000",
    @ColorRes
    val primaryCTAButtonDisabledBackgroundColor: String = "#FFE5E5E5",
    @ColorRes
    val primaryCTAButtonDisableTextColor: String = "#FFE5E5E5",
    @ColorRes
    val secondaryCTAButtonActiveBorderColor: String = "#FFFFFFFF",
    @ColorRes
    val secondaryCTAButtonActiveTextColor: String = "#FFFFFFFF",
    @ColorRes
    val separatorColor: String = "#33000000",
    @ColorRes
    val errorTextColor: String = "#FFCF6679",
    @ColorRes
    val loadingIndicatorColor: String = "#FFFFFFFF",
    @ColorRes
    val inputFieldPlaceholderColor: String = "#FFFFFFFF",
    @ColorRes
    val inputFieldBackgroundColor: String = "#FFF5F5F5",
    @ColorRes
    val inputFieldDefaultBorderColor: String = "#FFFFFFFF",
    @ColorRes
    val inputFieldSelectedBorderColor: String = "#FF00857D",
    @ColorRes
    val inputElementActiveTintColor: String = "#FF00857D",
    @ColorRes
    val inputElementDefaultTintColor: String = "#FFFFFFFF"
) : Serializable


internal val String.color
    get() = Color(android.graphics.Color.parseColor(this))
