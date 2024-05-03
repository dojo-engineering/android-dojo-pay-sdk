package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.core.StringProvider
import tech.dojo.pay.uisdk.domain.GetSupportedCountriesUseCase
import tech.dojo.pay.uisdk.domain.MakeCardPaymentUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.AllowedPaymentMethodsViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.CardCheckOutFullCardPaymentPayloadMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.SupportedCountriesViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.ActionButtonState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CheckBoxItem
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.validator.CardCheckoutScreenValidator

@Suppress("LongMethod", "LargeClass")
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CardDetailsCheckoutViewModelTest {
    @get:Rule
    @ExperimentalCoroutinesApi
    val coroutineScope = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val observePaymentIntent: ObservePaymentIntent = mock()
    private val dojoCardPaymentHandler: DojoCardPaymentHandler = mock()
    private val observePaymentStatus: ObservePaymentStatus = mock()
    private val getSupportedCountriesUseCase: GetSupportedCountriesUseCase = mock()
    private val supportedCountriesViewEntityMapper: SupportedCountriesViewEntityMapper = mock()
    private val allowedPaymentMethodsViewEntityMapper: AllowedPaymentMethodsViewEntityMapper =
        mock()
    private val cardCheckoutScreenValidator: CardCheckoutScreenValidator = mock()
    private val cardCheckOutFullCardPaymentPayloadMapper: CardCheckOutFullCardPaymentPayloadMapper =
        mock()
    private val stringProvider: StringProvider = mock()
    private val makeCardPaymentUseCase: MakeCardPaymentUseCase = mock()
    private val navigateToCardResult: (dojoPaymentResult: DojoPaymentResult) -> Unit = mock()
    private var isStartDestination: Boolean = false

    @Before
    fun setUp() {
        val toolBarTitle = "toolBarTitle"
        val saveCardToolBar = "saveCardToolBar"
        val payTitle = "pay"
        val checkBoxMessage = "checkBoxMessage"
        given(stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_button_pay)).willReturn(
            payTitle,
        )
        given(stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_title)).willReturn(
            toolBarTitle,
        )
        given(stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_save_card)).willReturn(
            checkBoxMessage,
        )
        given(stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_title_setup_intent)).willReturn(
            saveCardToolBar,
        )
    }

    @Test
    fun `when init viewModel with isStartDestination as false should emit correct state`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStateFakeFlow)

            val expected = CardDetailsCheckoutState(
                isLoading = false,
                toolbarTitle = "toolBarTitle",
                totalAmount = "",
                amountCurrency = "",
                allowedPaymentMethodsIcons = emptyList(),
                allowedCardSchemes = listOf(),
                cardHolderInputField = InputFieldState(value = ""),
                emailInputField = InputFieldState(value = ""),
                isBillingCountryFieldRequired = false,
                supportedCountriesList = emptyList(),
                currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
                isPostalCodeFieldRequired = false,
                postalCodeField = InputFieldState(value = ""),
                isEmailInputFieldRequired = false,
                checkBoxItem = CheckBoxItem(
                    isVisible = false,
                    isChecked = true,
                    messageText = "",
                ),
                cardNumberInputField = InputFieldState(value = ""),
                cardExpireDateInputField = InputFieldState(value = ""),
                cvvInputFieldState = InputFieldState(value = ""),
                actionButtonState = ActionButtonState(),
            )
            // act
            val viewModel = CardDetailsCheckoutViewModel(
                observePaymentIntent,
                dojoCardPaymentHandler,
                observePaymentStatus,
                getSupportedCountriesUseCase,
                supportedCountriesViewEntityMapper,
                allowedPaymentMethodsViewEntityMapper,
                cardCheckoutScreenValidator,
                cardCheckOutFullCardPaymentPayloadMapper,
                stringProvider,
                isStartDestination,
                makeCardPaymentUseCase,
                navigateToCardResult,
            )
            // assert
            Assert.assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `when init viewModel with isStartDestination as true should emit correct state with full loading as true  and correct toolBar title `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStateFakeFlow)
            isStartDestination = true

            val expected = CardDetailsCheckoutState(
                isLoading = true,
                toolbarTitle = "saveCardToolBar",
                totalAmount = "",
                amountCurrency = "",
                allowedPaymentMethodsIcons = emptyList(),
                allowedCardSchemes = listOf(),
                cardHolderInputField = InputFieldState(value = ""),
                emailInputField = InputFieldState(value = ""),
                isBillingCountryFieldRequired = false,
                supportedCountriesList = emptyList(),
                currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
                isPostalCodeFieldRequired = false,
                postalCodeField = InputFieldState(value = ""),
                isEmailInputFieldRequired = false,
                checkBoxItem = CheckBoxItem(
                    isVisible = false,
                    isChecked = false,
                    messageText = "",
                ),
                cardNumberInputField = InputFieldState(value = ""),
                cardExpireDateInputField = InputFieldState(value = ""),
                cvvInputFieldState = InputFieldState(value = ""),
                actionButtonState = ActionButtonState(),
            )
            // act
            val viewModel = CardDetailsCheckoutViewModel(
                observePaymentIntent,
                dojoCardPaymentHandler,
                observePaymentStatus,
                getSupportedCountriesUseCase,
                supportedCountriesViewEntityMapper,
                allowedPaymentMethodsViewEntityMapper,
                cardCheckoutScreenValidator,
                cardCheckOutFullCardPaymentPayloadMapper,
                stringProvider,
                isStartDestination,
                makeCardPaymentUseCase,
                navigateToCardResult,
            )
            // assert
            Assert.assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `when payment intent flow collect with collect billing address false viewModel should emit state without billing country `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
            val supportedIcons = listOf(1, 2, 3)
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStateFakeFlow)
            paymentIntentFakeFlow.tryEmit(
                PaymentIntentResult.Success(
                    result = PaymentIntentDomainEntity(
                        "id",
                        "token",
                        AmountDomainEntity(
                            10L,
                            "100",
                            "GBP",
                        ),
                        supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    ),
                ),
            )
            paymentStateFakeFlow.tryEmit(false)
            given(allowedPaymentMethodsViewEntityMapper.apply(any())).willReturn(supportedIcons)
            val payText = "pay £ 100"

            val expected = CardDetailsCheckoutState(
                orderId = "", merchantName = "",
                toolbarTitle = "toolBarTitle",
                totalAmount = "100",
                amountCurrency = "£",
                isBillingCountryFieldRequired = false,
                allowedCardSchemes = listOf(CardsSchemes.AMEX),
                supportedCountriesList = emptyList(),
                currentSelectedCountry = SupportedCountriesViewEntity(
                    countryName = "",
                    countryCode = "",
                    isPostalCodeEnabled = true,
                ),
                allowedPaymentMethodsIcons = listOf(1, 2, 3),
                cardHolderInputField = InputFieldState(value = ""),
                emailInputField = InputFieldState(value = ""),
                isEmailInputFieldRequired = false,
                cardNumberInputField = InputFieldState(value = ""),
                cardExpireDateInputField = InputFieldState(value = ""),
                cvvInputFieldState = InputFieldState(value = ""),
                isPostalCodeFieldRequired = false,
                checkBoxItem = CheckBoxItem(
                    isVisible = false,
                    isChecked = true,
                    messageText = "checkBoxMessage",
                ),
                postalCodeField = InputFieldState(value = ""),
                actionButtonState = ActionButtonState(text = payText),
            )
            // act
            val viewModel = CardDetailsCheckoutViewModel(
                observePaymentIntent,
                dojoCardPaymentHandler,
                observePaymentStatus,
                getSupportedCountriesUseCase,
                supportedCountriesViewEntityMapper,
                allowedPaymentMethodsViewEntityMapper,
                cardCheckoutScreenValidator,
                cardCheckOutFullCardPaymentPayloadMapper,
                stringProvider,
                isStartDestination,
                makeCardPaymentUseCase,
                navigateToCardResult,
            )
            // assert
            Assert.assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `when payment intent flow collect with collect billing address true viewModel should emit state with  billing country `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
            val supportedCountriesViewEntity = SupportedCountriesViewEntity(
                countryName = "EGP",
                countryCode = "EG",
                isPostalCodeEnabled = true,
            )
            val supportedIcons = listOf(1, 2, 3)
            val supportedCountriesDomainEntityList = listOf(
                SupportedCountriesDomainEntity("", "", false),
            )
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStateFakeFlow)
            paymentIntentFakeFlow.tryEmit(
                PaymentIntentResult.Success(
                    result = PaymentIntentDomainEntity(
                        "id",
                        "token",
                        AmountDomainEntity(
                            10L,
                            "100",
                            "GBP",
                        ),
                        supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                        collectionBillingAddressRequired = true,
                    ),
                ),
            )
            paymentStateFakeFlow.tryEmit(false)
            given(getSupportedCountriesUseCase.getSupportedCountries()).willReturn(
                supportedCountriesDomainEntityList,
            )
            given(
                supportedCountriesViewEntityMapper.mapToSupportedCountriesViewEntityWithPreSelectedCountry(
                    supportedCountriesDomainEntityList,
                    null,
                ),
            ).willReturn(
                listOf(supportedCountriesViewEntity),
            )

            given(allowedPaymentMethodsViewEntityMapper.apply(any())).willReturn(supportedIcons)
            val payText = "pay £ 100"
            val expected = CardDetailsCheckoutState(
                toolbarTitle = "toolBarTitle",
                orderId = "",
                merchantName = "",
                totalAmount = "100",
                amountCurrency = "£",
                isBillingCountryFieldRequired = true,
                supportedCountriesList = listOf(supportedCountriesViewEntity),
                currentSelectedCountry = SupportedCountriesViewEntity(
                    countryName = "EGP",
                    countryCode = "EG",
                    isPostalCodeEnabled = true,
                ),
                allowedPaymentMethodsIcons = listOf(1, 2, 3),
                cardHolderInputField = InputFieldState(value = ""),
                emailInputField = InputFieldState(value = ""),
                isEmailInputFieldRequired = false,
                cardNumberInputField = InputFieldState(value = ""),
                cardExpireDateInputField = InputFieldState(value = ""),
                cvvInputFieldState = InputFieldState(value = ""),
                isPostalCodeFieldRequired = true,
                checkBoxItem = CheckBoxItem(
                    isVisible = false,
                    isChecked = true,
                    messageText = "checkBoxMessage",
                ),
                postalCodeField = InputFieldState(value = ""),
                actionButtonState = ActionButtonState(text = payText),
                allowedCardSchemes = listOf(CardsSchemes.AMEX)
            )
            // act
            val viewModel = CardDetailsCheckoutViewModel(
                observePaymentIntent,
                dojoCardPaymentHandler,
                observePaymentStatus,
                getSupportedCountriesUseCase,
                supportedCountriesViewEntityMapper,
                allowedPaymentMethodsViewEntityMapper,
                cardCheckoutScreenValidator,
                cardCheckOutFullCardPaymentPayloadMapper,
                stringProvider,
                isStartDestination,
                makeCardPaymentUseCase,
                navigateToCardResult,
            )
            // assert
            Assert.assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `when payment intent flow collect with collect userId viewModel should emit state with save card checkBox and default as true`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
            val supportedCountriesViewEntity = SupportedCountriesViewEntity(
                countryName = "EGP",
                countryCode = "EG",
                isPostalCodeEnabled = true,
            )
            val supportedIcons = listOf(1, 2, 3)
            val supportedCountriesDomainEntityList = listOf(
                SupportedCountriesDomainEntity("", "", false),
            )
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStateFakeFlow)
            paymentIntentFakeFlow.tryEmit(
                PaymentIntentResult.Success(
                    result = PaymentIntentDomainEntity(
                        "id",
                        "token",
                        AmountDomainEntity(
                            10L,
                            "100",
                            "GBP",
                        ),
                        customerId = "customerId",
                        supportedCardsSchemes = listOf(),
                        collectionBillingAddressRequired = true,
                    ),
                ),
            )
            paymentStateFakeFlow.tryEmit(false)
            given(getSupportedCountriesUseCase.getSupportedCountries()).willReturn(
                supportedCountriesDomainEntityList,
            )
            given(
                supportedCountriesViewEntityMapper.mapToSupportedCountriesViewEntityWithPreSelectedCountry(
                    supportedCountriesDomainEntityList,
                    null,
                ),
            ).willReturn(
                listOf(supportedCountriesViewEntity),
            )
            val payText = "pay £ 100"
            given(allowedPaymentMethodsViewEntityMapper.apply(any())).willReturn(supportedIcons)
            val expected = CardDetailsCheckoutState(
                orderId = "",
                merchantName = "",
                toolbarTitle = "toolBarTitle",
                totalAmount = "100",
                amountCurrency = "£",
                isBillingCountryFieldRequired = true,
                supportedCountriesList = listOf(supportedCountriesViewEntity),
                currentSelectedCountry = SupportedCountriesViewEntity(
                    countryName = "EGP",
                    countryCode = "EG",
                    isPostalCodeEnabled = true,
                ),
                allowedPaymentMethodsIcons = listOf(1, 2, 3),
                cardHolderInputField = InputFieldState(value = ""),
                emailInputField = InputFieldState(value = ""),
                isEmailInputFieldRequired = false,
                cardNumberInputField = InputFieldState(value = ""),
                cardExpireDateInputField = InputFieldState(value = ""),
                cvvInputFieldState = InputFieldState(value = ""),
                isPostalCodeFieldRequired = true,
                checkBoxItem = CheckBoxItem(
                    isVisible = true,
                    isChecked = true,
                    messageText = "checkBoxMessage",
                ),
                postalCodeField = InputFieldState(value = ""),
                actionButtonState = ActionButtonState(text = payText),
            )
            // act
            val viewModel = CardDetailsCheckoutViewModel(
                observePaymentIntent,
                dojoCardPaymentHandler,
                observePaymentStatus,
                getSupportedCountriesUseCase,
                supportedCountriesViewEntityMapper,
                allowedPaymentMethodsViewEntityMapper,
                cardCheckoutScreenValidator,
                cardCheckOutFullCardPaymentPayloadMapper,
                stringProvider,
                isStartDestination,
                makeCardPaymentUseCase,
                navigateToCardResult,
            )
            // assert
            Assert.assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `when  onCardHolderValueChanged called state should have the new value and should be emitted`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            val supportedCountriesDomainEntityList = listOf(
                SupportedCountriesDomainEntity("", "", false),
            )
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(cardCheckoutScreenValidator.isCardNumberValidAndSupported(any(), any())).willReturn(true)
            given(cardCheckoutScreenValidator.isCardExpireDateValid(any())).willReturn(true)
            val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
            val supportedCountriesViewEntity = SupportedCountriesViewEntity(
                countryName = "EGP",
                countryCode = "EG",
                isPostalCodeEnabled = true,
            )
            val supportedIcons = listOf(1, 2, 3)
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStateFakeFlow)
            paymentIntentFakeFlow.tryEmit(
                PaymentIntentResult.Success(
                    result = PaymentIntentDomainEntity(
                        "id",
                        "token",
                        AmountDomainEntity(
                            10L,
                            "100",
                            "GBP",
                        ),
                        supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                        collectionBillingAddressRequired = true,
                    ),
                ),
            )
            paymentStateFakeFlow.tryEmit(true)
            given(getSupportedCountriesUseCase.getSupportedCountries()).willReturn(
                supportedCountriesDomainEntityList,
            )
            given(
                supportedCountriesViewEntityMapper.mapToSupportedCountriesViewEntityWithPreSelectedCountry(
                    supportedCountriesDomainEntityList,
                    null,
                ),
            ).willReturn(
                listOf(supportedCountriesViewEntity),
            )
            val payText = "pay £ 100"
            given(allowedPaymentMethodsViewEntityMapper.apply(any())).willReturn(supportedIcons)
            val expected = CardDetailsCheckoutState(
                orderId = "",
                merchantName = "",
                toolbarTitle = "toolBarTitle",
                totalAmount = "100",
                amountCurrency = "£",
                isBillingCountryFieldRequired = true,
                supportedCountriesList = listOf(supportedCountriesViewEntity),
                currentSelectedCountry = SupportedCountriesViewEntity(
                    countryName = "EGP",
                    countryCode = "EG",
                    isPostalCodeEnabled = true,
                ),
                allowedPaymentMethodsIcons = listOf(1, 2, 3),
                allowedCardSchemes = listOf(CardsSchemes.AMEX),
                cardHolderInputField = InputFieldState(value = "new"),
                emailInputField = InputFieldState(value = ""),
                isEmailInputFieldRequired = false,
                cardNumberInputField = InputFieldState(value = ""),
                cardExpireDateInputField = InputFieldState(value = ""),
                cvvInputFieldState = InputFieldState(value = ""),
                checkBoxItem = CheckBoxItem(
                    isVisible = false,
                    isChecked = true,
                    messageText = "checkBoxMessage",
                ),
                isPostalCodeFieldRequired = true,
                postalCodeField = InputFieldState(value = ""),
                actionButtonState = ActionButtonState(isEnabled = false, text = payText),
            )
            // act
            val viewModel = CardDetailsCheckoutViewModel(
                observePaymentIntent,
                dojoCardPaymentHandler,
                observePaymentStatus,
                getSupportedCountriesUseCase,
                supportedCountriesViewEntityMapper,
                allowedPaymentMethodsViewEntityMapper,
                cardCheckoutScreenValidator,
                cardCheckOutFullCardPaymentPayloadMapper,
                stringProvider,
                isStartDestination,
                makeCardPaymentUseCase,
                navigateToCardResult,
            )

            viewModel.onCardHolderValueChanged("new")
            // assert
            Assert.assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `when all card related fields is edited state should have the new values and should be emitted`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
            val supportedCountriesViewEntity = SupportedCountriesViewEntity(
                countryName = "EGP",
                countryCode = "EG",
                isPostalCodeEnabled = true,
            )
            val supportedIcons = listOf(1, 2, 3)
            val supportedCountriesDomainEntityList = listOf(
                SupportedCountriesDomainEntity("", "", false),
            )
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStateFakeFlow)
            paymentIntentFakeFlow.tryEmit(
                PaymentIntentResult.Success(
                    result = PaymentIntentDomainEntity(
                        "id",
                        "token",
                        AmountDomainEntity(
                            10L,
                            "100",
                            "GBP",
                        ),
                        supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                        collectionBillingAddressRequired = true,
                    ),
                ),
            )
            paymentStateFakeFlow.tryEmit(true)
            given(getSupportedCountriesUseCase.getSupportedCountries()).willReturn(
                supportedCountriesDomainEntityList,
            )
            given(
                supportedCountriesViewEntityMapper.mapToSupportedCountriesViewEntityWithPreSelectedCountry(
                    supportedCountriesDomainEntityList,
                    null,
                ),
            ).willReturn(
                listOf(supportedCountriesViewEntity),
            )
            val payText = "pay £ 100"
            given(allowedPaymentMethodsViewEntityMapper.apply(any())).willReturn(supportedIcons)
            val expected = CardDetailsCheckoutState(
                orderId = "",
                merchantName = "",
                toolbarTitle = "toolBarTitle",
                totalAmount = "100",
                amountCurrency = "£",
                isBillingCountryFieldRequired = true,
                supportedCountriesList = listOf(supportedCountriesViewEntity),
                currentSelectedCountry = SupportedCountriesViewEntity(
                    countryName = "EGP",
                    countryCode = "EG",
                    isPostalCodeEnabled = true,
                ),
                allowedPaymentMethodsIcons = listOf(1, 2, 3),
                allowedCardSchemes = listOf(CardsSchemes.AMEX),
                cardHolderInputField = InputFieldState(value = ""),
                emailInputField = InputFieldState(value = ""),
                isEmailInputFieldRequired = false,
                cardNumberInputField = InputFieldState(value = "new"),
                cardExpireDateInputField = InputFieldState(value = "new"),
                cvvInputFieldState = InputFieldState(value = "new"),
                checkBoxItem = CheckBoxItem(
                    isVisible = false,
                    isChecked = true,
                    messageText = "checkBoxMessage",
                ),
                isPostalCodeFieldRequired = true,
                postalCodeField = InputFieldState(value = ""),
                actionButtonState = ActionButtonState(text = payText),
            )
            // act
            val viewModel = CardDetailsCheckoutViewModel(
                observePaymentIntent,
                dojoCardPaymentHandler,
                observePaymentStatus,
                getSupportedCountriesUseCase,
                supportedCountriesViewEntityMapper,
                allowedPaymentMethodsViewEntityMapper,
                cardCheckoutScreenValidator,
                cardCheckOutFullCardPaymentPayloadMapper,
                stringProvider,
                isStartDestination,
                makeCardPaymentUseCase,
                navigateToCardResult,
            )
            viewModel.onCardNumberValueChanged("new")
            viewModel.onCvvValueChanged("new")
            viewModel.onExpireDateValueChanged("new")
            // assert
            Assert.assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `when onEmailValueChanged called  state should have the new value and should be emitted`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
            val supportedCountriesViewEntity = SupportedCountriesViewEntity(
                countryName = "EGP",
                countryCode = "EG",
                isPostalCodeEnabled = true,
            )
            val supportedIcons = listOf(1, 2, 3)
            val supportedCountriesDomainEntityList = listOf(
                SupportedCountriesDomainEntity("", "", false),
            )
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStateFakeFlow)
            paymentIntentFakeFlow.tryEmit(
                PaymentIntentResult.Success(
                    result = PaymentIntentDomainEntity(
                        "id",
                        "token",
                        AmountDomainEntity(
                            10L,
                            "100",
                            "GBP",
                        ),
                        supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                        collectionBillingAddressRequired = true,
                        collectionEmailRequired = true,
                    ),
                ),
            )
            paymentStateFakeFlow.tryEmit(true)
            given(getSupportedCountriesUseCase.getSupportedCountries()).willReturn(
                supportedCountriesDomainEntityList,
            )
            given(
                supportedCountriesViewEntityMapper.mapToSupportedCountriesViewEntityWithPreSelectedCountry(
                    supportedCountriesDomainEntityList,
                    null,
                ),
            ).willReturn(
                listOf(supportedCountriesViewEntity),
            )
            val payText = "pay £ 100"
            given(allowedPaymentMethodsViewEntityMapper.apply(any())).willReturn(supportedIcons)
            val expected = CardDetailsCheckoutState(
                orderId = "",
                merchantName = "",
                toolbarTitle = "toolBarTitle",
                totalAmount = "100",
                amountCurrency = "£",
                isBillingCountryFieldRequired = true,
                supportedCountriesList = listOf(supportedCountriesViewEntity),
                currentSelectedCountry = SupportedCountriesViewEntity(
                    countryName = "EGP",
                    countryCode = "EG",
                    isPostalCodeEnabled = true,
                ),
                allowedPaymentMethodsIcons = listOf(1, 2, 3),
                allowedCardSchemes = listOf(CardsSchemes.AMEX),
                cardHolderInputField = InputFieldState(value = ""),
                emailInputField = InputFieldState(value = "new"),
                isEmailInputFieldRequired = true,
                checkBoxItem = CheckBoxItem(
                    isVisible = false,
                    isChecked = true,
                    messageText = "checkBoxMessage",
                ),
                cardNumberInputField = InputFieldState(value = ""),
                cardExpireDateInputField = InputFieldState(value = ""),
                cvvInputFieldState = InputFieldState(value = ""),
                isPostalCodeFieldRequired = true,
                postalCodeField = InputFieldState(value = ""),
                actionButtonState = ActionButtonState(text = payText),

            )
            // act
            val viewModel = CardDetailsCheckoutViewModel(
                observePaymentIntent,
                dojoCardPaymentHandler,
                observePaymentStatus,
                getSupportedCountriesUseCase,
                supportedCountriesViewEntityMapper,
                allowedPaymentMethodsViewEntityMapper,
                cardCheckoutScreenValidator,
                cardCheckOutFullCardPaymentPayloadMapper,
                stringProvider,
                isStartDestination,
                makeCardPaymentUseCase,
                navigateToCardResult,
            )
            viewModel.onEmailValueChanged("new")
            // assert
            Assert.assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `when any of cardCheckoutScreenValidator methods return false action button should be disabled`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
            given(cardCheckoutScreenValidator.isCardNumberValidAndSupported(any(), any())).willReturn(false)
            given(cardCheckoutScreenValidator.isCvvValid(any())).willReturn(false)
            given(cardCheckoutScreenValidator.isCardExpireDateValid(any())).willReturn(false)
            given(cardCheckoutScreenValidator.isEmailValid(any())).willReturn(false)
            val supportedIcons = listOf(1, 2, 3)
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStateFakeFlow)
            paymentIntentFakeFlow.tryEmit(
                PaymentIntentResult.Success(
                    result = PaymentIntentDomainEntity(
                        "id",
                        "token",
                        AmountDomainEntity(
                            10L,
                            "100",
                            "GBP",
                        ),
                        supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                        collectionBillingAddressRequired = true,
                        collectionEmailRequired = true,
                    ),
                ),
            )
            paymentStateFakeFlow.tryEmit(true)
            given(getSupportedCountriesUseCase.getSupportedCountries()).willReturn(
                listOf(
                    SupportedCountriesDomainEntity("", "", false),
                ),
            )
            given(allowedPaymentMethodsViewEntityMapper.apply(any())).willReturn(supportedIcons)
            // act
            val viewModel = CardDetailsCheckoutViewModel(
                observePaymentIntent,
                dojoCardPaymentHandler,
                observePaymentStatus,
                getSupportedCountriesUseCase,
                supportedCountriesViewEntityMapper,
                allowedPaymentMethodsViewEntityMapper,
                cardCheckoutScreenValidator,
                cardCheckOutFullCardPaymentPayloadMapper,
                stringProvider,
                isStartDestination,
                makeCardPaymentUseCase,
                navigateToCardResult,
            )
            viewModel.validateCardHolder("new")
            viewModel.validateCardNumber("new")
            viewModel.validateExpireDate("new")
            viewModel.validateCvv("new")
            viewModel.validateEmailValue("new")
            viewModel.validatePostalCode("new")
            // assert
            Assert.assertEquals(false, viewModel.state.value?.actionButtonState?.isEnabled)
        }

    @Test
    fun `when onPayWithCardClicked called should call makeCardPayment from  makeCardPaymentUseCase`() =
        runTest {
            // arrange
            val fullCardPaymentPayload: DojoCardPaymentPayLoad.FullCardPaymentPayload = mock()
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            val supportedCountriesDomainEntityList = listOf(
                SupportedCountriesDomainEntity("", "", false),
            )
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
            val supportedCountriesViewEntity = SupportedCountriesViewEntity(
                countryName = "EGP",
                countryCode = "EG",
                isPostalCodeEnabled = true,
            )
            val supportedIcons = listOf(1, 2, 3)
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStateFakeFlow)
            paymentIntentFakeFlow.tryEmit(
                PaymentIntentResult.Success(
                    result = PaymentIntentDomainEntity(
                        "id",
                        "token",
                        AmountDomainEntity(
                            10L,
                            "100",
                            "GBP",
                        ),
                        supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                        collectionBillingAddressRequired = true,
                    ),
                ),
            )
            paymentStateFakeFlow.tryEmit(true)
            given(getSupportedCountriesUseCase.getSupportedCountries()).willReturn(
                supportedCountriesDomainEntityList,
            )
            given(
                supportedCountriesViewEntityMapper.mapToSupportedCountriesViewEntityWithPreSelectedCountry(
                    supportedCountriesDomainEntityList,
                    null,
                ),
            ).willReturn(
                listOf(supportedCountriesViewEntity),
            )
            given(
                cardCheckOutFullCardPaymentPayloadMapper.getPaymentPayLoad(
                    any(),
                    any(),
                ),
            ).willReturn(
                fullCardPaymentPayload,
            )
            given(allowedPaymentMethodsViewEntityMapper.apply(any())).willReturn(supportedIcons)
            // act
            val viewModel = CardDetailsCheckoutViewModel(
                observePaymentIntent,
                dojoCardPaymentHandler,
                observePaymentStatus,
                getSupportedCountriesUseCase,
                supportedCountriesViewEntityMapper,
                allowedPaymentMethodsViewEntityMapper,
                cardCheckoutScreenValidator,
                cardCheckOutFullCardPaymentPayloadMapper,
                stringProvider,
                isStartDestination,
                makeCardPaymentUseCase,
                navigateToCardResult,
            )
            viewModel.onPayWithCardClicked()
            // assert
            verify(makeCardPaymentUseCase).makeCardPayment(any(), any())
        }
}
