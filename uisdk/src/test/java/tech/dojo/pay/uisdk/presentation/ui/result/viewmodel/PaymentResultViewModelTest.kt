package tech.dojo.pay.uisdk.presentation.ui.result.viewmodel

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
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.RefreshPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class PaymentResultViewModelTest {
    @get:Rule
    @ExperimentalCoroutinesApi
    val coroutineScope = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val observePaymentIntent: ObservePaymentIntent = mock()
    private val refreshPaymentIntent: RefreshPaymentIntentUseCase = mock()
    private val isDarkModeEnabled: Boolean = false

    @Test
    fun `test initial state in case of success result`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val result: DojoPaymentResult = DojoPaymentResult.SUCCESSFUL
        val expected = PaymentResultState.SuccessfulResult(
            appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_success,
            imageId = R.drawable.ic_success_circle,
            status = R.string.dojo_ui_sdk_payment_result_title_success,
            orderInfo = "",
            description = ""
        )
        // act
        val actual =
            PaymentResultViewModel(
                result,
                observePaymentIntent,
                refreshPaymentIntent,
                isDarkModeEnabled
            ).state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test initial state in case of failure result`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val result: DojoPaymentResult = DojoPaymentResult.DECLINED
        val expected = PaymentResultState.FailedResult(
            appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_fail,
            imageId = R.drawable.ic_error_circle,
            showTryAgain = true,
            status = R.string.dojo_ui_sdk_payment_result_title_fail,
            orderInfo = "",
            details = R.string.dojo_ui_sdk_payment_result_failed_description,
            isTryAgainLoading = false,
            shouldNavigateToPreviousScreen = false
        )
        // act
        val actual =
            PaymentResultViewModel(
                result,
                observePaymentIntent,
                refreshPaymentIntent,
                isDarkModeEnabled
            ).state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test initial state in case of failure result and darck mode enabled `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val result: DojoPaymentResult = DojoPaymentResult.DECLINED
        val expected = PaymentResultState.FailedResult(
            appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_fail,
            imageId = R.drawable.ic_error_dark,
            showTryAgain = true,
            status = R.string.dojo_ui_sdk_payment_result_title_fail,
            orderInfo = "",
            details = R.string.dojo_ui_sdk_payment_result_failed_description,
            isTryAgainLoading = false,
            shouldNavigateToPreviousScreen = false
        )
        // act
        val actual =
            PaymentResultViewModel(
                result,
                observePaymentIntent,
                refreshPaymentIntent,
                true
            ).state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test initial state in case of internal SKD Error result`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)

        val result: DojoPaymentResult = DojoPaymentResult.SDK_INTERNAL_ERROR
        val expected = PaymentResultState.FailedResult(
            appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_fail,
            imageId = R.drawable.ic_error_circle,
            showTryAgain = false,
            status = R.string.dojo_ui_sdk_payment_result_title_fail,
            orderInfo = "",
            details = R.string.dojo_ui_sdk_payment_result_failed_description,
            isTryAgainLoading = false,
            shouldNavigateToPreviousScreen = false
        )
        // act
        val actual =
            PaymentResultViewModel(
                result,
                observePaymentIntent,
                refreshPaymentIntent,
                isDarkModeEnabled
            ).state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test  state when paymentIntent emits and success result`() = runTest {
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
                    orderId = "orderId",
                    supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD)
                )
            )
        )
        val result: DojoPaymentResult = DojoPaymentResult.SUCCESSFUL
        val expected = PaymentResultState.SuccessfulResult(
            appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_success,
            imageId = R.drawable.ic_success_circle,
            status = R.string.dojo_ui_sdk_payment_result_title_success,
            orderInfo = "id",
            description = "£100"
        )
        // act
        val actual =
            PaymentResultViewModel(
                result,
                observePaymentIntent,
                refreshPaymentIntent,
                isDarkModeEnabled
            ).state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when paymentIntent emits and failure result`() = runTest {
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
                    orderId = "orderId",
                    supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD)
                )
            )
        )
        val result: DojoPaymentResult = DojoPaymentResult.DECLINED
        val expected = PaymentResultState.FailedResult(
            appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_fail,
            imageId = R.drawable.ic_error_circle,
            showTryAgain = true,
            status = R.string.dojo_ui_sdk_payment_result_title_fail,
            orderInfo = "id",
            details = R.string.dojo_ui_sdk_payment_result_failed_description,
            isTryAgainLoading = false,
            shouldNavigateToPreviousScreen = false
        )
        // act
        val actual =
            PaymentResultViewModel(
                result,
                observePaymentIntent,
                refreshPaymentIntent,
                isDarkModeEnabled
            ).state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when press on try again button and refresh the payment intent Success`() =
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
                        orderId = "orderId",
                        supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD)
                    )
                )
            )
            val result: DojoPaymentResult = DojoPaymentResult.DECLINED
            val expected = PaymentResultState.FailedResult(
                appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_fail,
                imageId = R.drawable.ic_error_circle,
                showTryAgain = true,
                status = R.string.dojo_ui_sdk_payment_result_title_fail,
                orderInfo = "id",
                details = R.string.dojo_ui_sdk_payment_result_failed_description,
                isTryAgainLoading = false,
                shouldNavigateToPreviousScreen = true
            )
            // act
            val viewModel =
                PaymentResultViewModel(
                    result,
                    observePaymentIntent,
                    refreshPaymentIntent,
                    isDarkModeEnabled
                )
            viewModel.onTryAgainClicked()
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test state when press on try again button  and refresh the payment intent failed`() =
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
            val result: DojoPaymentResult = DojoPaymentResult.DECLINED
            val expected = PaymentResultState.FailedResult(
                appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_fail,
                imageId = R.drawable.ic_error_circle,
                showTryAgain = true,
                status = R.string.dojo_ui_sdk_payment_result_title_fail,
                orderInfo = "id",
                details = R.string.dojo_ui_sdk_payment_result_failed_description,
                isTryAgainLoading = false,
                shouldNavigateToPreviousScreen = false
            )
            // act
            val viewModel =
                PaymentResultViewModel(
                    result,
                    observePaymentIntent,
                    refreshPaymentIntent,
                    isDarkModeEnabled
                )
            viewModel.onTryAgainClicked()
            paymentIntentFakeFlow.tryEmit(PaymentIntentResult.RefreshFailure)
            val actual = viewModel.state.value
            // assert
            Assert.assertEquals(expected, actual)
        }
}
