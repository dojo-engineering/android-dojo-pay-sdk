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
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
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

    @Test
    fun `test initial state`() = runTest {
        // arrange
        val expected = PaymentMethodCheckoutState(
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
            observePaymentIntent,
            gpayPaymentHandler,
            null,
            false
        ).state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when google pay enabled and isMangePaymentEnabled true`() = runTest {
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
                    )
                )
            )
        )
        val viewModel = PaymentMethodCheckoutViewModel(
            observePaymentIntent,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = ""
            ),
            true
        )
        val expected = PaymentMethodCheckoutState(
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
    fun `test state when google pay disabled  and isMangePaymentEnabled true`() = runTest {
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
                    )
                )
            )
        )
        val viewModel = PaymentMethodCheckoutViewModel(
            observePaymentIntent,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = ""
            ),
            true
        )
        val expected = PaymentMethodCheckoutState(
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
    fun `test state when google pay enabled and isMangePaymentEnabled false`() = runTest {
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
                    )
                )
            )
        )
        val viewModel = PaymentMethodCheckoutViewModel(
            observePaymentIntent,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = ""
            ),
            false
        )
        val expected = PaymentMethodCheckoutState(
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
    fun `test state when google pay disabled  and isMangePaymentEnabled false`() = runTest {
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
                    )
                )
            )
        )
        val viewModel = PaymentMethodCheckoutViewModel(
            observePaymentIntent,
            gpayPaymentHandler,
            DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = ""
            ),
            false
        )
        val expected = PaymentMethodCheckoutState(
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
    fun `test state when google pay enabled and isMangePaymentEnabled false and gPayConfig is null `() =
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
                        )
                    )
                )
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                observePaymentIntent,
                gpayPaymentHandler,
                null,
                false
            )
            val expected = PaymentMethodCheckoutState(
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
            viewModel.handleGooglePayAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test state when google pay disabled  and isMangePaymentEnabled false and gPayConfig is null`() =
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
                        )
                    )
                )
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                observePaymentIntent,
                gpayPaymentHandler,
                null,
                false
            )
            val expected = PaymentMethodCheckoutState(
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
    fun `test state when google pay enabled and isMangePaymentEnabled true and gPayConfig is null`() =
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
                        )
                    )
                )
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                observePaymentIntent,
                gpayPaymentHandler,
                null,
                true
            )
            val expected = PaymentMethodCheckoutState(
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
            viewModel.handleGooglePayAvailable()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test state when google pay disabled  and isMangePaymentEnabled true and gPayConfig is null`() =
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
                        )
                    )
                )
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                observePaymentIntent,
                gpayPaymentHandler,
                null,
                true
            )
            val expected = PaymentMethodCheckoutState(
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
}
