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
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.sdk.card.entities.WalletSchemes
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.UpdateWalletState
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayWithCarButtonState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState

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
    private val updateWalletState: UpdateWalletState = mock()

    @Test
    fun `test initial state`() = runTest {
        // arrange
        val expected = PaymentMethodCheckoutState(
            gPayConfig = null,
            isGooglePayVisible = false,
            isBottomSheetVisible = true,
            isLoading = true,
            isGpayItemVisible = false,
            amountBreakDownList = listOf(),
            totalAmount = "",
            payWithCarButtonState = PayWithCarButtonState(
                isVisibleL = false,
                isPrimary = false
            )
        )
        // act
        val actual = PaymentMethodCheckoutViewModel(
            updateWalletState,
            observePaymentIntent,
            gpayPaymentHandler,
            null).state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Test state when payment Intent emits for the first time with supportedWalletSchemes contains gpay and gPayConfig not null`() {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                    supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                    supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY)
                )
            )
        )
        // act
        val expected = PaymentMethodCheckoutState(
            gPayConfig = DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
                allowedCardNetworks = listOf(CardsSchemes.MASTERCARD)
            ),
            isGooglePayVisible = false,
            isBottomSheetVisible = true,
            isLoading = true,
            isGpayItemVisible = false,
            amountBreakDownList = listOf(),
            totalAmount = "",
            payWithCarButtonState = PayWithCarButtonState(
                isVisibleL = false,
                isPrimary = false
            )
        )

        val actual = PaymentMethodCheckoutViewModel(
            updateWalletState,
            observePaymentIntent,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = ""
            )
        ).state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Test state when payment Intent emits for the first time with supportedWalletSchemes contains gpay and gPayConfig null`() {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                    supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                    supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY)
                )
            )
        )
        val expected = PaymentMethodCheckoutState(
            gPayConfig = null,
            isGooglePayVisible = false,
            isBottomSheetVisible = true,
            isLoading = false,
            isGpayItemVisible = false,
            amountBreakDownList = emptyList(),
            totalAmount = "£100",
            payWithCarButtonState = PayWithCarButtonState(
                isVisibleL = true,
                isPrimary = true
            )
        )
        // act

        val viewModel = PaymentMethodCheckoutViewModel(
            updateWalletState,
            observePaymentIntent,
            gpayPaymentHandler,
            null
        )
        val actual = viewModel.state.value

        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Test state when payment Intent emits for the first time with supportedWalletSchemes not contains gpay and gPayConfig not  null`() {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                    supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD)
                )
            )
        )
        val expected = PaymentMethodCheckoutState(
            gPayConfig = DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = ""
            ),
            isGooglePayVisible = false,
            isBottomSheetVisible = true,
            isLoading = false,
            isGpayItemVisible = false,
            amountBreakDownList = emptyList(),
            totalAmount = "£100",
            payWithCarButtonState = PayWithCarButtonState(
                isVisibleL = true,
                isPrimary = true
            )
        )
        // act

        val viewModel = PaymentMethodCheckoutViewModel(
            updateWalletState,
            observePaymentIntent,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = ""
            )
        )
        val actual = viewModel.state.value

        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when calling handleGooglePayAvailable with isMangePaymentEnabled true`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> =
                MutableStateFlow(null)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY)
                    )
                )
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                updateWalletState,
                observePaymentIntent,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = ""
                )
            )
            val expected = PaymentMethodCheckoutState(
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = ""
                ),
                isGooglePayVisible = true,
                isBottomSheetVisible = true,
                isLoading = false,
                isGpayItemVisible = true,
                amountBreakDownList = emptyList(),
                totalAmount = "£100",
                payWithCarButtonState = PayWithCarButtonState(
                    isVisibleL = false,
                    isPrimary = true
                )
            )
            // act
            viewModel.handleGooglePayAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test state when calling handleGooglePayUnAvailable and isMangePaymentEnabled true`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> =
                MutableStateFlow(null)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD)
                    )
                )
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                updateWalletState,
                observePaymentIntent,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = ""
                )
            )
            val expected = PaymentMethodCheckoutState(
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = ""
                ),
                isGooglePayVisible = false,
                isBottomSheetVisible = true,
                isLoading = false,
                isGpayItemVisible = false,
                amountBreakDownList = emptyList(),
                totalAmount = "£100",
                payWithCarButtonState = PayWithCarButtonState(
                    isVisibleL = true,
                    isPrimary = true
                )
            )
            // act
            viewModel.handleGooglePayUnAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test state when calling handleGooglePayAvailable with isMangePaymentEnabled false`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> =
                MutableStateFlow(null)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                        supportedWalletSchemes = listOf(WalletSchemes.GOOGLE_PAY)
                    )
                )
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                updateWalletState,
                observePaymentIntent,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = ""
                )
            )
            val expected = PaymentMethodCheckoutState(
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = ""
                ),
                isGooglePayVisible = true,
                isBottomSheetVisible = true,
                isLoading = false,
                isGpayItemVisible = false,
                amountBreakDownList = emptyList(),
                totalAmount = "£100",
                payWithCarButtonState = PayWithCarButtonState(
                    isVisibleL = true,
                    isPrimary = false
                )
            )
            // act
            viewModel.handleGooglePayAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test state when calling handleGooglePayUnAvailable with isMangePaymentEnabled false`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> =
                MutableStateFlow(null)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD)
                    )
                )
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                updateWalletState,
                observePaymentIntent,
                gpayPaymentHandler,
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = ""
                )
            )
            val expected = PaymentMethodCheckoutState(
                DojoGPayConfig(
                    merchantName = "",
                    merchantId = "",
                    gatewayMerchantId = ""
                ),
                isGooglePayVisible = false,
                isBottomSheetVisible = true,
                isLoading = false,
                isGpayItemVisible = false,
                amountBreakDownList = emptyList(),
                totalAmount = "£100",
                payWithCarButtonState = PayWithCarButtonState(
                    isVisibleL = true,
                    isPrimary = true
                )
            )
            // act
            viewModel.handleGooglePayUnAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `calling onGpayCLicked should add allowedCardNetworks from paymentIntent to gPayConfig and call executeGPay from gpayPaymentHandler`() {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> =
            MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
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
                    supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD)
                )
            )
        )
        val expectedGPayConfig = DojoGPayConfig(
            merchantName = "",
            merchantId = "",
            gatewayMerchantId = "",
            allowedCardNetworks = listOf(CardsSchemes.MASTERCARD)
        )

        val viewModel = PaymentMethodCheckoutViewModel(
            updateWalletState,
            observePaymentIntent,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = ""
            )
        )

        // act
        viewModel.onGpayCLicked()

        // assert
        verify(gpayPaymentHandler).executeGPay(
            GPayPayload = DojoGPayPayload(dojoGPayConfig = expectedGPayConfig),
            paymentIntent = DojoPaymentIntent(
                token = "token",
                totalAmount = DojoTotalAmount(
                    10L,
                    "GBP"
                )
            )
        )
    }
}
