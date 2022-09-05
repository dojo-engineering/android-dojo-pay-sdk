package tech.dojo.pay.uisdk.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.DojoPreview
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
fun BasicCardInformationField(
    cardNumberValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = TextFieldDefaults.outlinedTextFieldColors()

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
                onValueChange = { onValueChange(it) },
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
                if (cardNumberValue.text.isEmpty() && !expireDaterPlaceholder.isNullOrEmpty()) {
                    Text(
                        text = expireDaterPlaceholder,
                        style = DojoTheme.typography.subtitle1,
                        color = colors.placeholderColor(enabled).value
                    )
                }

                BasicTextField(
                    value = cardNumberValue,
                    onValueChange = {
                        onValueChange(it)
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
                if (cardNumberValue.text.isEmpty() && !cvvPlaceholder.isNullOrEmpty()) {
                    Text(
                        text = cvvPlaceholder,
                        style = DojoTheme.typography.subtitle1,
                        color = colors.placeholderColor(enabled).value
                    )
                }

                BasicTextField(
                    value = cardNumberValue,
                    onValueChange = {
                        onValueChange(it)
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

@Preview("DojoBrandFooter", group = "Footer")
@Composable
internal fun PreviewDojoBrandFooter() = DojoPreview {
    var textFieldValueState by remember {
        mutableStateOf(TextFieldValue(text = "", selection = TextRange("".length)))
    }
    BasicCardInformationField(
        cardNumberValue = textFieldValueState,
        onValueChange = { textFieldValueState = it },
        cardNumberPlaceholder = "1234 1234 1234 1234",
        cvvPlaceholder = "cvv",
        expireDaterPlaceholder = "MM/YY"

    )
}