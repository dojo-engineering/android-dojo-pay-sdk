package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    label: AnnotatedString? = null,
    assistiveText: AnnotatedString? = null,
    focusRequester: FocusRequester? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    leading: @Composable (RowScope.() -> Unit)? = null,
    trailing: @Composable (RowScope.() -> Unit)? = null,
    textHorizontalPadding: Dp = 16.dp,
    textVerticalPadding: Dp = 12.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    LabelAndAssistiveTextWrapper(
        modifier = modifier,
        label = label,
        assistiveText = assistiveText,
        isError = isError,
        enabled = enabled
    ) {
        BasicInputField(
            value,
            onValueChange,
            Modifier,
            placeholder,
            focusRequester,
            isError,
            enabled,
            singleLine,
            maxLines,
            leading,
            trailing,
            textHorizontalPadding,
            textVerticalPadding,
            keyboardOptions,
            keyboardActions
        )
    }
}
