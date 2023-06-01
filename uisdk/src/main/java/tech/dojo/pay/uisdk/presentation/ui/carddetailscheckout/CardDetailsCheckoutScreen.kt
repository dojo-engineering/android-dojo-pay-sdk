package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
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
import tech.dojo.pay.uisdk.presentation.components.AmountWithPaymentMethodsHeader
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.CardExpireDateInputField
import tech.dojo.pay.uisdk.presentation.components.CardNumberInPutField
import tech.dojo.pay.uisdk.presentation.components.CheckBoxItem
import tech.dojo.pay.uisdk.presentation.components.CountrySelectorField
import tech.dojo.pay.uisdk.presentation.components.CvvInputField
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooterModes
import tech.dojo.pay.uisdk.presentation.components.InputFieldWithErrorMessage
import tech.dojo.pay.uisdk.presentation.components.SingleButtonView
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.WindowSize
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel.CardDetailsCheckoutViewModel
import kotlin.math.roundToInt
@Suppress("LongMethod")
@OptIn(ExperimentalComposeUiApi::class)
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
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var scrollToPosition by remember { mutableStateOf(0F) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = DojoTheme.colors.primarySurfaceBackgroundColor,
        topBar = { AppBarItem(onBackClicked, onCloseClicked) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = if (windowSize.widthWindowType == WindowSize.WindowType.COMPACT) 1f else .6f)
                        .padding(it)
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
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        AmountWithPaymentMethodsHeader(state)
                        EmailField(
                            scrollState,
                            coroutineScope,
                            scrollToPosition,
                            state,
                            keyboardController,
                            viewModel
                        )
                        BillingCountryField(
                            state,
                            viewModel
                        )
                        PostalCodeField(
                            scrollState,
                            coroutineScope,
                            scrollToPosition,
                            state,
                            keyboardController,
                            viewModel
                        )
                        CardHolderNameField(
                            scrollState,
                            coroutineScope,
                            scrollToPosition,
                            keyboardController,
                            state,
                            viewModel
                        )
                        CardNumberField(
                            scrollState,
                            coroutineScope,
                            scrollToPosition,
                            keyboardController,
                            state,
                            viewModel,
                            isDarkModeEnabled
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .heightIn(48.dp)
                        ) {
                            Box(
                                modifier = Modifier.weight(1f)
                            ) {
                                CardExpireDateField(
                                    scrollState,
                                    coroutineScope,
                                    scrollToPosition,
                                    keyboardController,
                                    state,
                                    viewModel
                                )
                            }

                            Divider(modifier = Modifier.width(32.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                CvvField(
                                    scrollState,
                                    coroutineScope,
                                    scrollToPosition,
                                    state,
                                    keyboardController,
                                    viewModel
                                )
                            }
                        }

                        SaveCardCheckBox(state, viewModel)
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(DojoTheme.colors.primarySurfaceBackgroundColor)
                    ) {
                        PayButton(scrollState, state, viewModel)
                        ScreenFooter(showDojoBrand)
                    }
                }
            }
        }
    )
}

@Composable
private fun ScreenFooter(showDojoBrand: Boolean) {
    DojoBrandFooter(
        modifier = Modifier.padding(bottom = 24.dp),
        mode = if (showDojoBrand) DojoBrandFooterModes.DOJO_BRAND_WITH_TERMS_AND_PRIVACY else DojoBrandFooterModes.TERMS_AND_PRIVACY_ONLY
    )
}

@Composable
private fun SaveCardCheckBox(
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    if (state.saveCardCheckBox.isVisible) {
        CheckBoxItem(
            itemText = stringResource(id = state.saveCardCheckBox.messageText),
            onCheckedChange = {
                viewModel.onSaveCardChecked(it)
            }
        )
    }
}

