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
import androidx.compose.ui.layout.positionInParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
internal fun Modifier.autoScrollableInputFieldOnFocusChangeAndValidator(
    coroutineScope: CoroutineScope,
    scrollState: ScrollState,
    initialHasBeenFocused: Boolean,
    parentPosition: Float,
    onValidate: () -> Unit,
): Modifier {
    var hasBeenFocused by remember { mutableStateOf(initialHasBeenFocused) }
    val scrollOffsets = remember { mutableListOf<Float>() }
    var inputFieldLabelHeightInPx by remember { mutableStateOf(0) }
    return this.onFocusChanged { focusState ->
        if (focusState.isFocused) {
            coroutineScope.launch {
                delay(300)
                val totalOffset =  scrollOffsets.maxOrNull() ?: 0F
                if (totalOffset != 0F) {
                    scrollState.animateScrollTo((parentPosition + totalOffset - inputFieldLabelHeightInPx).roundToInt())
                } else {
                    scrollState.animateScrollTo((totalOffset).roundToInt())
                }
            }
            hasBeenFocused = true
        } else {
            if (hasBeenFocused) {
                onValidate()
                scrollOffsets.clear()
            }
        }
    }.onGloballyPositioned { layoutCoordinates ->
        inputFieldLabelHeightInPx = layoutCoordinates.size.height
        val totalHeight = layoutCoordinates.positionInParent().y
        scrollOffsets.add(totalHeight)
    }
}
