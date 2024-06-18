package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.CardExpireDateInputField
import tech.dojo.pay.uisdk.presentation.components.CardNumberInPutField
import tech.dojo.pay.uisdk.presentation.components.CheckBoxItem
import tech.dojo.pay.uisdk.presentation.components.CountrySelectorField
import tech.dojo.pay.uisdk.presentation.components.CvvInputField
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooterModes
import tech.dojo.pay.uisdk.presentation.components.DojoSpacer
import tech.dojo.pay.uisdk.presentation.components.HeaderItem
import tech.dojo.pay.uisdk.presentation.components.InputFieldWithErrorMessage
import tech.dojo.pay.uisdk.presentation.components.KeyboardController
import tech.dojo.pay.uisdk.presentation.components.MerchantInfoWithSupportedNetworksHeader
import tech.dojo.pay.uisdk.presentation.components.SingleButtonView
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.WindowSize
import tech.dojo.pay.uisdk.presentation.components.rememberKeyboardController
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardCheckOutHeaderType
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel.CardDetailsCheckoutViewModel
import kotlin.math.roundToInt

@Suppress("LongMethod")
@Composable
internal fun CardDetailsCheckoutScreen(
    windowSize: WindowSize,
    viewModel: CardDetailsCheckoutViewModel,
    onCloseClicked: () -> Unit,
    onBackClicked: () -> Unit,
    isDarkModeEnabled: Boolean,
    showDojoBrand: Boolean,
) {
    val state = viewModel.state.observeAsState().value ?: return
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val keyboardController = rememberKeyboardController()
    var scrollToPosition by remember { mutableFloatStateOf(0F) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = DojoTheme.colors.primarySurfaceBackgroundColor,
        topBar = { AppBarItem(onBackClicked, onCloseClicked, state.toolbarTitle) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Loading(isVisible = state.isLoading)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = if (windowSize.widthWindowType == WindowSize.WindowType.COMPACT) 1f else .6f)
                        .padding(it),
                ) {
                    Column(
                        Modifier
                            .verticalScroll(scrollState)
                            .wrapContentHeight()
                            .onGloballyPositioned { layoutCoordinates ->
                                scrollToPosition =
                                    scrollState.value + layoutCoordinates.positionInRoot().y
                            }
                            .imePadding()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 150.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                    ) {
                        HeaderItem(state)
                        BillingCountryField(
                            state,
                            viewModel,
                        )
                        PostalCodeField(
                            scrollState,
                            coroutineScope,
                            scrollToPosition,
                            state,
                            keyboardController,
                            viewModel,
                        )
                        CardHolderNameField(
                            scrollState,
                            coroutineScope,
                            scrollToPosition,
                            keyboardController,
                            state,
                            viewModel,
                        )
                        CardNumberField(
                            scrollState,
                            coroutineScope,
                            scrollToPosition,
                            keyboardController,
                            state,
                            viewModel,
                            isDarkModeEnabled,
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .heightIn(48.dp),
                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                            ) {
                                CardExpireDateField(
                                    scrollState,
                                    coroutineScope,
                                    scrollToPosition,
                                    keyboardController,
                                    state,
                                    viewModel,
                                )
                            }

                            DojoSpacer(width = 32.dp)

                            Box(modifier = Modifier.weight(1f)) {
                                CvvField(
                                    scrollState,
                                    coroutineScope,
                                    scrollToPosition,
                                    state,
                                    keyboardController,
                                    viewModel,
                                )
                            }
                        }
                        EmailField(
                            scrollState,
                            coroutineScope,
                            scrollToPosition,
                            state,
                            keyboardController,
                            viewModel,
                        )
                        SaveCardCheckBox(state, viewModel)
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(DojoTheme.colors.primarySurfaceBackgroundColor),
                    ) {
                        ActionButton(scrollState, state, viewModel)
                        ScreenFooter(showDojoBrand)
                    }
                }
            }
        },
    )
}

@Composable
private fun Loading(isVisible: Boolean) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DojoTheme.colors.primarySurfaceBackgroundColor.copy(alpha = 0.8f))
                .clickable(false) {},
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                color = DojoTheme.colors.loadingIndicatorColor,
            )
        }
    }
}

