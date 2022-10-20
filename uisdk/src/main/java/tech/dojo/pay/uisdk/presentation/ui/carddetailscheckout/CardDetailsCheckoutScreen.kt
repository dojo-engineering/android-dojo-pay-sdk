package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.*
import tech.dojo.pay.uisdk.presentation.components.AmountBanner
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.CardNumberInPutField
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
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
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = BringIntoViewRequester()
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
                    AmountBannerItem(state)
                    EmailField(state, keyboardController, viewModel)
                    BillingCountryField(state,keyboardController,viewModel)
                    PostalCodeField(state,keyboardController,viewModel)
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
                        Box(modifier = Modifier.weight(1f))
                        {
                            CvvField(
                                coroutineScope,
                                bringIntoViewRequester,
                                state,
                                focusManager,
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
                    PayButton(bringIntoViewRequester, scrollState, state, viewModel)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PayButton(
    bringIntoViewRequester: BringIntoViewRequester,
    scrollState: ScrollState,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    SingleButtonView(
        modifier = Modifier.bringIntoViewRequester(bringIntoViewRequester),
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CvvField(
    coroutineScope: CoroutineScope,
    bringIntoViewRequester: BringIntoViewRequester,
    state: CardDetailsCheckoutState,
    focusManager: FocusManager,
    viewModel: CardDetailsCheckoutViewModel
) {
    CvvInputField(
        modifier = Modifier.onFocusEvent {
            if (it.hasFocus) {
                coroutineScope.launch {
                    bringIntoViewRequester.bringIntoView()
                }
            }
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
        cvvValue = state.cardDetailsInPutField.cvvValue,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        cvvPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv),
        onCvvValueChanged = { viewModel.onCvvValueChanged(it) })
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CardExpireDateField(
    keyboardController: SoftwareKeyboardController?,
    state: CardDetailsCheckoutState,
    viewModel: CardDetailsCheckoutViewModel
) {
    CardExpireDateInputField(
        label = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.15.sp
                ),
            ) {
                append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_label_expiry))
            }
        },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),

        expireDateValue = state.cardDetailsInPutField.expireDateValueValue,
        expireDaterPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_expiry),
        onExpireDateValueChanged = { viewModel.onExpireDareValueChanged(it) }
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
        cardNumberValue = state.cardDetailsInPutField.cardNumberValue,
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
                    append(stringResource(R.string.dojo_ui_sdk_card_details_email_field))
                }
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BillingCountryField(
    state: CardDetailsCheckoutState,
    keyboardController: SoftwareKeyboardController?,
    viewModel: CardDetailsCheckoutViewModel
) {
    if (state.isBillingCountryFieldRequired) {
        CountrySelectorField(
            label=buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        letterSpacing = 0.15.sp
                    ),
                ) {
                    append(stringResource(R.string.dojo_ui_sdk_card_details_billing_country_field))
                }
            },
            supportedCountriesViewEntity = state.supportedCountriesList,
            onCountrySelected = { viewModel.onCountrySelected(it) })
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
                    append(stringResource(R.string.dojo_ui_sdk_card_details_postcode_field))
                }
            }
        )
    }
}

@Composable
private fun AmountBannerItem(state: CardDetailsCheckoutState) {
    AmountBanner(
        amount = state.totalAmount,
        currencyLogo = state.amountCurrency
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