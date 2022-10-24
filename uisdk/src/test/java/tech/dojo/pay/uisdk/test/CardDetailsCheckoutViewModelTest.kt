package tech.dojo.pay.uisdk.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.GetSupportedCountriesUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.AllowedPaymentMethodsViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.SupportedCountriesViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsInputFieldState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel.CardDetailsCheckoutViewModel

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

    @Test
    fun `test initial state`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
        whenever(observePaymentStatus.observePaymentStates()).thenReturn(paymentStateFakeFlow)

        val expected = CardDetailsCheckoutState(
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
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "",
                expireDateValueValue = "",
            ),
            isLoading = false,
            isEnabled = false
        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper
        )
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when paymentIntent emits with collect billing address false`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
        val supportedIcons = listOf(1, 2, 3)
        whenever(observePaymentStatus.observePaymentStates()).thenReturn(paymentStateFakeFlow)
        paymentIntentFakeFlow.tryEmit(
            PaymentIntentResult.Success(
                result = PaymentIntentDomainEntity(
                    "id",
                    "token",
                    AmountDomainEntity(
                        10L,
                        "100",
                        "GBP"
                    ),
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX)
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        whenever(allowedPaymentMethodsViewEntityMapper.apply(any())).thenReturn(supportedIcons)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            isBillingCountryFieldRequired = false,
            supportedCountriesList = emptyList(),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
            allowedPaymentMethodsIcons = listOf(1, 2, 3),
            cardHolderInputField = InputFieldState(value = ""),
            emailInputField = InputFieldState(value = ""),
            isEmailInputFieldRequired = false,
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "",
                expireDateValueValue = "",
            ),
            isPostalCodeFieldRequired = false,
            postalCodeField = InputFieldState(value = ""),
            isLoading = false,
            isEnabled = false

        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper
        )
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when paymentIntent emits with collect billing address true`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
        val supportedCountriesViewEntity = SupportedCountriesViewEntity(
            countryName = "EGP",
            countryCode = "EG",
            isPostalCodeEnabled = true,
        )
        val supportedIcons = listOf(1, 2, 3)
        whenever(observePaymentStatus.observePaymentStates()).thenReturn(paymentStateFakeFlow)
        paymentIntentFakeFlow.tryEmit(
            PaymentIntentResult.Success(
                result = PaymentIntentDomainEntity(
                    "id",
                    "token",
                    AmountDomainEntity(
                        10L,
                        "100",
                        "GBP"
                    ),
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        whenever(getSupportedCountriesUseCase.getSupportedCountries()).thenReturn(
            listOf(
                SupportedCountriesDomainEntity("", "", false)
            )
        )
        whenever(supportedCountriesViewEntityMapper.apply(any())).thenReturn(supportedCountriesViewEntity)

        whenever(allowedPaymentMethodsViewEntityMapper.apply(any())).thenReturn(supportedIcons)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            isBillingCountryFieldRequired = true,
            supportedCountriesList = listOf(supportedCountriesViewEntity),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
            allowedPaymentMethodsIcons = listOf(1, 2, 3),
            cardHolderInputField = InputFieldState(value = ""),
            emailInputField = InputFieldState(value = ""),
            isEmailInputFieldRequired = false,
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "",
                expireDateValueValue = "",
            ),
            isPostalCodeFieldRequired = true,
            postalCodeField = InputFieldState(value = ""),
            isLoading = false,
            isEnabled = false

        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper
        )
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user clicks on pay button`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
        val supportedCountriesViewEntity = SupportedCountriesViewEntity(
            countryName = "EGP",
            countryCode = "EG",
            isPostalCodeEnabled = true,
        )
        val supportedIcons = listOf(1, 2, 3)
        whenever(observePaymentStatus.observePaymentStates()).thenReturn(paymentStateFakeFlow)
        paymentIntentFakeFlow.tryEmit(
            PaymentIntentResult.Success(
                result = PaymentIntentDomainEntity(
                    "id",
                    "token",
                    AmountDomainEntity(
                        10L,
                        "100",
                        "GBP"
                    ),
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        whenever(getSupportedCountriesUseCase.getSupportedCountries()).thenReturn(
            listOf(
                SupportedCountriesDomainEntity("", "", false)
            )
        )
        whenever(supportedCountriesViewEntityMapper.apply(any())).thenReturn(supportedCountriesViewEntity)

        whenever(allowedPaymentMethodsViewEntityMapper.apply(any())).thenReturn(supportedIcons)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            isBillingCountryFieldRequired = true,
            supportedCountriesList = listOf(supportedCountriesViewEntity),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
            allowedPaymentMethodsIcons = listOf(1, 2, 3),
            cardHolderInputField = InputFieldState(value = ""),
            emailInputField = InputFieldState(value = ""),
            isEmailInputFieldRequired = false,
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "",
                expireDateValueValue = "",
            ),
            isPostalCodeFieldRequired = true,
            postalCodeField = InputFieldState(value = ""),
            isLoading = true,
            isEnabled = false

        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper
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
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
        val supportedCountriesViewEntity = SupportedCountriesViewEntity(
            countryName = "EGP",
            countryCode = "EG",
            isPostalCodeEnabled = true,
        )
        val supportedIcons = listOf(1, 2, 3)
        whenever(observePaymentStatus.observePaymentStates()).thenReturn(paymentStateFakeFlow)
        paymentIntentFakeFlow.tryEmit(
            PaymentIntentResult.Success(
                result = PaymentIntentDomainEntity(
                    "id",
                    "token",
                    AmountDomainEntity(
                        10L,
                        "100",
                        "GBP"
                    ),
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        whenever(getSupportedCountriesUseCase.getSupportedCountries()).thenReturn(
            listOf(
                SupportedCountriesDomainEntity("", "", false)
            )
        )
        whenever(supportedCountriesViewEntityMapper.apply(any())).thenReturn(supportedCountriesViewEntity)

        whenever(allowedPaymentMethodsViewEntityMapper.apply(any())).thenReturn(supportedIcons)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            isBillingCountryFieldRequired = true,
            supportedCountriesList = listOf(supportedCountriesViewEntity),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
            allowedPaymentMethodsIcons = listOf(1, 2, 3),
            cardHolderInputField = InputFieldState(value = ""),
            emailInputField = InputFieldState(value = ""),
            isEmailInputFieldRequired = false,
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "",
                expireDateValueValue = "",
            ),
            isPostalCodeFieldRequired = true,
            postalCodeField = InputFieldState(value = ""),
            isLoading = false,
            isEnabled = false

        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper
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
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
        val supportedCountriesViewEntity = SupportedCountriesViewEntity(
            countryName = "EGP",
            countryCode = "EG",
            isPostalCodeEnabled = true,
        )
        val supportedIcons = listOf(1, 2, 3)
        whenever(observePaymentStatus.observePaymentStates()).thenReturn(paymentStateFakeFlow)
        paymentIntentFakeFlow.tryEmit(
            PaymentIntentResult.Success(
                result = PaymentIntentDomainEntity(
                    "id",
                    "token",
                    AmountDomainEntity(
                        10L,
                        "100",
                        "GBP"
                    ),
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        whenever(getSupportedCountriesUseCase.getSupportedCountries()).thenReturn(
            listOf(
                SupportedCountriesDomainEntity("", "", false)
            )
        )
        whenever(supportedCountriesViewEntityMapper.apply(any())).thenReturn(supportedCountriesViewEntity)

        whenever(allowedPaymentMethodsViewEntityMapper.apply(any())).thenReturn(supportedIcons)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            isBillingCountryFieldRequired = true,
            supportedCountriesList = listOf(supportedCountriesViewEntity),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
            allowedPaymentMethodsIcons = listOf(1, 2, 3),
            cardHolderInputField = InputFieldState(value = "new"),
            emailInputField = InputFieldState(value = ""),
            isEmailInputFieldRequired = false,
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "",
                expireDateValueValue = "",
            ),
            isPostalCodeFieldRequired = true,
            postalCodeField = InputFieldState(value = ""),
            isLoading = false,
            isEnabled = false

        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper
        )

        viewModel.onCardHolderValueChanged("new")
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user update card information field `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
        val supportedCountriesViewEntity = SupportedCountriesViewEntity(
            countryName = "EGP",
            countryCode = "EG",
            isPostalCodeEnabled = true,
        )
        val supportedIcons = listOf(1, 2, 3)
        whenever(observePaymentStatus.observePaymentStates()).thenReturn(paymentStateFakeFlow)
        paymentIntentFakeFlow.tryEmit(
            PaymentIntentResult.Success(
                result = PaymentIntentDomainEntity(
                    "id",
                    "token",
                    AmountDomainEntity(
                        10L,
                        "100",
                        "GBP"
                    ),
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        whenever(getSupportedCountriesUseCase.getSupportedCountries()).thenReturn(
            listOf(
                SupportedCountriesDomainEntity("", "", false)
            )
        )
        whenever(supportedCountriesViewEntityMapper.apply(any())).thenReturn(supportedCountriesViewEntity)

        whenever(allowedPaymentMethodsViewEntityMapper.apply(any())).thenReturn(supportedIcons)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            isBillingCountryFieldRequired = true,
            supportedCountriesList = listOf(supportedCountriesViewEntity),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
            allowedPaymentMethodsIcons = listOf(1, 2, 3),
            cardHolderInputField = InputFieldState(value = ""),
            emailInputField = InputFieldState(value = ""),
            isEmailInputFieldRequired = false,
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "new",
                cvvValue = "new",
                expireDateValueValue = "new",
            ),
            isPostalCodeFieldRequired = true,
            postalCodeField = InputFieldState(value = ""),
            isLoading = false,
            isEnabled = false

        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper
        )
        viewModel.onCardNumberValueChanged("new")
        viewModel.onCvvValueChanged("new")
        viewModel.onExpireDareValueChanged("new")
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user update email address  field `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
        val supportedCountriesViewEntity = SupportedCountriesViewEntity(
            countryName = "EGP",
            countryCode = "EG",
            isPostalCodeEnabled = true,
        )
        val supportedIcons = listOf(1, 2, 3)
        whenever(observePaymentStatus.observePaymentStates()).thenReturn(paymentStateFakeFlow)
        paymentIntentFakeFlow.tryEmit(
            PaymentIntentResult.Success(
                result = PaymentIntentDomainEntity(
                    "id",
                    "token",
                    AmountDomainEntity(
                        10L,
                        "100",
                        "GBP"
                    ),
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true,
                    collectionEmailRequired = true,
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        whenever(getSupportedCountriesUseCase.getSupportedCountries()).thenReturn(
            listOf(
                SupportedCountriesDomainEntity("", "", false)
            )
        )
        whenever(supportedCountriesViewEntityMapper.apply(any())).thenReturn(supportedCountriesViewEntity)

        whenever(allowedPaymentMethodsViewEntityMapper.apply(any())).thenReturn(supportedIcons)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            isBillingCountryFieldRequired = true,
            supportedCountriesList = listOf(supportedCountriesViewEntity),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
            allowedPaymentMethodsIcons = listOf(1, 2, 3),
            cardHolderInputField = InputFieldState(value = ""),
            emailInputField = InputFieldState(value = "new"),
            isEmailInputFieldRequired = true,
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "",
                expireDateValueValue = "",
            ),
            isPostalCodeFieldRequired = true,
            postalCodeField = InputFieldState(value = ""),
            isLoading = false,
            isEnabled = false

        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper
        )
        viewModel.onEmailValueChanged("new")
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }
}
