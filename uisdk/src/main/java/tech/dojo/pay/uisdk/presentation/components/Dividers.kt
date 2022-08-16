package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
internal fun HorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp
) {
    Box(
        modifier
            .padding(start = paddingStart, end = paddingEnd)
            .fillMaxWidth()
            .height(thickness)
            .background(color = color)
    )
}

@Composable
internal fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = DojoTheme.colors.onBackground.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp
) {
    Box(
        modifier
            .padding(top = paddingTop, bottom = paddingBottom)
            .fillMaxHeight()
            .width(thickness)
            .background(color = color)
    )
}

private const val DividerAlpha = 0.12f