@Composable
private fun ScreenFooter(showDojoBrand: Boolean) {
    DojoBrandFooter(
        modifier = Modifier.padding(bottom = 24.dp),
        mode = if (showDojoBrand) DojoBrandFooterModes.DOJO_BRAND_WITH_TERMS_AND_PRIVACY else DojoBrandFooterModes.TERMS_AND_PRIVACY_ONLY,
    )
}

@Composable
private fun SaveCardCheckBox(
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel,
) {
    if (state.checkBoxItem.isVisible) {
        CheckBoxItem(
            itemText = state.checkBoxItem.messageText,
            isChecked = state.checkBoxItem.isChecked,
            onCheckedChange = {
                viewModel.onCheckBoxChecked(it)
            },
        )
    }
}

@Composable
private fun ActionButton(
    scrollState: ScrollState,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel,
) {
    val focusManager = LocalFocusManager.current
    SingleButtonView(
        scrollState = scrollState,
        text = state.actionButtonState.text,
        isLoading = state.actionButtonState.isLoading,
        enabled = state.actionButtonState.isEnabled,
    ) {
        if (!state.actionButtonState.isLoading) {
            focusManager.clearFocus()
            viewModel.onPayWithCardClicked()
        }
    }
}

@Composable
private fun CvvField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    state: CardDetailsCheckoutState,
    keyboardController: KeyboardController,
    viewModel: CardDetailsCheckoutViewModel,
) {
    val scrollOffset = with(LocalDensity.current) {
        if (state.isBillingCountryFieldRequired && state.isPostalCodeFieldRequired) {
            FIFTH_FIELD_OFF_SET_DP.dp.toPx()
        } else if (state.isBillingCountryFieldRequired || state.isPostalCodeFieldRequired) {
            FORTH_FIELD_OFF_SET_DP.dp.toPx()
        } else {
            THIRD_FIELD_OFF_SET_DP.dp.toPx()
        }
    }
    val hasBeenFocused by remember { mutableStateOf(false) }

    CvvInputField(
        modifier = getModifierWithFocusChangedLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            scrollToPosition = scrollToPosition,
            scrollOffset = scrollOffset,
            initialHasBeenFocused = hasBeenFocused,
            onValidate = { viewModel.validateCvv(state.cvvInputFieldState.value) },
        ),
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv)) },
        cvvValue = state.cvvInputFieldState.value,
        isError = state.cvvInputFieldState.isError,
        assistiveText = state.cvvInputFieldState.errorMessages?.let { AnnotatedString(it) },
        keyboardActions = KeyboardActions(onDone = {
            keyboardController.hide()
        }),
        cvvPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv),
        onCvvValueChanged = { viewModel.onCvvValueChanged(it) },
    )
}

@Composable
private fun CardExpireDateField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    keyboardController: KeyboardController,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel,
) {
    val scrollOffset = with(LocalDensity.current) {
        if (state.isBillingCountryFieldRequired && state.isPostalCodeFieldRequired) {
            FIFTH_FIELD_OFF_SET_DP.dp.toPx()
        } else if (state.isBillingCountryFieldRequired || state.isPostalCodeFieldRequired) {
            FORTH_FIELD_OFF_SET_DP.dp.toPx()
        } else {
            THIRD_FIELD_OFF_SET_DP.dp.toPx()
        }
    }
    val hasBeenFocused by remember { mutableStateOf(false) }

    CardExpireDateInputField(
        modifier = getModifierWithFocusChangedLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            scrollToPosition = scrollToPosition,
            scrollOffset = scrollOffset,
            initialHasBeenFocused = hasBeenFocused,
            onValidate = { viewModel.validateExpireDate(state.cardExpireDateInputField.value) },
        ),
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_expiry_date)) },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController.hide()
        }),
        isError = state.cardExpireDateInputField.isError,
        assistiveText = state.cardExpireDateInputField.errorMessages?.let { AnnotatedString(it) },
        expireDateValue = state.cardExpireDateInputField.value,
        expireDaterPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_expiry),
        onExpireDateValueChanged = { viewModel.onExpireDateValueChanged(it) },
    )
}

