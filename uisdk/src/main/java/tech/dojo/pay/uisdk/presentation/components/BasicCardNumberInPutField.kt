package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
internal fun BasicCardNumberInPutField(
    cardNumberValue: String,
    cardNumberPlaceholder: String?,
    onCardNumberValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    textHorizontalPadding: Dp = 16.dp,
    textVerticalPadding: Dp = 12.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    var cardNumberValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = cardNumberValue,
                selection = TextRange(cardNumberValue.length)
            )
        )
    }

    BasicCardNumberInputField(
        cardNumberValue = cardNumberValueState,
        cardNumberPlaceholder = cardNumberPlaceholder,
        onCardNumberValueChanged = {
            cardNumberValueState = it
            onCardNumberValueChanged(it.text)
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
internal fun BasicCardNumberInputField(
    cardNumberValue: TextFieldValue,
    cardNumberPlaceholder: String?,
    onCardNumberValueChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    textHorizontalPadding: Dp = 16.dp,
    textVerticalPadding: Dp = 12.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = TextFieldDefaults.outlinedTextFieldColors()
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val maxCardNumberChar = when (isAmexCardScheme(cardNumberValue.text)) {
        true -> 15
        else -> 16
    }


    Column(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .height(IntrinsicSize.Min)
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = colors.indicatorColor(enabled, isError, interactionSource).value,
                shape = DojoTheme.shapes.small
            )
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = textHorizontalPadding, vertical = textVerticalPadding)
                .align(Alignment.CenterHorizontally)
        ) {
            if (cardNumberValue.text.isEmpty() && !cardNumberPlaceholder.isNullOrEmpty()) {
                Text(
                    text = cardNumberPlaceholder,
                    style = DojoTheme.typography.subtitle1,
                    color = colors.placeholderColor(enabled).value
                )
            }

            BasicTextField(
                value = cardNumberValue,
                onValueChange = {
                    if (it.text.length < maxCardNumberChar || it.text.length == maxCardNumberChar) onCardNumberValueChanged(
                        it
                    )
                },
                visualTransformation = {
                    when (isAmexCardScheme(cardNumberValue.text)) {
                        true -> formatAmex(it)
                        else -> formatNormalCard(it)
                    }
                },
                textStyle = DojoTheme.typography.subtitle1.copy(color = colors.textColor(enabled).value),
                maxLines = maxLines,
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
internal fun PreviewBasicCardNumber() = DojoPreview {
    val value by remember { mutableStateOf("") }
    var cardNumberValueState by remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    BasicCardNumberInputField(
        cardNumberValue = cardNumberValueState,
        onCardNumberValueChanged = { cardNumberValueState = it },
        cardNumberPlaceholder = "1234  5678  1234  5678"
    )
}