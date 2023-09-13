package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.CardExpireDateInputField
import tech.dojo.pay.uisdk.presentation.components.CardNumberInPutField
import tech.dojo.pay.uisdk.presentation.components.CvvInputField
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooterModes
import tech.dojo.pay.uisdk.presentation.components.DojoSpacer
import tech.dojo.pay.uisdk.presentation.components.InputFieldModifierWithFocusChangedAndScrollingLogic
import tech.dojo.pay.uisdk.presentation.components.InputFieldWithErrorMessage
import tech.dojo.pay.uisdk.presentation.components.SupportedPaymentMethods
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.medium
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CardDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.VirtualTerminalViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun CardDetailsSection(
    viewModel: VirtualTerminalViewModel,
    isDarkModeEnabled: Boolean,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
    showDojoBrand: Boolean,
) {
    val state = viewModel.state.observeAsState().value ?: return
    if (state.cardDetailsSection?.isVisible == true) {
        var parentPosition by remember { mutableStateOf(0f) }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.background(DojoTheme.colors.primarySurfaceBackgroundColor)
                .onGloballyPositioned { parentPosition = it.positionInParent().y },
        ) {
            HeaderTitle()
            SupportedPaymentMethods(
                Modifier.padding(top = 0.dp),
                state.cardDetailsSection.allowedPaymentMethodsIcons,
                stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_transactions_are_secure),
            )
            CardHolderInputField(
                state.cardDetailsSection,
                viewModel,
                coroutineScope,
                scrollToPosition,
                scrollState,
                keyboardController,
                parentPosition,
            )
            DojoSpacer(height = 4.dp)
            CardNumberInputField(
                state.cardDetailsSection,
                viewModel,
                isDarkModeEnabled,
                coroutineScope,
                scrollToPosition,
                scrollState,
                keyboardController,
                parentPosition,
            )

            var rowtPosition by remember { mutableStateOf(0f) }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .heightIn(48.dp)
                    .padding(top = 16.dp)
                    .onGloballyPositioned { rowtPosition = it.positionInParent().y },
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                ) {
                    CardExpireDateField(
                        state.cardDetailsSection,
                        viewModel,
                        coroutineScope,
                        scrollToPosition,
                        scrollState,
                        keyboardController,
                        parentPosition + rowtPosition,
                    )
                }

                DojoSpacer(width = 32.dp)
                Box(modifier = Modifier.weight(1f)) {
                    CvvField(
                        state.cardDetailsSection,
                        viewModel,
                        coroutineScope,
                        scrollToPosition,
                        scrollState,
                        keyboardController,
                        parentPosition + rowtPosition,
                    )
                }
            }

            EmailInputField(
                state.cardDetailsSection,
                viewModel,
                coroutineScope,
                scrollToPosition,
                scrollState,
                keyboardController,
                parentPosition,
            )
        }
        ScreenFooter(showDojoBrand)
    }
}

