package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
fun BasicCardInformationField(
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
                visualTransformation = { creditCardFilter(it) },
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

fun creditCardFilter(text: AnnotatedString): TransformedText {
    val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text

    val annotatedString = AnnotatedString.Builder().run {
        for (i in trimmed.indices) {
            append(trimmed[i])
            if (i % 4 == 3 && i != 15) {
                append(" ")
            }
        }
        pushStyle(SpanStyle(color = Color.LightGray))
        toAnnotatedString()
    }

    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 3) return offset
            if (offset <= 7) return offset + 1
            if (offset <= 11) return offset + 2
            if (offset <= 16) return offset + 3
            return 19
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 9) return offset - 1
            if (offset <= 14) return offset - 2
            if (offset <= 19) return offset - 3
            return 16
        }
    }

    return TransformedText(annotatedString, creditCardOffsetTranslator)
}


fun dateFilter(text: AnnotatedString): TransformedText {

    val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i % 2 == 1 && i <3) out += "/"
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 1) return offset
            if (offset <= 3) return offset + 1
            return 5
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 2) return offset
            if (offset <= 5) return offset - 1
            if (offset <= 10) return offset - 2
            return 4
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}

@Preview("App bar with two icons and title aligned to left", group = "AppBar")
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