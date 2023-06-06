package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.CardExpireDateInputField
import tech.dojo.pay.uisdk.presentation.components.CardNumberInPutField
import tech.dojo.pay.uisdk.presentation.components.CvvInputField
import tech.dojo.pay.uisdk.presentation.components.InputFieldWithErrorMessage
import tech.dojo.pay.uisdk.presentation.components.SupportedPaymentMethods
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.medium
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CardDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.VirtualTerminalViewModel
import kotlin.math.roundToInt

@Composable
internal fun CardDetailsSection(
    viewModel: VirtualTerminalViewModel,
    isDarkModeEnabled: Boolean,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState
) {
    val state = viewModel.state.observeAsState().value ?: return
    if (state.cardDetailsSection?.isVisible == true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.background(DojoTheme.colors.primarySurfaceBackgroundColor)
        ) {
            HeaderTitle()
            SupportedPaymentMethods(
                Modifier.padding(top = 0.dp),
                state.cardDetailsSection.allowedPaymentMethodsIcons,
                stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_transactions_are_secure)
            )
            CardHolderInputField(
                state.cardDetailsSection,
                viewModel,
                coroutineScope,
                scrollToPosition,
                scrollState
            )
            CardNumberInputField(
                state.cardDetailsSection,
                viewModel,
                isDarkModeEnabled,
                coroutineScope,
                scrollToPosition,
                scrollState
            )
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .heightIn(48.dp)
                    .padding(top = 16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CardExpireDateField(
                        state.cardDetailsSection,
                        viewModel,
                        coroutineScope,
                        scrollToPosition,
                        scrollState
                    )
                }
                Divider(modifier = Modifier.width(32.dp))
                Box(modifier = Modifier.weight(1f)) {
                    CvvField(
                        state.cardDetailsSection,
                        viewModel,
                        coroutineScope,
                        scrollToPosition,
                        scrollState
                    )
                }
            }
            EmailInputField(
                state.cardDetailsSection,
                viewModel,
                coroutineScope,
                scrollToPosition,
                scrollState
            )
        }
    }
}

@Composable
private fun HeaderTitle() {
    Text(
        text = "Payment Details",
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = DojoTheme.typography.h6.medium,
        color = DojoTheme.colors.primaryLabelTextColor.copy(alpha = ContentAlpha.high)
    )
}

@Composable
private fun CardHolderInputField(
    cardDetailsViewState: CardDetailsViewState,
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState
) {
    var isTextNotFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        cardDetailsViewState.itemPoissonOffset.dp.toPx() + NORMAL_FILED_SIZE_DP.dp.toPx()
    }
    InputFieldWithErrorMessage(
        modifier = Modifier.onFocusChanged {
            isTextNotFocused = if (it.isFocused) {
                coroutineScope.launch {
                    delay(300)
                    scrollState.animateScrollTo(
                        scrollToPosition.roundToInt() + scrollOffset.roundToInt()
                    )
                }
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
    isDarkModeEnabled: Boolean,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState
) {
    var isTextNotFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        cardDetailsViewState.itemPoissonOffset.dp.toPx() +
            (2 * NORMAL_FILED_SIZE_DP).dp.toPx()
    }
    CardNumberInPutField(
        modifier = Modifier
            .onFocusChanged {
                isTextNotFocused = if (it.isFocused) {
                    coroutineScope.launch {
                        delay(300)
                        scrollState.animateScrollTo(
                            scrollToPosition.roundToInt() + scrollOffset.roundToInt()
                        )
                    }
                    true
                } else {
                    if (isTextNotFocused) {
                        viewModel.onValidateCardNumber(cardDetailsViewState.cardNumberInputField.value)
                    }
                    false
                }
            }
            .padding(top = 16.dp),
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
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState
) {
    var isTextNotFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        cardDetailsViewState.itemPoissonOffset.dp.toPx() +
            (3 * NORMAL_FILED_SIZE_DP).dp.toPx()
    }
    CardExpireDateInputField(
        modifier = Modifier.onFocusChanged {
            isTextNotFocused = if (it.isFocused) {
                coroutineScope.launch {
                    delay(300)
                    scrollState.animateScrollTo(
                        scrollToPosition.roundToInt() + scrollOffset.roundToInt()
                    )
                }
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
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState
) {
    var isTextNotFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        cardDetailsViewState.itemPoissonOffset.dp.toPx() +
            (3 * NORMAL_FILED_SIZE_DP).dp.toPx()
    }
    CvvInputField(
        modifier = Modifier.onFocusChanged {
            isTextNotFocused = if (it.isFocused) {
                coroutineScope.launch {
                    delay(300)
                    scrollState.animateScrollTo(
                        scrollToPosition.roundToInt() + scrollOffset.roundToInt()
                    )
                }
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
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState
) {
    var isTextNotFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        cardDetailsViewState.itemPoissonOffset.dp.toPx() +
            (5 * NORMAL_FILED_SIZE_DP).dp.toPx()
    }
    val assistiveText = when (cardDetailsViewState.emailInputField.isError) {
        true -> { cardDetailsViewState.emailInputField.errorMessages?.let { AnnotatedString(stringResource(id = it)) } }
        else -> AnnotatedString(stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_field_subtitle_email_vt))
    }

    InputFieldWithErrorMessage(
        modifier = Modifier
            .onFocusChanged {
                isTextNotFocused = if (it.isFocused) {
                    coroutineScope.launch {
                        delay(300)
                        scrollState.animateScrollTo(
                            scrollToPosition.roundToInt() + scrollOffset.roundToInt()
                        )
                    }
                    true
                } else {
                    if (isTextNotFocused) {
                        viewModel.onValidateEmail(cardDetailsViewState.emailInputField.value)
                    }
                    false
                }
            }
            .padding(top = 16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        value = cardDetailsViewState.emailInputField.value,
        isError = cardDetailsViewState.emailInputField.isError,
        assistiveText = assistiveText,
        onValueChange = { viewModel.onEmailChanged(it) },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_email)) }
    )
}

private const val NORMAL_FILED_SIZE_DP = 100