@Composable
private fun HeaderTitle() {
    Text(
        text = "Payment Details",
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = DojoTheme.typography.h6.medium,
        color = DojoTheme.colors.primaryLabelTextColor.copy(alpha = ContentAlpha.high),
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CardHolderInputField(
    cardDetailsViewState: CardDetailsViewState,
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
    parentPosition: Float,
) {
    val hasBeenFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        cardDetailsViewState.itemPoissonOffset.dp.toPx() + NORMAL_FILED_SIZE_DP.dp.toPx()
    }
    InputFieldWithErrorMessage(
        modifier = InputFieldModifierWithFocusChangedAndScrollingLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            initialHasBeenFocused = hasBeenFocused,
            parentPosition = parentPosition,
            onValidate = {
                viewModel.onValidateCardHolder(cardDetailsViewState.cardHolderInputField.value)
            },
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        value = cardDetailsViewState.cardHolderInputField.value,
        isError = cardDetailsViewState.cardHolderInputField.isError,
        assistiveText = cardDetailsViewState.cardHolderInputField.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        onValueChange = { viewModel.onCardHolderChanged(it) },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_card_name)) },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CardNumberInputField(
    cardDetailsViewState: CardDetailsViewState,
    viewModel: VirtualTerminalViewModel,
    isDarkModeEnabled: Boolean,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
    parentPosition: Float,
) {
    val hasBeenFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        cardDetailsViewState.itemPoissonOffset.dp.toPx() +
            (2 * NORMAL_FILED_SIZE_DP).dp.toPx()
    }
    CardNumberInPutField(
        modifier = InputFieldModifierWithFocusChangedAndScrollingLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            initialHasBeenFocused = hasBeenFocused,
            parentPosition = parentPosition,
            onValidate = {
                viewModel.onValidateCardNumber(cardDetailsViewState.cardNumberInputField.value)
            },
        ),
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_pan)) },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        isError = cardDetailsViewState.cardNumberInputField.isError,
        assistiveText = cardDetailsViewState.cardNumberInputField.errorMessages?.let {
            AnnotatedString(
                stringResource(id = it),
            )
        },
        cardNumberValue = cardDetailsViewState.cardNumberInputField.value,
        cardNumberPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_pan),
        onCardNumberValueChanged = { viewModel.onCardNumberChanged(it) },
        isDarkModeEnabled = isDarkModeEnabled,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CardExpireDateField(
    cardDetailsViewState: CardDetailsViewState,
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
    parentPosition: Float,
) {
    val hasBeenFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        cardDetailsViewState.itemPoissonOffset.dp.toPx() +
            (3 * NORMAL_FILED_SIZE_DP).dp.toPx()
    }
    CardExpireDateInputField(
        modifier = InputFieldModifierWithFocusChangedAndScrollingLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            initialHasBeenFocused = hasBeenFocused,
            parentPosition = parentPosition,
            onValidate = {
                viewModel.onValidateCardDate(cardDetailsViewState.cardExpireDateInputField.value)
            },
        ),
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_expiry_date)) },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        isError = cardDetailsViewState.cardExpireDateInputField.isError,
        assistiveText = cardDetailsViewState.cardExpireDateInputField.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        expireDateValue = cardDetailsViewState.cardExpireDateInputField.value,
        expireDaterPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_expiry),
        onExpireDateValueChanged = { viewModel.onCardDateChanged(it) },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CvvField(
    cardDetailsViewState: CardDetailsViewState,
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
    parentPosition: Float,
) {
    val hasBeenFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        cardDetailsViewState.itemPoissonOffset.dp.toPx() +
            (3 * NORMAL_FILED_SIZE_DP).dp.toPx()
    }
    CvvInputField(
        modifier = InputFieldModifierWithFocusChangedAndScrollingLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            initialHasBeenFocused = hasBeenFocused,
            parentPosition = parentPosition,
            onValidate = {
                viewModel.onValidateCvv(cardDetailsViewState.cvvInputFieldState.value)
            },
        ),
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv)) },
        cvvValue = cardDetailsViewState.cvvInputFieldState.value,
        isError = cardDetailsViewState.cvvInputFieldState.isError,
        assistiveText = cardDetailsViewState.cvvInputFieldState.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        cvvPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv),
        onCvvValueChanged = { viewModel.onCardCvvChanged(it) },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun EmailInputField(
    cardDetailsViewState: CardDetailsViewState,
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
    parentPosition: Float,
) {
    val hasBeenFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        cardDetailsViewState.itemPoissonOffset.dp.toPx() +
            (5 * NORMAL_FILED_SIZE_DP).dp.toPx()
    }
    val assistiveText = when (cardDetailsViewState.emailInputField.isError) {
        true -> { cardDetailsViewState.emailInputField.errorMessages?.let { AnnotatedString(stringResource(id = it)) } }
        else -> AnnotatedString(stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_field_subtitle_email_vt))
    }

    InputFieldWithErrorMessage(
        modifier = InputFieldModifierWithFocusChangedAndScrollingLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            initialHasBeenFocused = hasBeenFocused,
            parentPosition = parentPosition,
            onValidate = {
                viewModel.onValidateEmail(cardDetailsViewState.emailInputField.value)
            },
        ).padding(top = 16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        value = cardDetailsViewState.emailInputField.value,
        isError = cardDetailsViewState.emailInputField.isError,
        assistiveText = assistiveText,
        onValueChange = { viewModel.onEmailChanged(it) },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_email)) },
    )
}

@Composable
private fun ScreenFooter(showDojoBrand: Boolean) {
    DojoBrandFooter(
        mode = if (showDojoBrand) DojoBrandFooterModes.DOJO_BRAND_WITH_TERMS_AND_PRIVACY else DojoBrandFooterModes.TERMS_AND_PRIVACY_ONLY,
    )
}

private const val NORMAL_FILED_SIZE_DP = 160