@Composable
private fun CardNumberField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    keyboardController: KeyboardController,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel,
    isDarkModeEnabled: Boolean,
) {
    val scrollOffset = with(LocalDensity.current) {
        if (state.isBillingCountryFieldRequired && state.isPostalCodeFieldRequired) {
            FORTH_FIELD_OFF_SET_DP.dp.toPx()
        } else if (state.isBillingCountryFieldRequired || state.isPostalCodeFieldRequired) {
            THIRD_FIELD_OFF_SET_DP.dp.toPx()
        } else {
            SECOND_FIELD_OFF_SET_DP.dp.toPx()
        }
    }
    val hasBeenFocused by remember { mutableStateOf(false) }

    CardNumberInPutField(
        modifier = getModifierWithFocusChangedLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            scrollToPosition = scrollToPosition,
            scrollOffset = scrollOffset,
            initialHasBeenFocused = hasBeenFocused,
            onValidate = { viewModel.validateCardNumber(state.cardNumberInputField.value) },
        ),
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_pan)) },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController.hide() }),
        isError = state.cardNumberInputField.isError,
        assistiveText = state.cardNumberInputField.errorMessages?.let {
            AnnotatedString(it)
        },
        cardNumberValue = state.cardNumberInputField.value,
        cardNumberPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_pan),
        onCardNumberValueChanged = { viewModel.onCardNumberValueChanged(it) },
        isDarkModeEnabled = isDarkModeEnabled,
        supportedCardSchemas = state.allowedCardSchemes
    )
}

@Composable
private fun CardHolderNameField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    keyboardController: KeyboardController,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel,
) {
    val scrollOffset = with(LocalDensity.current) {
        if (state.isBillingCountryFieldRequired && state.isPostalCodeFieldRequired) {
            THIRD_FIELD_OFF_SET_DP.dp.toPx()
        } else if (state.isBillingCountryFieldRequired || state.isPostalCodeFieldRequired) {
            SECOND_FIELD_OFF_SET_DP.dp.toPx()
        } else {
            FIRST_FIELD_OFF_SET_DP.dp.toPx()
        }
    }

    val hasBeenFocused by remember { mutableStateOf(false) }

    InputFieldWithErrorMessage(
        modifier = getModifierWithFocusChangedLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            scrollToPosition = scrollToPosition,
            scrollOffset = scrollOffset,
            initialHasBeenFocused = hasBeenFocused,
            onValidate = { viewModel.validateCardHolder(state.cardHolderInputField.value) },
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController.hide() }),
        value = state.cardHolderInputField.value,
        isError = state.cardHolderInputField.isError,
        assistiveText = state.cardHolderInputField.errorMessages?.let { AnnotatedString(it) },
        onValueChange = { viewModel.onCardHolderValueChanged(it) },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_card_name)) },
    )
}

@Composable
private fun EmailField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    state: CardDetailsCheckoutState,
    keyboardController: KeyboardController,
    viewModel: CardDetailsCheckoutViewModel,
) {
    if (state.isEmailInputFieldRequired) {
        val scrollOffset = with(LocalDensity.current) {
            if (state.isBillingCountryFieldRequired && state.isPostalCodeFieldRequired) {
                SIXTH_FIELD_OFF_SET_DP.dp.toPx()
            } else {
                FIFTH_FIELD_OFF_SET_DP.dp.toPx()
            }
        }
        val hasBeenFocused by remember { mutableStateOf(false) }

        InputFieldWithErrorMessage(
            modifier = getModifierWithFocusChangedLogic(
                coroutineScope = coroutineScope,
                scrollState = scrollState,
                scrollToPosition = scrollToPosition,
                scrollOffset = scrollOffset,
                initialHasBeenFocused = hasBeenFocused,
                onValidate = { viewModel.validateEmailValue(state.emailInputField.value) },
            ),
            isError = state.emailInputField.isError,
            assistiveText = state.emailInputField.errorMessages?.let {
                AnnotatedString(it)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController.hide() }),
            value = state.emailInputField.value,
            onValueChange = { viewModel.onEmailValueChanged(it) },
            label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_email)) },
        )
    }
}

