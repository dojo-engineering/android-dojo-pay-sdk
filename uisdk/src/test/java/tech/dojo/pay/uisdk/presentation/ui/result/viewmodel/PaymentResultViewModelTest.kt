package tech.dojo.pay.uisdk.presentation.ui.result.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.presentation.ui.result.mapper.PaymentResultViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState

@ExperimentalCoroutinesApi
class PaymentResultViewModelTest {
    @get:Rule
    @ExperimentalCoroutinesApi
    val coroutineScope = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockResult: DojoPaymentResult = mock()

    private val mockObservePaymentIntent: ObservePaymentIntent = mock()

    private val mockMapper: PaymentResultViewEntityMapper = mock()

    private lateinit var viewModel: PaymentResultViewModel

    @Test
    fun `when init view model with success from DojoPaymentResult and success from observePaymentIntent should emits mapper result  state with orderInfo`() = runTest {
        // arrange
        val mockSuccessResult: PaymentIntentResult.Success = mock()
        val orderId = "12345"
        val expected = PaymentResultState.SuccessfulResult(
            appBarTitle = "appBarTitle",
            imageId = 1,
            status = "status",
            orderInfo = "12345",
            description = "description",
        )
        given(mockObservePaymentIntent.observePaymentIntent()).willReturn(MutableStateFlow(mockSuccessResult))
        given(mockSuccessResult.result).willReturn(mock())
        given(mockSuccessResult.result.orderId).willReturn(orderId)
        given(mockMapper.mapToOrderIdField(any())).willReturn(orderId)
        given(mockMapper.mapTpResultState(mockResult)).willReturn(expected)
        // act
        viewModel = PaymentResultViewModel(mockResult, mockObservePaymentIntent, mockMapper)
        val actual = viewModel.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when init view model with success from DojoPaymentResult and FetchFailure from observePaymentIntent should emits mapper result  state with empty orderInfo`() = runTest {
        // arrange
        val mockSuccessResult: PaymentIntentResult.FetchFailure = mock()
        val orderId = "12345"
        val expected = PaymentResultState.SuccessfulResult(
            appBarTitle = "appBarTitle",
            imageId = 1,
            status = "status",
            orderInfo = "",
            description = "description",
        )
        given(mockObservePaymentIntent.observePaymentIntent()).willReturn(MutableStateFlow(mockSuccessResult))
        given(mockMapper.mapToOrderIdField(any())).willReturn(orderId)
        given(mockMapper.mapTpResultState(mockResult)).willReturn(expected)
        // act
        viewModel = PaymentResultViewModel(mockResult, mockObservePaymentIntent, mockMapper)
        val actual = viewModel.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when init  view model with none success DojoPaymentResult and call onTryAgainClicked  should emits state  shouldNavigateToPreviousScreen as true `() {
        // arrange
        val mockSuccessResult: PaymentIntentResult.Success = mock()
        val orderId = "12345"
        val failedResult = PaymentResultState.FailedResult(
            appBarTitle = "appBarTitle",
            imageId = 1,
            status = "status",
            shouldNavigateToPreviousScreen = false,
            details = "details",
        )
        given(mockObservePaymentIntent.observePaymentIntent()).willReturn(MutableStateFlow(mockSuccessResult))
        given(mockSuccessResult.result).willReturn(mock())
        given(mockSuccessResult.result.orderId).willReturn(orderId)
        given(mockMapper.mapToOrderIdField(any())).willReturn(orderId)
        given(mockMapper.mapTpResultState(mockResult)).willReturn(failedResult)
        // act
        viewModel = PaymentResultViewModel(mockResult, mockObservePaymentIntent, mockMapper)
        viewModel.onTryAgainClicked()
        val actual = viewModel.state.value
        // assert
        Assert.assertEquals(failedResult.copy(shouldNavigateToPreviousScreen = true), actual)
    }
}
