package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InputFieldWithErrorMessage(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    label: AnnotatedString? = null,
    assistiveText: AnnotatedString? = null,
    focusRequester: FocusRequester? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        label = label,
        assistiveText = assistiveText,
        singleLine = true,
        enabled = enabled,
        isError = isError,
        focusRequester = focusRequester,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
    )
}

@Preview("PrefixInputField", group = "Input")
@Composable
fun PrefixInputFieldPreview() {
    var value: String by remember { mutableStateOf("") }
    var error:Boolean by remember {
        mutableStateOf(true)
    }
    DojoPreview() {
        InputFieldWithErrorMessage(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            value = value,
            onValueChange = {
                error= false
                value = it
            },
            placeholder = "Placeholder",
            label = buildAnnotatedString {
                append("Label")
                withStyle(SpanStyle(LocalContentColor.current.copy(alpha = ContentAlpha.medium))) {
                    append(" (Optional)")
                }
            },
            isError = error,
            assistiveText =if(error) AnnotatedString("error must not be Empty ") else AnnotatedString("")
        )
    }
}
