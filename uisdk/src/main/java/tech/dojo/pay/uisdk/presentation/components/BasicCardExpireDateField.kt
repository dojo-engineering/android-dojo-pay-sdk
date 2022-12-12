package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
internal fun BasicCardExpireDateField(
    expireDateValue: String,
    expireDaterPlaceholder: String?,
    onExpireDateValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    textHorizontalPadding: Dp = 16.dp,
    textVerticalPadding: Dp = 12.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    var expireDateValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = expireDateValue,
                selection = TextRange(expireDateValue.length)
            )
        )
    }
    BasicCardExpireDateField(
        expireDateValue = expireDateValueState,
        expireDaterPlaceholder = expireDaterPlaceholder,
        onExpireDateValueChanged = {
            expireDateValueState = it
            onExpireDateValueChanged(it.text)
        },
        modifier = modifier,
        focusRequester = focusRequester,
        isError = isError,
        enabled = enabled,
        singleLine = singleLine,
        maxLines = maxLines,
        textHorizontalPadding = textHorizontalPadding,
        textVerticalPadding = textVerticalPadding,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
internal fun BasicCardExpireDateField(
    expireDateValue: TextFieldValue,
    onExpireDateValueChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    expireDaterPlaceholder: String?,
    focusRequester: FocusRequester? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    textHorizontalPadding: Dp = 16.dp,
    textVerticalPadding: Dp = 12.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = DojoTheme.colors.primaryLabelTextColor,
        cursorColor= DojoTheme.colors.primaryLabelTextColor,
        unfocusedBorderColor = DojoTheme.colors.inputFieldDefaultBorderColor,
        backgroundColor = DojoTheme.colors.inputFieldBackgroundColor,
        focusedBorderColor = DojoTheme.colors.inputFieldSelectedBorderColor,
        placeholderColor = DojoTheme.colors.inputFieldPlaceholderColor,
        errorBorderColor = DojoTheme.colors.errorTextColor,
        errorCursorColor = DojoTheme.colors.errorTextColor,
        errorLabelColor = DojoTheme.colors.errorTextColor,
        errorLeadingIconColor = DojoTheme.colors.errorTextColor,
        errorTrailingIconColor = DojoTheme.colors.errorTextColor
    )
    val expireDataCharLength = 4
    Column(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(DojoTheme.colors.inputFieldBackgroundColor)
            .border(
                width = 1.dp,
                color = colors.indicatorColor(enabled, isError, interactionSource).value,
                shape = DojoTheme.shapes.small
            )
            .background(DojoTheme.colors.inputFieldBackgroundColor)

    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = textHorizontalPadding, vertical = textVerticalPadding)
                .fillMaxHeight()
        ) {
            if (expireDateValue.text.isEmpty() && !expireDaterPlaceholder.isNullOrEmpty()) {
                Text(
                    text = expireDaterPlaceholder,
                    style = DojoTheme.typography.subtitle1,
                    color = colors.placeholderColor(enabled).value
                )
            }

            BasicTextField(
                value = expireDateValue,
                onValueChange = {
                    if (it.text.length < expireDataCharLength || it.text.length == expireDataCharLength) onExpireDateValueChanged(
                        it
                    )
                },
                textStyle = DojoTheme.typography.subtitle1.copy(color = colors.textColor(enabled).value),
                maxLines = maxLines,
                visualTransformation = { dateFilter(it) },
                enabled = enabled,
                singleLine = singleLine,
                interactionSource = interactionSource,
                cursorBrush = SolidColor(colors.cursorColor(isError).value),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                modifier = Modifier
                    .fillMaxWidth()
                    .run { if (focusRequester != null) focusRequester(focusRequester) else this }
            )
        }
    }
}

@Preview("card Details Group", group = "CardDetails")
@Composable
internal fun PreviewBasicCardExpireDate() = DojoPreview {
    val value by remember { mutableStateOf("") }

    var expireDateValueState by remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    BasicCardExpireDateField(
        expireDateValue = expireDateValueState,
        onExpireDateValueChanged = { expireDateValueState = it },
        expireDaterPlaceholder = "MM/YY"
    )
}
