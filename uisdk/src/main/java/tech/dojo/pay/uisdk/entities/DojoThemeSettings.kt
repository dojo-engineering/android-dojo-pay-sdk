package tech.dojo.pay.uisdk.entities

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color
import java.io.Serializable

@Keep
data class DojoThemeSettings @JvmOverloads constructor(
    val lightColorPalette: LightColorPalette = LightColorPalette(),
    val darkColorPalette: DarkColorPalette = DarkColorPalette(),
    val forceLightMode: Boolean = false,
    val showBranding: Boolean = true,
    val analyticsExcludedFieldsIdentifier: String = "",
    var additionalLegalText: String = ""
) : Serializable

@Keep
data class LightColorPalette @JvmOverloads constructor(
    val primaryLabelTextColor: String = "#DD000000",
    val secondaryLabelTextColor: String = "#99000000",
    val headerTintColor: String = "#DD000000",
    val headerButtonTintColor: String = "#99000000",
    val primarySurfaceBackgroundColor: String = "#FFFFFFFF",
    val primaryCTAButtonActiveBackgroundColor: String = "#FF000000",
    val primaryCTAButtonActiveTextColor: String = "#FFFFFFFF",
    val primaryCTAButtonDisabledBackgroundColor: String = "#FFE5E5E5",
    val primaryCTAButtonDisableTextColor: String = "#60000000",
    val secondaryCTAButtonActiveBorderColor: String = "#FF262626",
    val secondaryCTAButtonActiveTextColor: String = "#FF262626",
    val separatorColor: String = "#33000000",
    val errorTextColor: String = "#FFB00020",
    val loadingIndicatorColor: String = "#FF262626",
    val inputFieldPlaceholderColor: String = "#61000000",
    val inputFieldBackgroundColor: String = "#FFFFFFFF",
    val inputFieldDefaultBorderColor: String = "#26000000",
    val inputFieldSelectedBorderColor: String = "#FF00857D",
    val inputElementActiveTintColor: String = "#FF00857D",
    val inputElementDefaultTintColor: String = "#26000000",
    val backdropViewColor: String = "#FF000000",
    val backdropViewAlpha: Float = 0.3f,
) : Serializable

@Keep
data class DarkColorPalette @JvmOverloads constructor(
    val primaryLabelTextColor: String = "#FFFFFFFF",
    val secondaryLabelTextColor: String = "#FFFFFFFF",
    val headerTintColor: String = "#FFFFFFFF",
    val headerButtonTintColor: String = "#FFFFFFFF",
    val primarySurfaceBackgroundColor: String = "#FF1B1B1B",
    val primaryCTAButtonActiveBackgroundColor: String = "#FFFFFFFF",
    val primaryCTAButtonActiveTextColor: String = "#DD000000",
    val primaryCTAButtonDisabledBackgroundColor: String = "#FFC3C3C3",
    val primaryCTAButtonDisableTextColor: String = "#60000000",
    val secondaryCTAButtonActiveBorderColor: String = "#FFFFFFFF",
    val secondaryCTAButtonActiveTextColor: String = "#FFFFFFFF",
    val separatorColor: String = "#33000000",
    val errorTextColor: String = "#FFED5645",
    val loadingIndicatorColor: String = "#FFFFFFFF",
    val inputFieldPlaceholderColor: String = "#99FFFFFF",
    val inputFieldBackgroundColor: String = "#FF313131",
    val inputFieldDefaultBorderColor: String = "#26FFFFFF",
    val inputFieldSelectedBorderColor: String = "#FFFFFFFF",
    val inputElementActiveTintColor: String = "#FFFFFFFF",
    val inputElementDefaultTintColor: String = "#FFFFFFFF",
    val backdropViewColor: String = "#FF000000",
    val backdropViewAlpha: Float = 0.3f,
) : Serializable

internal val String.color
    get() = Color(android.graphics.Color.parseColor(this))
