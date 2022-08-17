package tech.dojo.pay.uisdk.entities

import androidx.compose.ui.graphics.Color
import java.io.Serializable

data class DojoThemeSettings(
    val headerButtonTintColor: Color?= null,
    val headerTintColor: Color?= null,
    val primaryLabelTextColor:Color?=null,
    val secondaryLabelTextColor:Color?= null,
    val primaryCTAButtonActiveBackgroundColor:Color?=null
): Serializable
