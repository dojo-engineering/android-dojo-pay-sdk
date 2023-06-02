package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.CardExpireDateInputField
import tech.dojo.pay.uisdk.presentation.components.CardNumberInPutField
import tech.dojo.pay.uisdk.presentation.components.CvvInputField
import tech.dojo.pay.uisdk.presentation.components.InputFieldWithErrorMessage
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.medium
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CardDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.VirtualTerminalViewModel

@Composable
internal fun CardDetailsSection(
    viewModel: VirtualTerminalViewModel,
    isDarkModeEnabled: Boolean
) {
    val state = viewModel.state.observeAsState().value ?: return
    if (state.cardDetailsSection?.isVisible == true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.background(DojoTheme.colors.primarySurfaceBackgroundColor)
        ) {
            HeaderTitle()
            CardHolderInputField(state.cardDetailsSection, viewModel)
            CardNumberInputField(state.cardDetailsSection, viewModel, isDarkModeEnabled)
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .heightIn(48.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    CardExpireDateField(state.cardDetailsSection, viewModel)
                    Divider(modifier = Modifier.width(32.dp))
                    CvvField(state.cardDetailsSection, viewModel)
                }
            }
            EmailInputField(state.cardDetailsSection, viewModel)
        }
    }
}

@Composable
private fun HeaderTitle() {
    Text(
        text = "Payment Details",
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = DojoTheme.typography.h1.medium,
        color = DojoTheme.colors.primaryLabelTextColor.copy(alpha = ContentAlpha.high)
    )
}

@Composable
private fun CardHolderInputField(
    cardDetailsViewState: CardDetailsViewState,
    viewModel: VirtualTerminalViewModel
) {
    var isTextNotFocused by remember { mutableStateOf(false) }
    InputFieldWithErrorMessage(
        modifier = Modifier.onFocusChanged {
            isTextNotFocused = if (it.isFocused) {
                true
            } else {
                if (isTextNotFocused) {
                    viewModel.onValidateCardHolder(cardDetailsViewState.cardHolderInputField.value)
                }
                false
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        value = cardDetailsViewState.cardHolderInputField.value,
        isError = cardDetailsViewState.cardHolderInputField.isError,
        assistiveText = cardDetailsViewState.cardHolderInputField.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        onValueChange = { viewModel.onCardHolderChanged(it) },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_card_name)) }
    )
}

@Composable
private fun CardNumberInputField(
    cardDetailsViewState: CardDetailsViewState,
    viewModel: VirtualTerminalViewModel,
    isDarkModeEnabled: Boolean
) {
    var isTextNotFocused by remember { mutableStateOf(false) }

    CardNumberInPutField(
        modifier = Modifier.onFocusChanged {
            isTextNotFocused = if (it.isFocused) {
                true
            } else {
                if (isTextNotFocused) {
                    viewModel.onValidateCardNumber(cardDetailsViewState.cardNumberInputField.value)
                }
                false
            }
        },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_pan)) },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        isError = cardDetailsViewState.cardNumberInputField.isError,
        assistiveText = cardDetailsViewState.cardNumberInputField.errorMessages?.let {
            AnnotatedString(
                stringResource(id = it)
            )
        },
        cardNumberValue = cardDetailsViewState.cardNumberInputField.value,
        cardNumberPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_pan),
        onCardNumberValueChanged = { viewModel.onCardNumberChanged(it) },
        isDarkModeEnabled = isDarkModeEnabled
    )
}

@Composable
private fun CardExpireDateField(
    cardDetailsViewState: CardDetailsViewState,
    viewModel: VirtualTerminalViewModel
) {
    var isTextNotFocused by remember { mutableStateOf(false) }

    CardExpireDateInputField(
        modifier = Modifier.onFocusChanged {
            isTextNotFocused = if (it.isFocused) {
                true
            } else {
                if (isTextNotFocused) {
                    viewModel.onValidateCardDate(cardDetailsViewState.cardExpireDateInputField.value)
                }
                false
            }
        },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_expiry_date)) },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        isError = cardDetailsViewState.cardExpireDateInputField.isError,
        assistiveText = cardDetailsViewState.cardExpireDateInputField.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        expireDateValue = cardDetailsViewState.cardExpireDateInputField.value,
        expireDaterPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_expiry),
        onExpireDateValueChanged = { viewModel.onCardDateChanged(it) }
    )
}

@Composable
private fun CvvField(
    cardDetailsViewState: CardDetailsViewState,
    viewModel: VirtualTerminalViewModel
) {
    var isTextNotFocused by remember { mutableStateOf(false) }

    CvvInputField(
        modifier = Modifier.onFocusChanged {
            isTextNotFocused = if (it.isFocused) {
                true
            } else {
                if (isTextNotFocused) {
                    viewModel.onValidateCvv(cardDetailsViewState.cvvInputFieldState.value)
                }
                false
            }
        },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv)) },
        cvvValue = cardDetailsViewState.cvvInputFieldState.value,
        isError = cardDetailsViewState.cvvInputFieldState.isError,
        assistiveText = cardDetailsViewState.cvvInputFieldState.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        cvvPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv),
        onCvvValueChanged = { viewModel.onCardCvvChanged(it) }
    )
}

@Composable
private fun EmailInputField(
    cardDetailsViewState: CardDetailsViewState,
    viewModel: VirtualTerminalViewModel
) {
    var isTextNotFocused by remember { mutableStateOf(false) }
    InputFieldWithErrorMessage(
        modifier = Modifier.onFocusChanged {
            isTextNotFocused = if (it.isFocused) {
                true
            } else {
                if (isTextNotFocused) {
                    viewModel.onValidateEmail(cardDetailsViewState.emailInputField.value)
                }
                false
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        value = cardDetailsViewState.emailInputField.value,
        isError = cardDetailsViewState.emailInputField.isError,
        assistiveText = cardDetailsViewState.emailInputField.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        onValueChange = { viewModel.onEmailChanged(it) },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_email)) }
    )
}