@Composable
private fun PayButton(
    scrollState: ScrollState,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    SingleButtonView(
        scrollState = scrollState,
        text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_button_pay) + " " + state.amountCurrency + " " + state.totalAmount,
        isLoading = state.isLoading,
        enabled = state.isEnabled
    ) {
        if (!state.isLoading) {
            viewModel.onPayWithCardClicked()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CvvField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    state: CardDetailsCheckoutState,
    keyboardController: SoftwareKeyboardController?,
    viewModel: CardDetailsCheckoutViewModel
) {
    val scrollOffset = with(LocalDensity.current) {
        if (state.isEmailInputFieldRequired && state.isPostalCodeFieldRequired) FIFTH_FIELD_OFF_SET_DP.dp.toPx()
        else if (state.isPostalCodeFieldRequired || state.isEmailInputFieldRequired) FORTH_FIELD_OFF_SET_DP.dp.toPx()
        else THIRD_FIELD_OFF_SET_DP.dp.toPx()
    }

    CvvInputField(
        modifier = Modifier
            .onFocusChanged {
                if (it.hasFocus) {
                    coroutineScope.launch {
                        delay(300)
                        scrollState.animateScrollTo(
                            scrollToPosition.roundToInt() + scrollOffset.roundToInt()
                        )
                    }
                }
                viewModel.validateCvv(
                    state.cvvInputFieldState.value,
                    it.isFocused
                )
            },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv)) },
        cvvValue = state.cvvInputFieldState.value,
        isError = state.cvvInputFieldState.isError,
        assistiveText = state.cvvInputFieldState.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),
        cvvPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv),
        onCvvValueChanged = { viewModel.onCvvValueChanged(it) }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CardExpireDateField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    keyboardController: SoftwareKeyboardController?,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    val scrollOffset = with(LocalDensity.current) {
        if (state.isEmailInputFieldRequired && state.isPostalCodeFieldRequired) FIFTH_FIELD_OFF_SET_DP.dp.toPx()
        else if (state.isPostalCodeFieldRequired || state.isEmailInputFieldRequired) FORTH_FIELD_OFF_SET_DP.dp.toPx()
        else THIRD_FIELD_OFF_SET_DP.dp.toPx()
    }

    CardExpireDateInputField(

        modifier = Modifier
            .onFocusChanged {
                if (it.isFocused) {
                    coroutineScope.launch {
                        delay(300)
                        scrollState.animateScrollTo(
                            scrollToPosition.roundToInt() + scrollOffset.roundToInt()
                        )
                    }
                }
                viewModel.validateExpireDate(
                    state.cardExpireDateInputField.value,
                    it.isFocused
                )
            },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_expiry_date)) },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),
        isError = state.cardExpireDateInputField.isError,
        assistiveText = state.cardExpireDateInputField.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        expireDateValue = state.cardExpireDateInputField.value,
        expireDaterPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_expiry),
        onExpireDateValueChanged = { viewModel.onExpireDateValueChanged(it) }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CardNumberField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    keyboardController: SoftwareKeyboardController?,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel,
    isDarkModeEnabled: Boolean
) {
    val scrollOffset = with(LocalDensity.current) {
        if (state.isEmailInputFieldRequired && state.isPostalCodeFieldRequired) FORTH_FIELD_OFF_SET_DP.dp.toPx()
        else if (state.isPostalCodeFieldRequired || state.isEmailInputFieldRequired) THIRD_FIELD_OFF_SET_DP.dp.toPx()
        else SECOND_FIELD_OFF_SET_DP.dp.toPx()
    }
    var focusedTextKey by remember { mutableStateOf(false) }

    CardNumberInPutField(
        modifier = Modifier.onFocusChanged {
            if (it.isFocused) {
                coroutineScope.launch {
                    delay(300)
                    scrollState.animateScrollTo(
                        scrollToPosition.roundToInt() + scrollOffset.roundToInt()
                    )
                }
            }
            focusedTextKey = if (it.isFocused) { true } else {
                if (focusedTextKey) {
                    viewModel.validateCardNumber(
                        state.cardNumberInputField.value,
                        it.isFocused
                    )
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
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        isError = state.cardNumberInputField.isError,
        assistiveText = state.cardNumberInputField.errorMessages?.let {
            AnnotatedString(
                stringResource(id = it)
            )
        },
        cardNumberValue = state.cardNumberInputField.value,
        cardNumberPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_pan),
        onCardNumberValueChanged = { viewModel.onCardNumberValueChanged(it) },
        isDarkModeEnabled = isDarkModeEnabled
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CardHolderNameField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    keyboardController: SoftwareKeyboardController?,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    val scrollOffset = with(LocalDensity.current) {
        if (state.isEmailInputFieldRequired && state.isPostalCodeFieldRequired) THIRD_FIELD_OFF_SET_DP.dp.toPx()
        else if (state.isPostalCodeFieldRequired || state.isEmailInputFieldRequired) SECOND_FIELD_OFF_SET_DP.dp.toPx()
        else FIRST_FIELD_OFF_SET_DP.dp.toPx()
    }

    InputFieldWithErrorMessage(
        modifier = Modifier.onFocusChanged {
            if (it.isFocused) {
                coroutineScope.launch {
                    delay(300)
                    scrollState.animateScrollTo(
                        scrollToPosition.roundToInt() + scrollOffset.roundToInt()
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        value = state.cardHolderInputField.value,
        isError = state.cardHolderInputField.isError,
        assistiveText = state.cardHolderInputField.errorMessages?.let {
            AnnotatedString(
                stringResource(id = it)
            )
        },
        onValueChange = { viewModel.onCardHolderValueChanged(it) },
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_card_name)) }
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
private fun EmailField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    state: CardDetailsCheckoutState,
    keyboardController: SoftwareKeyboardController?,
    viewModel: CardDetailsCheckoutViewModel
) {
    if (state.isEmailInputFieldRequired) {
        val scrollOffset = with(LocalDensity.current) {
            FIRST_FIELD_OFF_SET_DP.dp.toPx()
        }

        InputFieldWithErrorMessage(
            modifier = Modifier
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            delay(300)
                            scrollState.animateScrollTo(
                                scrollToPosition.roundToInt() + scrollOffset.roundToInt()
                            )
                        }
                    }
                    viewModel.validateEmailValue(
                        state.emailInputField.value,
                        it.isFocused
                    )
                },
            isError = state.emailInputField.isError,
            assistiveText = state.emailInputField.errorMessages?.let {
                AnnotatedString(
                    stringResource(id = it)
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            value = state.emailInputField.value,
            onValueChange = { viewModel.onEmailValueChanged(it) },
            label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_email)) }
        )
    }
}

@Composable
private fun BillingCountryField(
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    if (state.isBillingCountryFieldRequired) {
        CountrySelectorField(
            label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_billing_country)) },
            supportedCountriesViewEntity = state.supportedCountriesList,
            onCountrySelected = { viewModel.onCountrySelected(it) }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PostalCodeField(
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    state: CardDetailsCheckoutState,
    keyboardController: SoftwareKeyboardController?,
    viewModel: CardDetailsCheckoutViewModel
) {
    if (state.isPostalCodeFieldRequired) {
        val scrollOffset = with(LocalDensity.current) {
            if (state.isEmailInputFieldRequired) SECOND_FIELD_OFF_SET_DP.dp.toPx()
            else FIRST_FIELD_OFF_SET_DP.dp.toPx()
        }
        InputFieldWithErrorMessage(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) {
                    coroutineScope.launch {
                        delay(300)
                        scrollState.animateScrollTo(
                            scrollToPosition.roundToInt() + scrollOffset.roundToInt()
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            value = state.postalCodeField.value,
            isError = state.postalCodeField.isError,
            assistiveText =
            state.postalCodeField.errorMessages?.let { AnnotatedString(stringResource(id = it)) },
            onValueChange = { viewModel.onPostalCodeValueChanged(it) },
            label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_billing_postcode)) }
        )
    }
}

@Composable
private fun AmountWithPaymentMethodsHeader(state: CardDetailsCheckoutState) {
    AmountWithPaymentMethodsHeader(
        amount = state.totalAmount,
        currencyLogo = state.amountCurrency,
        allowedPaymentMethodsIcons = state.allowedPaymentMethodsIcons
    )
}

@Composable
private fun AppBarItem(onBackClicked: () -> Unit, onCloseClicked: () -> Unit) {
    DojoAppBar(
        title = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_title),
        titleGravity = TitleGravity.LEFT,
        navigationIcon = AppBarIcon.back(DojoTheme.colors.headerButtonTintColor) { onBackClicked() },
        actionIcon = AppBarIcon.close(DojoTheme.colors.headerButtonTintColor) { onCloseClicked() }
    )
}

private const val FIRST_FIELD_OFF_SET_DP = 30
private const val SECOND_FIELD_OFF_SET_DP = 290
private const val THIRD_FIELD_OFF_SET_DP = 360
private const val FORTH_FIELD_OFF_SET_DP = 420
private const val FIFTH_FIELD_OFF_SET_DP = 490
