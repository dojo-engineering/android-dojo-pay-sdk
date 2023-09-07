package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun InputFieldModifierWithFocusChangedAndScrollingLogic(
    coroutineScope: CoroutineScope,
    scrollState: ScrollState,
    initialHasBeenFocused: Boolean,
    onValidate: () -> Unit,
): Modifier {
    var hasBeenFocused by remember { mutableStateOf(initialHasBeenFocused) }
    val scrollOffsets = remember { mutableListOf<Float>() }
    val inputFieldLabelHeightInPx = with(LocalDensity.current) {
        INPUT_FIELD_LABEL_HEIGHT.dp.toPx()
    }
    return Modifier.onFocusChanged { focusState ->
        if (focusState.isFocused) {
            coroutineScope.launch {
                delay(300)
                val totalOffset = scrollOffsets.maxOrNull() ?: 0F
                if (totalOffset != 0F) {
                    scrollState.animateScrollTo((totalOffset - inputFieldLabelHeightInPx).roundToInt())
                } else {
                    scrollState.animateScrollTo((totalOffset).roundToInt())
                }
            }
            hasBeenFocused = true
        } else {
            if (hasBeenFocused) {
                onValidate()
            }
        }
    }.onGloballyPositioned { layoutCoordinates ->
        val totalHeight = layoutCoordinates.positionInRoot().y
        scrollOffsets.add(totalHeight)
    }
}

private const val INPUT_FIELD_LABEL_HEIGHT = 70
