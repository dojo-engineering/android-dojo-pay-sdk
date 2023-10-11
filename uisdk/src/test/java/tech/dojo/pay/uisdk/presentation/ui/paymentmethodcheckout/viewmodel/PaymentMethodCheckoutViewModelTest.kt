package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

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
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.WalletSchemes
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.UpdateDeviceWalletState
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntityItem
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayAmountButtonVState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayWithCarButtonState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState
@Suppress("LongMethod", "LargeClass")
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PaymentMethodCheckoutViewModelTest {

    @get:Rule
    @ExperimentalCoroutinesApi
    val coroutineScope = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val observePaymentIntent: ObservePaymentIntent = mock()
    private val gpayPaymentHandler: DojoGPayHandler = mock()
    private val updateDeviceWalletState: UpdateDeviceWalletState = mock()
    private val observePaymentMethods: ObservePaymentMethods = mock()
    private val savedCardPaymentHandler: DojoSavedCardPaymentHandler = mock()
    private val observePaymentStatus: ObservePaymentStatus = mock()
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase = mock()

    @Test
    fun `test initial state`() = runTest {
        // arrange
        val expected = PaymentMethodCheckoutState(
            gPayConfig = null,
            isGooglePayButtonVisible = false,
            isBottomSheetVisible = true,
            isBottomSheetLoading = true,
            paymentMethodItem = null,
            amountBreakDownList = listOf(),
            totalAmount = "",
            cvvFieldState = InputFieldState(value = ""),
            payWithCarButtonState = PayWithCarButtonState(
                isVisible = false,
                isPrimary = false,
                navigateToCardCheckout = false,
            ),
            payAmountButtonState = null,
        )
        // act
        val viewModel = PaymentMethodCheckoutViewModel(
            savedCardPaymentHandler,
            updateDeviceWalletState,
            observePaymentIntent,
            observePaymentMethods,
            gpayPaymentHandler,
            null,
            observePaymentStatus,
            updatePaymentStateUseCase,
            observeWalletState,
        )
        val actual = viewModel.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should emit state with allowedCardNetworks when payment Intent emits  with supportedWalletSchemes contains gpay and gPayConfig not null`() {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> = MutableStateFlow(
            PaymentIntentResult.None,
        )
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                    supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                    supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                ),
            ),
        )
        val expected = PaymentMethodCheckoutState(
            gPayConfig = DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
                allowedCardNetworks = listOf(CardsSchemes.MASTERCARD),
            ),
            isGooglePayButtonVisible = false,
            isBottomSheetVisible = true,
            isBottomSheetLoading = true,
            paymentMethodItem = null,
            amountBreakDownList = listOf(),
            totalAmount = "",
            cvvFieldState = InputFieldState(value = ""),
            payWithCarButtonState = PayWithCarButtonState(
                isVisible = false,
                isPrimary = false,
                navigateToCardCheckout = false,
            ),
            payAmountButtonState = null,
        )

        // act
        val actual = PaymentMethodCheckoutViewModel(
            savedCardPaymentHandler,
            updateDeviceWalletState,
            observePaymentIntent,
            observePaymentMethods,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
            ),
            observePaymentStatus,
            updatePaymentStateUseCase,
            observeWalletState,
        ).state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when gpay is not available from the payment intent `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> = MutableStateFlow(
            PaymentIntentResult.None,
        )
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                    supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                    supportedWalletSchemes = listOf(),
                ),
            ),
        )
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        fetchPaymentMethodsStream.tryEmit(FetchPaymentMethodsResult.Failure)

        val expected = PaymentMethodCheckoutState(
            gPayConfig = DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
                allowedCardNetworks = emptyList(),
            ),
            isGooglePayButtonVisible = false,
            isBottomSheetVisible = true,
            isBottomSheetLoading = false,
            paymentMethodItem = null,
            amountBreakDownList = listOf(),
            totalAmount = "£100",
            cvvFieldState = InputFieldState(value = ""),
            payWithCarButtonState = PayWithCarButtonState(
                isVisible = true,
                isPrimary = true,
                navigateToCardCheckout = true,
            ),
            payAmountButtonState = null,
        )

        // act
        val viewModel = PaymentMethodCheckoutViewModel(
            savedCardPaymentHandler,
            updateDeviceWalletState,
            observePaymentIntent,
            observePaymentMethods,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
            ),
            observePaymentStatus,
            updatePaymentStateUseCase,
            observeWalletState,
        )
        val actual = viewModel.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when gpay is not available from the sdk and payment intent didn't contain customer id `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                    ),
                ),
            )
            val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(null)
            whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
            fetchPaymentMethodsStream.tryEmit(FetchPaymentMethodsResult.Failure)

            val expected = PaymentMethodCheckoutState(
                gPayConfig = DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                    allowedCardNetworks = emptyList(),
                ),
                isGooglePayButtonVisible = false,
                isBottomSheetVisible = true,
                isBottomSheetLoading = false,
                paymentMethodItem = null,
                amountBreakDownList = listOf(),
                totalAmount = "£100",
                cvvFieldState = InputFieldState(value = ""),
                payWithCarButtonState = PayWithCarButtonState(
                    isVisible = true,
                    isPrimary = true,
                    navigateToCardCheckout = true,
                ),
                payAmountButtonState = null,
            )

            // act
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                updateDeviceWalletState,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                ),
                observePaymentStatus,
                updatePaymentStateUseCase,
                observeWalletState,
            )
            viewModel.handleGooglePayUnAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test state when gpay is available from the sdk and not from API and payment intent didn't contain customer id `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(),
                    ),
                ),
            )
            val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(null)
            whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
            fetchPaymentMethodsStream.tryEmit(FetchPaymentMethodsResult.Failure)

            val expected = PaymentMethodCheckoutState(
                gPayConfig = DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                    allowedCardNetworks = emptyList(),
                ),
                isGooglePayButtonVisible = false,
                isBottomSheetVisible = true,
                isBottomSheetLoading = false,
                paymentMethodItem = null,
                amountBreakDownList = listOf(),
                totalAmount = "£100",
                cvvFieldState = InputFieldState(value = ""),
                payWithCarButtonState = PayWithCarButtonState(
                    isVisible = true,
                    isPrimary = true,
                    navigateToCardCheckout = true,
                ),
                payAmountButtonState = null,
            )

            // act
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                updateDeviceWalletState,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                ),
                observePaymentStatus,
                updatePaymentStateUseCase,
                observeWalletState,
            )
            viewModel.handleGooglePayAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test state when gpay is available from the sdk and API and payment intent didn't contain customer id `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                    ),
                ),
            )
            val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(null)
            whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
            fetchPaymentMethodsStream.tryEmit(FetchPaymentMethodsResult.Failure)

            val expected = PaymentMethodCheckoutState(
                gPayConfig = DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                    allowedCardNetworks = emptyList(),
                ),
                isGooglePayButtonVisible = true,
                isBottomSheetVisible = true,
                isBottomSheetLoading = false,
                paymentMethodItem = null,
                amountBreakDownList = listOf(),
                totalAmount = "£100",
                cvvFieldState = InputFieldState(value = ""),
                payWithCarButtonState = PayWithCarButtonState(
                    isVisible = true,
                    isPrimary = false,
                    navigateToCardCheckout = true,
                ),
                payAmountButtonState = null,
            )

            // act
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                updateDeviceWalletState,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                ),
                observePaymentStatus,
                updatePaymentStateUseCase,
                observeWalletState,
            )
            viewModel.handleGooglePayAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test state if customer id exists in payment intent but there is no saved payment methods and google pay is  available`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                        customerId = " customerId",
                    ),
                ),
            )
            val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(null)
            whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
            fetchPaymentMethodsStream.tryEmit(
                FetchPaymentMethodsResult.Success(
                    PaymentMethodsDomainEntity(listOf()),
                ),
            )

            val expected = PaymentMethodCheckoutState(
                gPayConfig = DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                    allowedCardNetworks = emptyList(),
                ),
                isGooglePayButtonVisible = true,
                isBottomSheetVisible = true,
                isBottomSheetLoading = false,
                paymentMethodItem = null,
                amountBreakDownList = listOf(),
                totalAmount = "£100",
                cvvFieldState = InputFieldState(value = ""),
                payWithCarButtonState = PayWithCarButtonState(
                    isVisible = true,
                    isPrimary = false,
                    navigateToCardCheckout = true,
                ),
                payAmountButtonState = null,
            )

            // act
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                updateDeviceWalletState,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                ),
                observePaymentStatus,
                updatePaymentStateUseCase,
                observeWalletState,
            )
            viewModel.handleGooglePayAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test state if customer id exists in payment intent but there is no saved payment methods and google pay is not available`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                        customerId = " customerId",
                    ),
                ),
            )
            val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(null)
            whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
            fetchPaymentMethodsStream.tryEmit(
                FetchPaymentMethodsResult.Success(
                    PaymentMethodsDomainEntity(listOf()),
                ),
            )

            val expected = PaymentMethodCheckoutState(
                gPayConfig = DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                    allowedCardNetworks = emptyList(),
                ),
                isGooglePayButtonVisible = false,
                isBottomSheetVisible = true,
                isBottomSheetLoading = false,
                paymentMethodItem = null,
                amountBreakDownList = listOf(),
                totalAmount = "£100",
                cvvFieldState = InputFieldState(value = ""),
                payWithCarButtonState = PayWithCarButtonState(
                    isVisible = true,
                    isPrimary = true,
                    navigateToCardCheckout = true,
                ),
                payAmountButtonState = null,
            )

            // act
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                updateDeviceWalletState,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                ),
                observePaymentStatus,
                updatePaymentStateUseCase,
                observeWalletState,
            )
            viewModel.handleGooglePayUnAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `tests state when customer if exists and there is a saved payment methods and google pay is enabled `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                        customerId = " customerId",
                    ),
                ),
            )
            val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(null)
            whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
            fetchPaymentMethodsStream.tryEmit(
                FetchPaymentMethodsResult.Success(
                    PaymentMethodsDomainEntity(
                        listOf(
                            PaymentMethodsDomainEntityItem(
                                "",
                                "",
                                "",
                                CardsSchemes.VISA,
                            ),
                        ),
                    ),
                ),
            )

            val expected = PaymentMethodCheckoutState(
                gPayConfig = DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                    allowedCardNetworks = emptyList(),
                ),
                isGooglePayButtonVisible = true,
                isBottomSheetVisible = true,
                isBottomSheetLoading = false,
                paymentMethodItem = PaymentMethodItemViewEntityItem.WalletItemItem,
                amountBreakDownList = listOf(),
                totalAmount = "£100",
                cvvFieldState = InputFieldState(value = ""),
                payWithCarButtonState = PayWithCarButtonState(
                    isVisible = false,
                    isPrimary = false,
                    navigateToCardCheckout = true,
                ),
                payAmountButtonState = null,
            )

            // act
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                updateDeviceWalletState,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                ),
                observePaymentStatus,
                updatePaymentStateUseCase,
                observeWalletState,
            )
            viewModel.handleGooglePayAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `tests state when customer if exists and there is a saved payment methods and google pay is not enabled `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                        customerId = " customerId",
                    ),
                ),
            )
            val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(null)
            whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
            fetchPaymentMethodsStream.tryEmit(
                FetchPaymentMethodsResult.Success(
                    PaymentMethodsDomainEntity(
                        listOf(
                            PaymentMethodsDomainEntityItem(
                                "",
                                "",
                                "",
                                CardsSchemes.VISA,
                            ),
                        ),
                    ),
                ),
            )

            val expected = PaymentMethodCheckoutState(
                gPayConfig = DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                    allowedCardNetworks = emptyList(),
                ),
                isGooglePayButtonVisible = false,
                isBottomSheetVisible = true,
                isBottomSheetLoading = false,
                paymentMethodItem = null,
                amountBreakDownList = listOf(),
                totalAmount = "£100",
                cvvFieldState = InputFieldState(value = ""),
                payWithCarButtonState = PayWithCarButtonState(
                    isVisible = true,
                    isPrimary = true,
                    navigateToCardCheckout = false,
                ),
                payAmountButtonState = null,
            )

            // act
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                updateDeviceWalletState,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                ),
                observePaymentStatus,
                updatePaymentStateUseCase,
                observeWalletState,
            )
            viewModel.handleGooglePayUnAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test state when we have a new currently selected payment method as no Item`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
            MutableStateFlow(PaymentIntentResult.None)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                    supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                    supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                    customerId = " customerId",
                ),
            ),
        )
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        fetchPaymentMethodsStream.tryEmit(
            FetchPaymentMethodsResult.Success(
                PaymentMethodsDomainEntity(
                    listOf(
                        PaymentMethodsDomainEntityItem(
                            "",
                            "",
                            "",
                            CardsSchemes.VISA,
                        ),
                    ),
                ),
            ),
        )

        val expected = PaymentMethodCheckoutState(
            gPayConfig = DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
                allowedCardNetworks = emptyList(),
            ),
            isGooglePayButtonVisible = true,
            isBottomSheetVisible = true,
            isBottomSheetLoading = false,
            paymentMethodItem = null,
            amountBreakDownList = listOf(),
            totalAmount = "£100",
            cvvFieldState = InputFieldState(value = ""),
            payWithCarButtonState = PayWithCarButtonState(
                isVisible = true,
                isPrimary = false,
                navigateToCardCheckout = true,
            ),
            payAmountButtonState = null,
        )

        // act
        val viewModel = PaymentMethodCheckoutViewModel(
            savedCardPaymentHandler,
            updateDeviceWalletState,
            observePaymentIntent,
            observePaymentMethods,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
            ),
            observePaymentStatus,
            updatePaymentStateUseCase,
            observeWalletState,
        )
        val newValue: PaymentMethodItemViewEntityItem =
            PaymentMethodItemViewEntityItem.NoItem
        viewModel.handleGooglePayAvailable()
        viewModel.onSavedPaymentMethodChanged(newValue)
        val actual = viewModel.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when we have a new currently selected payment method`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
            MutableStateFlow(PaymentIntentResult.None)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                    supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                    supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                    customerId = " customerId",
                ),
            ),
        )
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        fetchPaymentMethodsStream.tryEmit(
            FetchPaymentMethodsResult.Success(
                PaymentMethodsDomainEntity(
                    listOf(
                        PaymentMethodsDomainEntityItem(
                            "",
                            "",
                            "",
                            CardsSchemes.VISA,
                        ),
                    ),
                ),
            ),
        )

        val expected = PaymentMethodCheckoutState(
            gPayConfig = DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
                allowedCardNetworks = emptyList(),
            ),
            isGooglePayButtonVisible = false,
            isBottomSheetVisible = true,
            isBottomSheetLoading = false,
            paymentMethodItem = PaymentMethodItemViewEntityItem.CardItemItem(
                id = "",
                icon = R.drawable.ic_mastercard,
                scheme = "masterCard",
                pan = "****9560",
            ),
            amountBreakDownList = listOf(),
            totalAmount = "£100",
            cvvFieldState = InputFieldState(value = ""),
            payWithCarButtonState = PayWithCarButtonState(
                isVisible = false,
                isPrimary = false,
                navigateToCardCheckout = false,
            ),
            payAmountButtonState = PayAmountButtonVState(false, isLoading = false),
        )

        // act
        val viewModel = PaymentMethodCheckoutViewModel(
            savedCardPaymentHandler,
            updateDeviceWalletState,
            observePaymentIntent,
            observePaymentMethods,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
            ),
            observePaymentStatus,
            updatePaymentStateUseCase,
            observeWalletState,
        )
        val newValue: PaymentMethodItemViewEntityItem =
            PaymentMethodItemViewEntityItem.CardItemItem(
                id = "",
                icon = R.drawable.ic_mastercard,
                scheme = "masterCard",
                pan = "****9560",
            )
        viewModel.handleGooglePayAvailable()
        viewModel.onSavedPaymentMethodChanged(newValue)
        val actual = viewModel.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when user add valid CVV`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
            MutableStateFlow(PaymentIntentResult.None)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                    supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                    supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                    customerId = " customerId",
                ),
            ),
        )
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        fetchPaymentMethodsStream.tryEmit(
            FetchPaymentMethodsResult.Success(
                PaymentMethodsDomainEntity(
                    listOf(
                        PaymentMethodsDomainEntityItem(
                            "",
                            "",
                            "",
                            CardsSchemes.VISA,
                        ),
                    ),
                ),
            ),
        )

        val expected = PaymentMethodCheckoutState(
            gPayConfig = DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
                allowedCardNetworks = emptyList(),
            ),
            isGooglePayButtonVisible = false,
            isBottomSheetVisible = true,
            isBottomSheetLoading = false,
            paymentMethodItem = PaymentMethodItemViewEntityItem.CardItemItem(
                id = "",
                icon = R.drawable.ic_mastercard,
                scheme = "masterCard",
                pan = "****9560",
            ),
            amountBreakDownList = listOf(),
            totalAmount = "£100",
            cvvFieldState = InputFieldState(value = "123"),
            payWithCarButtonState = PayWithCarButtonState(
                isVisible = false,
                isPrimary = false,
                navigateToCardCheckout = false,
            ),
            payAmountButtonState = PayAmountButtonVState(true, isLoading = false),
        )

        val viewModel = PaymentMethodCheckoutViewModel(
            savedCardPaymentHandler,
            updateDeviceWalletState,
            observePaymentIntent,
            observePaymentMethods,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
            ),
            observePaymentStatus,
            updatePaymentStateUseCase,
            observeWalletState,
        )
        val newValue: PaymentMethodItemViewEntityItem =
            PaymentMethodItemViewEntityItem.CardItemItem(
                id = "",
                icon = R.drawable.ic_mastercard,
                scheme = "masterCard",
                pan = "****9560",
            )
        viewModel.handleGooglePayAvailable()
        viewModel.onSavedPaymentMethodChanged(newValue)

        // act
        viewModel.onCvvValueChanged("123")
        val actual = viewModel.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `executeSavedCardPayment from savedCardPaymentHandler should be called when user clicks on pay amount`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                        customerId = " customerId",
                    ),
                ),
            )
            val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(null)
            whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
            fetchPaymentMethodsStream.tryEmit(
                FetchPaymentMethodsResult.Success(
                    PaymentMethodsDomainEntity(
                        listOf(
                            PaymentMethodsDomainEntityItem(
                                "",
                                "",
                                "",
                                CardsSchemes.VISA,
                            ),
                        ),
                    ),
                ),
            )

            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                updateDeviceWalletState,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                ),
                observePaymentStatus,
                updatePaymentStateUseCase,
                observeWalletState,
            )
            val newValue: PaymentMethodItemViewEntityItem =
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "",
                    icon = R.drawable.ic_mastercard,
                    scheme = "masterCard",
                    pan = "****9560",
                )
            viewModel.handleGooglePayAvailable()
            viewModel.onSavedPaymentMethodChanged(newValue)
            viewModel.onCvvValueChanged("123")

            // act
            viewModel.onPayAmountClicked()
            // assert
            verify(savedCardPaymentHandler).executeSavedCardPayment(
                "token",
                DojoCardPaymentPayLoad.SavedCardPaymentPayLoad(
                    cv2 = "123",
                    paymentMethodId = "",
                ),
            )
        }

    @Test
    fun `executeGPay should be called in case if user click on pay with gpay button`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                        customerId = " customerId",
                    ),
                ),
            )
            val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(null)
            whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
            fetchPaymentMethodsStream.tryEmit(
                FetchPaymentMethodsResult.Success(
                    PaymentMethodsDomainEntity(
                        listOf(
                            PaymentMethodsDomainEntityItem(
                                "",
                                "",
                                "",
                                CardsSchemes.VISA,
                            ),
                        ),
                    ),
                ),
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                updateDeviceWalletState,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                ),
                observePaymentStatus,
                updatePaymentStateUseCase,
                observeWalletState,
            )
            viewModel.handleGooglePayAvailable()
            // act
            viewModel.onGpayCLicked()
            // assert
            verify(gpayPaymentHandler).executeGPay(any(), any())
        }

    @Test
    fun `observeGooglePayPaymentState should be called in case if user click on pay with gpay button`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY),
                        customerId = " customerId",
                    ),
                ),
            )
            val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(null)
            whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
            fetchPaymentMethodsStream.tryEmit(
                FetchPaymentMethodsResult.Success(
                    PaymentMethodsDomainEntity(
                        listOf(
                            PaymentMethodsDomainEntityItem(
                                "",
                                "",
                                "",
                                CardsSchemes.VISA,
                            ),
                        ),
                    ),
                ),
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                updateDeviceWalletState,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = "",
                ),
                observePaymentStatus,
                updatePaymentStateUseCase,
                observeWalletState,
            )
            viewModel.handleGooglePayAvailable()
            // act
            viewModel.onGpayCLicked()
            // assert
            verify(observePaymentStatus).observeGpayPaymentStates()
        }
}
