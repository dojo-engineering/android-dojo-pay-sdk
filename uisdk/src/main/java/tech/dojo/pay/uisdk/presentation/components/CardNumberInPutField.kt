package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.dojo.pay.sdk.card.entities.CardsSchemes

@Composable
internal fun CardNumberInPutField(
    cardNumberValue: String,
    cardNumberPlaceholder: String?,
    onCardNumberValueChanged: (String) -> Unit,
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
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions(),
    isDarkModeEnabled: Boolean,
    supportedCardSchemas: List<CardsSchemes>,
    inputTestTag: String? = null,
) {
    LabelAndAssistiveTextWrapper(
        modifier = modifier,
        label = label,
        assistiveText = assistiveText,
        isError = isError,
        enabled = enabled
    ) {
        BasicCardNumberInPutField(
            cardNumberValue,
            cardNumberPlaceholder,
            onCardNumberValueChanged,
            modifier,
            focusRequester,
            isError,
            enabled,
            singleLine,
            maxLines,
            textHorizontalPadding,
            textVerticalPadding,
            keyboardOptions,
            keyboardActions,
            isDarkModeEnabled,
            supportedCardSchemas,
            inputTestTag,
        )
    }
}
