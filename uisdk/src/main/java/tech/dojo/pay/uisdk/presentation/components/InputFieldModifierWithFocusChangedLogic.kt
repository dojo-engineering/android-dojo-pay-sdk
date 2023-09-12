package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun InputFieldModifierWithFocusChangedLogic(
    coroutineScope: CoroutineScope,
    scrollState: ScrollState,
    scrollToPosition: Float,
    scrollOffset: Float,
    initialHasBeenFocused: Boolean,
    onValidate: () -> Unit,
): Modifier {
    var hasBeenFocused by remember { mutableStateOf(initialHasBeenFocused) }
    return Modifier.onFocusChanged { focusState ->
        if (focusState.isFocused) {
            coroutineScope.launch {
                delay(300)
                scrollState.animateScrollTo(
                    (scrollToPosition + scrollOffset).roundToInt(),
                )
            }
            hasBeenFocused = true
        } else {
            if (hasBeenFocused) {
                onValidate()
            }
        }
    }
}