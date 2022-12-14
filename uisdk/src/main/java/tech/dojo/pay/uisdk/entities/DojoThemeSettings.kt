package tech.dojo.pay.uisdk.entities

import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import java.io.Serializable

data class DojoThemeSettings(
    val lightColorPalette: LightColorPalette = LightColorPalette(),
    val DarkColorPalette: DarkColorPalette = DarkColorPalette(),
    val forceLightMode: Boolean = false
) : Serializable

data class LightColorPalette(
    @ColorRes
    val primaryLabelTextColor: String = "#DD000000",
    @ColorRes
    val secondaryLabelTextColor: String = "#99000000",
    @ColorRes
    val headerTintColor: String = "#DD000000",
    @ColorRes
    val headerButtonTintColor: String = "#99000000",
    @ColorRes
    val primarySurfaceBackgroundColor: String = "#FFFFFFFF",
    @ColorRes
    val primaryCTAButtonActiveBackgroundColor: String = "#FF000000",
    @ColorRes
    val primaryCTAButtonActiveTextColor: String = "#FFFFFFFF",
    @ColorRes
    val primaryCTAButtonDisabledBackgroundColor: String = "#FFE5E5E5",
    @ColorRes
    val primaryCTAButtonDisableTextColor: String = "#60000000",
    @ColorRes
    val secondaryCTAButtonActiveBorderColor: String = "#FF262626",
    @ColorRes
    val secondaryCTAButtonActiveTextColor: String = "#FF262626",
    @ColorRes
    val separatorColor: String = "#33000000",
    @ColorRes
    val errorTextColor: String = "#FFB00020",
    @ColorRes
    val loadingIndicatorColor: String = "#FF262626",
    @ColorRes
    val inputFieldPlaceholderColor: String = "#FF000000",
    @ColorRes
    val inputFieldBackgroundColor: String = "#FFFFFFFF",
    @ColorRes
    val inputFieldDefaultBorderColor: String = "#26000000",
    @ColorRes
    val inputFieldSelectedBorderColor: String = "#FF00857D",
    @ColorRes
    val inputElementActiveTintColor: String = "#FF00857D",
    @ColorRes
    val inputElementDefaultTintColor: String = "#26000000"
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
    val primarySurfaceBackgroundColor: String = "#FF1B1B1B",
    @ColorRes
    val primaryCTAButtonActiveBackgroundColor: String = "#FFFFFFFF",
    @ColorRes
    val primaryCTAButtonActiveTextColor: String = "#DD000000",
    @ColorRes
    val primaryCTAButtonDisabledBackgroundColor: String = "#FFC3C3C3",
    @ColorRes
    val primaryCTAButtonDisableTextColor: String = "#60000000",
    @ColorRes
    val secondaryCTAButtonActiveBorderColor: String = "#FFFFFFFF",
    @ColorRes
    val secondaryCTAButtonActiveTextColor: String = "#FFFFFFFF",
    @ColorRes
    val separatorColor: String = "#33000000",
    @ColorRes
    val errorTextColor: String = "#FFED5645",
    @ColorRes
    val loadingIndicatorColor: String = "#FFFFFFFF",
    @ColorRes
    val inputFieldPlaceholderColor: String = "#FFFFFFFF",
    @ColorRes
    val inputFieldBackgroundColor: String = "#FF313131",
    @ColorRes
    val inputFieldDefaultBorderColor: String = "#26FFFFFF",
    @ColorRes
    val inputFieldSelectedBorderColor: String = "#FFFFFFFF",
    @ColorRes
    val inputElementActiveTintColor: String = "#FFFFFFFF",
    @ColorRes
    val inputElementDefaultTintColor: String = "#FFFFFFFF"
) : Serializable

internal val String.color
    get() = Color(android.graphics.Color.parseColor(this))
