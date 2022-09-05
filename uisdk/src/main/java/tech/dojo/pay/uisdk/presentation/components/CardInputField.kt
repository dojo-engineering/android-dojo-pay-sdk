package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CardInputField(
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
    label: AnnotatedString? = null,
    assistiveText: AnnotatedString? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    textHorizontalPadding: Dp = 16.dp,
    textVerticalPadding: Dp = 12.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    keyboardActions: KeyboardActions = KeyboardActions()
) {

    LabelAndAssistiveTextWrapper(
        modifier = modifier,
        label = label,
        assistiveText = assistiveText,
        isError = isError,
        enabled = enabled
    ) {
        BasicCardInputField(
            cardNumberValue,
            cvvValue,
            expireDateValue,
            cardNumberPlaceholder,
            cvvPlaceholder,
            expireDaterPlaceholder,
            onCardNumberValueChanged,
            onExpireDateValueChanged,
            onCvvValueChanged,
            modifier,
            focusRequester,
            isError,
            enabled,
            singleLine,
            maxLines,
            textHorizontalPadding,
            textVerticalPadding,
            keyboardOptions,
            keyboardActions
        )
    }
}


@Preview("LabelAndAssistiveTextWrapper", group = "Input")
@Composable
fun Text() {
    CardInputField(
        label =
        buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.15.sp
                ),
            ) {
                append(
                    "hi there top"
                )
            }
        },
        cardNumberPlaceholder = "card number",
        cardNumberValue ="",
        onCardNumberValueChanged = { },
        cvvPlaceholder = "cvv",
        cvvValue ="",
        onCvvValueChanged = { },
        expireDaterPlaceholder = "ex",
        expireDateValue = "",
        onExpireDateValueChanged = { }
    )













}
