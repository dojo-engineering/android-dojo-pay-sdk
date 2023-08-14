package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.core.StringProvider
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.GetRefreshedPaymentTokenFlow
import tech.dojo.pay.uisdk.domain.GetSupportedCountriesUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.RefreshPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult
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
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase = mock()
    private val getSupportedCountriesUseCase: GetSupportedCountriesUseCase = mock()
    private val supportedCountriesViewEntityMapper: SupportedCountriesViewEntityMapper = mock()
    private val allowedPaymentMethodsViewEntityMapper: AllowedPaymentMethodsViewEntityMapper =
        mock()
    private val cardCheckoutScreenValidator: CardCheckoutScreenValidator = mock()
    private val cardCheckOutFullCardPaymentPayloadMapper: CardCheckOutFullCardPaymentPayloadMapper =
        mock()
    private val stringProvider: StringProvider = mock()
    private val refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase = mock()
    private val getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow = mock()
    private val navigateToCardResult: (dojoPaymentResult: DojoPaymentResult) -> Unit = {}
    private val isStartDestination: Boolean = false

    @Before
    fun setUp() {
        val toolBarTitle = "toolBarTitle"
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
    }

    @Test
    fun `test initial state`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
        given(observePaymentStatus.observePaymentStates()).willReturn(paymentStateFakeFlow)

        val expected = CardDetailsCheckoutState(
            toolbarTitle = "toolBarTitle",
            totalAmount = "",
            amountCurrency = "",
            allowedPaymentMethodsIcons = emptyList(),
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
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
            cardCheckoutScreenValidator,
            cardCheckOutFullCardPaymentPayloadMapper,
            stringProvider,
            isStartDestination,
            refreshPaymentIntentUseCase,
            getRefreshedPaymentTokenFlow,
            navigateToCardResult,
        )
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when paymentIntent emits with collect billing address false`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
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
        paymentStateFakeFlow.tryEmit(true)
        given(allowedPaymentMethodsViewEntityMapper.apply(any())).willReturn(supportedIcons)
        val payText = "pay £ 100"

        val expected = CardDetailsCheckoutState(
            orderId = "", merchantName = "",
            toolbarTitle = "toolBarTitle",
            totalAmount = "100",
            amountCurrency = "£",
            isBillingCountryFieldRequired = false,
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
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
            cardCheckoutScreenValidator,
            cardCheckOutFullCardPaymentPayloadMapper,
            stringProvider,
            isStartDestination,
            refreshPaymentIntentUseCase,
            getRefreshedPaymentTokenFlow,
            navigateToCardResult,
        )
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when paymentIntent emits with collect billing address true`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
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
            listOf(
                SupportedCountriesDomainEntity("", "", false),
            ),
        )
        given(supportedCountriesViewEntityMapper.apply(any())).willReturn(
            supportedCountriesViewEntity,
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
        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
            cardCheckoutScreenValidator,
            cardCheckOutFullCardPaymentPayloadMapper,
            stringProvider,
            isStartDestination,
            refreshPaymentIntentUseCase,
            getRefreshedPaymentTokenFlow,
            navigateToCardResult,
        )
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when paymentIntent emits with collect userId`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
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
                    customerId = "customerId",
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true,
                ),
            ),
        )
        paymentStateFakeFlow.tryEmit(true)
        given(getSupportedCountriesUseCase.getSupportedCountries()).willReturn(
            listOf(
                SupportedCountriesDomainEntity("", "", false),
            ),
        )
        given(supportedCountriesViewEntityMapper.apply(any())).willReturn(
            supportedCountriesViewEntity,
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
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
            cardCheckoutScreenValidator,
            cardCheckOutFullCardPaymentPayloadMapper,
            stringProvider,
            isStartDestination,
            refreshPaymentIntentUseCase,
            getRefreshedPaymentTokenFlow,
            navigateToCardResult,
        )
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user clicks on pay button with normal card payment`() = runTest {
        // arrange
        val fullCardPaymentPayload: DojoCardPaymentPayLoad.FullCardPaymentPayload = mockk()
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
        given(getRefreshedPaymentTokenFlow.getUpdatedPaymentTokenFlow()).willReturn(
            MutableStateFlow(
                RefreshPaymentIntentResult.Success(result = "token"),
            ),
        )
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
            listOf(
                SupportedCountriesDomainEntity("", "", false),
            ),
        )
        given(supportedCountriesViewEntityMapper.apply(any())).willReturn(
            supportedCountriesViewEntity,
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
        val payText = "pay £ 100"
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
            checkBoxItem = CheckBoxItem(
                isVisible = false,
                isChecked = true,
                messageText = "checkBoxMessage",
            ),
            isPostalCodeFieldRequired = true,
            postalCodeField = InputFieldState(value = ""),
            actionButtonState = ActionButtonState(isLoading = true, text = payText),
        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
            cardCheckoutScreenValidator,
            cardCheckOutFullCardPaymentPayloadMapper,
            stringProvider,
            isStartDestination,
            refreshPaymentIntentUseCase,
            getRefreshedPaymentTokenFlow,
            navigateToCardResult,
        )
        viewModel.onPayWithCardClicked()
        // assert
        verify(updatePaymentStateUseCase).updatePaymentSate(any())
        verify(dojoCardPaymentHandler).executeCardPayment(any(), any())
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test loading state when paymentState emits after clicking on pay `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
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
            listOf(
                SupportedCountriesDomainEntity("", "", false),
            ),
        )
        given(supportedCountriesViewEntityMapper.apply(any())).willReturn(
            supportedCountriesViewEntity,
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
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
            cardCheckoutScreenValidator,
            cardCheckOutFullCardPaymentPayloadMapper,
            stringProvider,
            isStartDestination,
            refreshPaymentIntentUseCase,
            getRefreshedPaymentTokenFlow,
            navigateToCardResult,
        )
        viewModel.onPayWithCardClicked()
        paymentStateFakeFlow.tryEmit(false)
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user update card holder field `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
        given(cardCheckoutScreenValidator.isCardNumberValid(any())).willReturn(true)
        given(cardCheckoutScreenValidator.isCvvValid(any())).willReturn(true)
        given(cardCheckoutScreenValidator.isCardExpireDateValid(any())).willReturn(true)
        given(
            cardCheckoutScreenValidator.isEmailFieldValidWithInputFieldVisibility(
                any(),
                any(),
            ),
        ).willReturn(true)
        given(
            cardCheckoutScreenValidator.isPostalCodeFieldWithInputFieldVisibility(
                any(),
                any(),
            ),
        ).willReturn(true)
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
            listOf(
                SupportedCountriesDomainEntity("", "", false),
            ),
        )
        given(supportedCountriesViewEntityMapper.apply(any())).willReturn(
            supportedCountriesViewEntity,
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
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
            cardCheckoutScreenValidator,
            cardCheckOutFullCardPaymentPayloadMapper,
            stringProvider,
            isStartDestination,
            refreshPaymentIntentUseCase,
            getRefreshedPaymentTokenFlow,
            navigateToCardResult,
        )

        viewModel.onCardHolderValueChanged("new")
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user update card information field `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
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
            listOf(
                SupportedCountriesDomainEntity("", "", false),
            ),
        )
        given(supportedCountriesViewEntityMapper.apply(any())).willReturn(
            supportedCountriesViewEntity,
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
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
            cardCheckoutScreenValidator,
            cardCheckOutFullCardPaymentPayloadMapper,
            stringProvider,
            isStartDestination,
            refreshPaymentIntentUseCase,
            getRefreshedPaymentTokenFlow,
            navigateToCardResult,
        )
        viewModel.onCardNumberValueChanged("new")
        viewModel.onCvvValueChanged("new")
        viewModel.onExpireDateValueChanged("new")
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user update email address  field `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
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
        given(supportedCountriesViewEntityMapper.apply(any())).willReturn(
            supportedCountriesViewEntity,
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
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
            cardCheckoutScreenValidator,
            cardCheckOutFullCardPaymentPayloadMapper,
            stringProvider,
            isStartDestination,
            refreshPaymentIntentUseCase,
            getRefreshedPaymentTokenFlow,
            navigateToCardResult,
        )
        viewModel.onEmailValueChanged("new")
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `pay button should be disabled if any of cardCheckoutScreenValidator methods return false `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> =
                MutableStateFlow(null)
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
            given(cardCheckoutScreenValidator.isCardNumberValid(any())).willReturn(false)
            given(cardCheckoutScreenValidator.isCvvValid(any())).willReturn(false)
            given(cardCheckoutScreenValidator.isCardExpireDateValid(any())).willReturn(false)
            given(cardCheckoutScreenValidator.isEmailValid(any())).willReturn(false)
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
            given(supportedCountriesViewEntityMapper.apply(any())).willReturn(
                supportedCountriesViewEntity,
            )
            given(allowedPaymentMethodsViewEntityMapper.apply(any())).willReturn(supportedIcons)
            // act
            val viewModel = CardDetailsCheckoutViewModel(
                observePaymentIntent,
                dojoCardPaymentHandler,
                observePaymentStatus,
                updatePaymentStateUseCase,
                getSupportedCountriesUseCase,
                supportedCountriesViewEntityMapper,
                allowedPaymentMethodsViewEntityMapper,
                cardCheckoutScreenValidator,
                cardCheckOutFullCardPaymentPayloadMapper,
                stringProvider,
                isStartDestination,
                refreshPaymentIntentUseCase,
                getRefreshedPaymentTokenFlow,
                navigateToCardResult,
            )
            viewModel.validateCvv("new", false)
            viewModel.validateCardNumber("new")
            viewModel.validateEmailValue("new", false)
            viewModel.validateExpireDate("new", false)
            // assert
            Assert.assertEquals(false, viewModel.state.value?.actionButtonState?.isEnabled)
        }
}