@Composable
private fun BillingCountryField(
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel,
) {
    if (state.isBillingCountryFieldRequired) {
        CountrySelectorField(
            label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_billing_country)) },
            supportedCountriesViewEntity = state.supportedCountriesList,
            onCountrySelected = { viewModel.onCountrySelected(it) },
        )
    }
}

@Composable
private fun PostalCodeField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    state: CardDetailsCheckoutState,
    keyboardController: KeyboardController,
    viewModel: CardDetailsCheckoutViewModel,
) {
    if (state.isPostalCodeFieldRequired) {
        val scrollOffset = with(LocalDensity.current) {
            SECOND_FIELD_OFF_SET_DP.dp.toPx()
        }
        val hasBeenFocused by remember { mutableStateOf(false) }
        InputFieldWithErrorMessage(
            modifier = getModifierWithFocusChangedLogic(
                coroutineScope = coroutineScope,
                scrollState = scrollState,
                scrollToPosition = scrollToPosition,
                scrollOffset = scrollOffset,
                initialHasBeenFocused = hasBeenFocused,
                onValidate = {
                    viewModel.validatePostalCode(state.postalCodeField.value)
                },
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController.hide() }),
            value = state.postalCodeField.value,
            isError = state.postalCodeField.isError,
            assistiveText =
            state.postalCodeField.errorMessages?.let { AnnotatedString(it) },
            onValueChange = { viewModel.onPostalCodeValueChanged(it) },
            label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_billing_postcode)) },
        )
    }
}

@Composable
private fun HeaderItem(state: CardDetailsCheckoutState) {
    when (state.headerType) {
        CardCheckOutHeaderType.AMOUNT_HEADER -> {
            HeaderItem(
                amount = state.totalAmount,
                currencyLogo = state.amountCurrency,
                allowedPaymentMethodsIcons = state.allowedPaymentMethodsIcons,
            )
        }

        CardCheckOutHeaderType.MERCHANT_HEADER -> {
            state.orderId?.let { orderId ->
                state.merchantName?.let { merchantName ->
                    MerchantInfoWithSupportedNetworksHeader(
                        merchantName = merchantName,
                        orderId = orderId,
                        allowedPaymentMethodsIcons = state.allowedPaymentMethodsIcons,
                    )
                }
            }
        }
    }
}

@Composable
private fun AppBarItem(
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    toolbarTitle: String?,
) {
    DojoAppBar(
        title = toolbarTitle
            ?: stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_title),
        titleGravity = TitleGravity.LEFT,
        navigationIcon = AppBarIcon.back(DojoTheme.colors.headerButtonTintColor) { onBackClicked() },
        actionIcon = AppBarIcon.close(DojoTheme.colors.headerButtonTintColor) { onCloseClicked() },
    )
}

@Composable
fun getModifierWithFocusChangedLogic(
    coroutineScope: CoroutineScope,
    scrollState: ScrollState,
    scrollToPosition: Float,
    scrollOffset: Float,
    initialHasBeenFocused: Boolean,
    onValidate: () -> Unit,
): Modifier {
    var hasBeenFocused by remember { mutableStateOf(initialHasBeenFocused) }
    return Modifier.onFocusChanged { focusState ->
        if (focusState.isFocused) {
            coroutineScope.launch {
                delay(300)
                scrollState.animateScrollTo(
                    (scrollToPosition + scrollOffset).roundToInt(),
                )
            }
            hasBeenFocused = true
        } else {
            if (hasBeenFocused) {
                onValidate()
            }
        }
    }
}

private const val FIRST_FIELD_OFF_SET_DP = 30
private const val SECOND_FIELD_OFF_SET_DP = 230
private const val THIRD_FIELD_OFF_SET_DP = 330
private const val FORTH_FIELD_OFF_SET_DP = 430
private const val FIFTH_FIELD_OFF_SET_DP = 500
private const val SIXTH_FIELD_OFF_SET_DP = 600
