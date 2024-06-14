package tech.dojo.pay.uisdk.presentation.components.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import tech.dojo.pay.uisdk.R

private val DojoRoobert = FontFamily(
    Font(R.font.roobert_regular, FontWeight.Normal),
    Font(R.font.roobert_medium, FontWeight.Medium),
    Font(R.font.roobert_heavy, FontWeight.Black),
    Font(R.font.roobert_bold, FontWeight.Bold)
)

val DojoTypography = Typography(
    h1 = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 48.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 56.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 40.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 48.sp,
        letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 32.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 40.sp
    ),
    h4 = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp
    ),
    h5 = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    body1 = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    body2 = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    button = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    caption = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp
    ),
    overline = TextStyle(
        fontFamily = DojoRoobert,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        letterSpacing = 1.sp
    )
)

val TextStyle.medium: TextStyle
    get() = this.copy(fontWeight = FontWeight.Medium)

val TextStyle.bold: TextStyle
    get() = this.copy(fontWeight = FontWeight.Bold)
