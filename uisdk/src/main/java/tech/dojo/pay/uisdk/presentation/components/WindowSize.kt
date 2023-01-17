package tech.dojo.pay.uisdk.presentation.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator

data class WindowSize(
    val widthWindowType: WindowType,
    val heightWindowType: WindowType,
    val widthWindowDpSize: Dp,
    val heightWindowDpSize: Dp
) {
    sealed class WindowType {
        object COMPACT : WindowType()
        object MEDIUM : WindowType()
        object EXPANDED : WindowType()
    }
}

@Composable
fun Activity.rememberWindowSize(): WindowSize {
    val configuration = LocalConfiguration.current
    val windowMetrics = remember(configuration) {
        WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(activity = this)
    }
    val windowDpSize = with(LocalDensity.current) {
        windowMetrics.bounds.toComposeRect().size.toDpSize()
    }
    return WindowSize(
        widthWindowType = when {
            windowDpSize.width < 600.dp -> WindowSize.WindowType.COMPACT
            windowDpSize.width < 840.dp -> WindowSize.WindowType.MEDIUM
            else -> WindowSize.WindowType.EXPANDED
        },
        heightWindowType = when {
            windowDpSize.height < 480.dp -> WindowSize.WindowType.COMPACT
            windowDpSize.height < 600.dp -> WindowSize.WindowType.MEDIUM
            else -> WindowSize.WindowType.EXPANDED
        },
        widthWindowDpSize = windowDpSize.width,
        heightWindowDpSize = windowDpSize.height
    )
}
