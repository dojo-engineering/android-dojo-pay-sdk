package tech.dojo.pay.uisdk.entities

import androidx.compose.ui.graphics.Color
import java.io.Serializable

data class DojoThemeSettings(
    val headerButtonTintColor: String?= null,
    val headerTintColor: String?= null,
    val primaryLabelTextColor:String?=null,
    val secondaryLabelTextColor:String?= null,
    val primaryCTAButtonActiveBackgroundColor:String?=null
): Serializable

val String.color
    get() = Color(android.graphics.Color.parseColor(this))