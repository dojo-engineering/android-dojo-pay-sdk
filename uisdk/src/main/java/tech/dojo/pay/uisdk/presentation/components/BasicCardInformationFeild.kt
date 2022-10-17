package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
internal fun BasicCardInputField(
    cardNumberValue: String,
    cvvValue: String,
    expireDateValue: String,
    cardNumberPlaceholder: String?,
    cvvPlaceholder: String?,
    expireDaterPlaceholder: String?,
    onCardNumberValueChanged: (String) -> Unit,
    onExpireDateValueChanged: (String) -> Unit,
    onCvvValueChanged: (String) -> Unit,
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
    var cardNumberValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = cardNumberValue,
                selection = TextRange(cardNumberValue.length)
            )
        )
    }
    var cvvValueState by remember {
        mutableStateOf(TextFieldValue(text = cvvValue, selection = TextRange(cvvValue.length)))
    }
    var expireDateValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = expireDateValue,
                selection = TextRange(expireDateValue.length)
            )
        )
    }
    BasicCardInformationField(
        cardNumberValue = cardNumberValueState,
        cvvValue = cvvValueState,
        expireDateValue = expireDateValueState,
        cardNumberPlaceholder = cardNumberPlaceholder,
        cvvPlaceholder = cvvPlaceholder,
        expireDaterPlaceholder = expireDaterPlaceholder,
        onCvvValueChanged = {
            cvvValueState = it
            onCvvValueChanged(it.text)
        },
        onCardNumberValueChanged = {
            cardNumberValueState = it
            onCardNumberValueChanged(it.text)
        },
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
internal fun BasicCardInformationField(
    cardNumberValue: TextFieldValue,
    cvvValue: TextFieldValue,
    expireDateValue: TextFieldValue,
    onCardNumberValueChanged: (TextFieldValue) -> Unit,
    onExpireDateValueChanged: (TextFieldValue) -> Unit,
    onCvvValueChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    cardNumberPlaceholder: String?,
    expireDaterPlaceholder: String?,
    cvvPlaceholder: String?,
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
    val colors = TextFieldDefaults.outlinedTextFieldColors()
    val maxCardNumberChar = 16
    val expireDataCharLength = 4
    val maxCvvChar = 3

    Column(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .height(IntrinsicSize.Min)
            .border(
                width = 1.dp,
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
                visualTransformation = { formatNormalCard(it) },
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

        Divider(
            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
            thickness = 1.dp
        )
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = textHorizontalPadding, vertical = textVerticalPadding)
                    .fillMaxHeight()
                    .weight(1f)
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

            Divider(
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = textHorizontalPadding, vertical = textVerticalPadding)
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                if (cvvValue.text.isEmpty() && !cvvPlaceholder.isNullOrEmpty()) {
                    Text(
                        text = cvvPlaceholder,
                        style = DojoTheme.typography.subtitle1,
                        color = colors.placeholderColor(enabled).value
                    )
                }

                BasicTextField(
                    value = cvvValue,
                    onValueChange = {
                        if (it.text.length < maxCvvChar || it.text.length == maxCvvChar) onCvvValueChanged(
                            it
                        )
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
}

@Preview("card Details Group", group = "CardDetails")
@Composable
internal fun PreviewBasicCardInformationField() = DojoPreview {
    val value by remember { mutableStateOf("") }
    var cardNumberValueState by remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    var cvvValueState by remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    var expireDateValueState by remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    BasicCardInformationField(
        cardNumberValue = cardNumberValueState,
        cvvValue = cvvValueState,
        expireDateValue = expireDateValueState,
        onCvvValueChanged = { cvvValueState = it },
        onCardNumberValueChanged = { cardNumberValueState = it },
        onExpireDateValueChanged = { expireDateValueState = it },
        cardNumberPlaceholder = "1234  5678  1234  5678",
        cvvPlaceholder = "cvv",
        expireDaterPlaceholder = "MM/YY"
    )
}
