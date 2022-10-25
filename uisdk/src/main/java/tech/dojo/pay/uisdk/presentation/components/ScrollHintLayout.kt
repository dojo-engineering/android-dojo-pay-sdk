package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
fun ScrollHintLayout(
    modifier: Modifier,
    scrollState: ScrollState,
    content: @Composable () -> Unit
) {
    // If scrollable and can scroll down
    val showScrollHintShadow = scrollState.maxValue != 0 && scrollState.maxValue != scrollState.value

    Column(modifier) {
        if (showScrollHintShadow) {
            Box(
                Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                DojoTheme.colors.background.copy(alpha = 0F),
                                DojoTheme.colors.onBackground.copy(alpha = 0.06F)
                            )
                        )
                    )
            )
        }
        content()
    }
}
