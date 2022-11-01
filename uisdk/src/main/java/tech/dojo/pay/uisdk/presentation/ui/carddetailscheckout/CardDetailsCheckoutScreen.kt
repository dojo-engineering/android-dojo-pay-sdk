package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.AmountWithPaymentMethodsHeader
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.CardExpireDateInputField
import tech.dojo.pay.uisdk.presentation.components.CardNumberInPutField
import tech.dojo.pay.uisdk.presentation.components.CountrySelectorField
import tech.dojo.pay.uisdk.presentation.components.CvvInputField
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.InputFieldWithErrorMessage
import tech.dojo.pay.uisdk.presentation.components.SingleButtonView
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel.CardDetailsCheckoutViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun CardDetailsCheckoutScreen(
    viewModel: CardDetailsCheckoutViewModel,
    onCloseClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    val state = viewModel.state.observeAsState().value ?: return
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color.White,
        topBar = { AppBarItem(onBackClicked, onCloseClicked) },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(
                    Modifier
                        .verticalScroll(scrollState)
                        .imePadding()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 150.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    AmountWithPaymentMethodsHeader(state)
                    EmailField(state, keyboardController, viewModel)
                    BillingCountryField(state, viewModel)
                    PostalCodeField(state, keyboardController, viewModel)
                    CardHolderNameField(keyboardController, state, viewModel)
                    CardNumberField(keyboardController, state, viewModel)
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .heightIn(48.dp)
                    ) {
                        Box(
                            modifier = Modifier.weight(1f)
                        ) { CardExpireDateField(keyboardController, state, viewModel) }

                        Divider(modifier = Modifier.width(32.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            CvvField(
                                keyboardController,
                                state,
                                viewModel
                            )
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(Color.White)
                ) {
                    PayButton(scrollState, state, viewModel)
                    ScreenFooter()
                }
            }
        }
    )
}

@Composable
private fun ScreenFooter() {
    DojoBrandFooter(
        withTermsAndPrivacy = true,
        modifier = Modifier.padding(bottom = 24.dp)
    )
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
    keyboardController: SoftwareKeyboardController?,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    CvvInputField(
        modifier = Modifier
            .onFocusChanged {
                viewModel.validateCvv(
                    state.cvvInputFieldState.value,
                    it.isFocused
                )
            },
        label = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.15.sp
                ),
            ) {
                append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv))
            }
        },
        cvvValue = state.cvvInputFieldState.value,
        isError = state.cvvInputFieldState.isError,
        assistiveText = state.cvvInputFieldState.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        cvvPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv),
        onCvvValueChanged = { viewModel.onCvvValueChanged(it) }
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
private fun CardExpireDateField(
    keyboardController: SoftwareKeyboardController?,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    CardExpireDateInputField(
        modifier = Modifier
            .onFocusChanged {
                viewModel.validateExpireDate(
                    state.cardExpireDateInputField.value,
                    it.isFocused
                )
            },
        label = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.15.sp
                ),
            ) {
                append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_expiry_date))
            }
        },
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
    keyboardController: SoftwareKeyboardController?,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    CardNumberInPutField(
        modifier = Modifier.onFocusChanged {
            viewModel.validateCardNumber(
                state.cardNumberInputField.value,
                it.isFocused
            )
        },
        label = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.15.sp
                ),
            ) {
                append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_pan))
            }
        },
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
        onCardNumberValueChanged = { viewModel.onCardNumberValueChanged(it) }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CardHolderNameField(
    keyboardController: SoftwareKeyboardController?,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    InputFieldWithErrorMessage(
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
        label = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.15.sp
                ),
            ) {
                append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_card_name))
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun EmailField(
    state: CardDetailsCheckoutState,
    keyboardController: SoftwareKeyboardController?,
    viewModel: CardDetailsCheckoutViewModel
) {
    if (state.isEmailInputFieldRequired) {
        InputFieldWithErrorMessage(
            modifier = Modifier.onFocusChanged {
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
            label = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        letterSpacing = 0.15.sp
                    ),
                ) {
                    append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_email))
                }
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BillingCountryField(
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    if (state.isBillingCountryFieldRequired) {
        CountrySelectorField(
            label = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        letterSpacing = 0.15.sp
                    ),
                ) {
                    append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_billing_country))
                }
            },
            supportedCountriesViewEntity = state.supportedCountriesList,
            onCountrySelected = { viewModel.onCountrySelected(it) }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PostalCodeField(
    state: CardDetailsCheckoutState,
    keyboardController: SoftwareKeyboardController?,
    viewModel: CardDetailsCheckoutViewModel
) {
    if (state.isPostalCodeFieldRequired) {
        InputFieldWithErrorMessage(
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            value = state.postalCodeField.value,
            isError = state.postalCodeField.isError,
            assistiveText =
            state.postalCodeField.errorMessages?.let { AnnotatedString(stringResource(id = it)) },
            onValueChange = { viewModel.onPostalCodeValueChanged(it) },
            label = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        letterSpacing = 0.15.sp
                    ),
                ) {
                    append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_billing_postcode))
                }
            }
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
