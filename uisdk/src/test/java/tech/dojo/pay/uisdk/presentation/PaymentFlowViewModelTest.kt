package tech.dojo.pay.uisdk.presentation

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
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.FetchPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.FetchPaymentMethodsUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowNavigationEvents

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class PaymentFlowViewModelTest {
    @get:Rule
    @ExperimentalCoroutinesApi
    val coroutineScope = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val paymentId: String = "paymentId"
    private val customerSecret: String = "customerSecret"
    private val fetchPaymentIntentUseCase: FetchPaymentIntentUseCase = mock()
    private val observePaymentIntent: ObservePaymentIntent = mock()
    private val fetchPaymentMethodsUseCase: FetchPaymentMethodsUseCase = mock()
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase = mock()

    @Test
    fun `initialize view model with FetchFailure state from payment intent should close the flow with Internal error `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> =
                MutableStateFlow(null)
            whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
            paymentIntentFakeFlow.tryEmit(PaymentIntentResult.FetchFailure)
            val expected = PaymentFlowNavigationEvents.CLoseFlowWithInternalError
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase
            )
            val actual = viewModel.navigationEvent.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `initialize view model with Success state from payment intent should call fetch payment methods `() =
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
                        supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                        collectionBillingAddressRequired = true,
                        customerId = "customerId"
                    )
                )
            )
            // act
            PaymentFlowViewModel(
                paymentId,
                customerSecret,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase
            )
            // assert
            verify(fetchPaymentMethodsUseCase).fetchPaymentMethods("customerId", customerSecret)
        }

    @Test
    fun `calling updatePaymentState should call updatePaymentSate from updatePaymentStateUseCase`() =
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
                        supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                        collectionBillingAddressRequired = true,
                        customerId = "customerId"
                    )
                )
            )
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase
            )
            viewModel.updatePaymentState(isActivity = false)
            // assert
            verify(updatePaymentStateUseCase).updatePaymentSate(false)
        }

    @Test
    fun `calling onBackClicked should emits OnBack`() = runTest {
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
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true,
                    customerId = "customerId"
                )
            )
        )
        val expected = PaymentFlowNavigationEvents.OnBack
        // act
        val viewModel = PaymentFlowViewModel(
            paymentId,
            customerSecret,
            fetchPaymentIntentUseCase,
            observePaymentIntent,
            fetchPaymentMethodsUseCase,
            updatePaymentStateUseCase
        )
        viewModel.onBackClicked()
        val actual = viewModel.navigationEvent.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling onBackClickedWithSavedPaymentMethod should emits PaymentMethodsCheckOutWithSelectedPaymentMethod`() = runTest {
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
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true,
                    customerId = "customerId"
                )
            )
        )
        val expected = PaymentFlowNavigationEvents.PaymentMethodsCheckOutWithSelectedPaymentMethod()
        // act
        val viewModel = PaymentFlowViewModel(
            paymentId,
            customerSecret,
            fetchPaymentIntentUseCase,
            observePaymentIntent,
            fetchPaymentMethodsUseCase,
            updatePaymentStateUseCase
        )
        viewModel.onBackClickedWithSavedPaymentMethod()
        val actual = viewModel.navigationEvent.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling onCloseFlowClicked should emits OnCloseFlow`() = runTest {
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
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true,
                    customerId = "customerId"
                )
            )
        )
        val expected = PaymentFlowNavigationEvents.OnCloseFlow
        // act
        val viewModel = PaymentFlowViewModel(
            paymentId,
            customerSecret,
            fetchPaymentIntentUseCase,
            observePaymentIntent,
            fetchPaymentMethodsUseCase,
            updatePaymentStateUseCase
        )
        viewModel.onCloseFlowClicked()
        val actual = viewModel.navigationEvent.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling navigateToPaymentResult with SUCCESSFUL should emits PaymentResult with popBackStack as true `() = runTest {
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
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true,
                    customerId = "customerId"
                )
            )
        )
        val expected = PaymentFlowNavigationEvents.PaymentResult(DojoPaymentResult.SUCCESSFUL, true)
        // act
        val viewModel = PaymentFlowViewModel(
            paymentId,
            customerSecret,
            fetchPaymentIntentUseCase,
            observePaymentIntent,
            fetchPaymentMethodsUseCase,
            updatePaymentStateUseCase
        )
        viewModel.navigateToPaymentResult(DojoPaymentResult.SUCCESSFUL)
        val actual = viewModel.navigationEvent.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling navigateToPaymentResult with SDK_INTERNAL_ERROR should emits PaymentResult with popBackStack as false `() = runTest {
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
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true,
                    customerId = "customerId"
                )
            )
        )
        val expected = PaymentFlowNavigationEvents.PaymentResult(DojoPaymentResult.SDK_INTERNAL_ERROR, false)
        // act
        val viewModel = PaymentFlowViewModel(
            paymentId,
            customerSecret,
            fetchPaymentIntentUseCase,
            observePaymentIntent,
            fetchPaymentMethodsUseCase,
            updatePaymentStateUseCase
        )
        viewModel.navigateToPaymentResult(DojoPaymentResult.SDK_INTERNAL_ERROR)
        val actual = viewModel.navigationEvent.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling navigateToPaymentResult with FAILED should emits PaymentResult with popBackStack as false `() = runTest {
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
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true,
                    customerId = "customerId"
                )
            )
        )
        val expected = PaymentFlowNavigationEvents.PaymentResult(DojoPaymentResult.FAILED, false)
        // act
        val viewModel = PaymentFlowViewModel(
            paymentId,
            customerSecret,
            fetchPaymentIntentUseCase,
            observePaymentIntent,
            fetchPaymentMethodsUseCase,
            updatePaymentStateUseCase
        )
        viewModel.navigateToPaymentResult(DojoPaymentResult.FAILED)
        val actual = viewModel.navigationEvent.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling navigateToCardDetailsCheckoutScreen should emits CardDetailsCheckout`() = runTest {
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
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true,
                    customerId = "customerId"
                )
            )
        )
        val expected = PaymentFlowNavigationEvents.CardDetailsCheckout
        // act
        val viewModel = PaymentFlowViewModel(
            paymentId,
            customerSecret,
            fetchPaymentIntentUseCase,
            observePaymentIntent,
            fetchPaymentMethodsUseCase,
            updatePaymentStateUseCase
        )
        viewModel.navigateToCardDetailsCheckoutScreen()
        val actual = viewModel.navigationEvent.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling navigateToManagePaymentMethods should emits ManagePaymentMethods with customer id `() = runTest {
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
                    supportedCardsSchemes = listOf(CardsSchemes.AMEX),
                    collectionBillingAddressRequired = true,
                    customerId = "customerId"
                )
            )
        )
        val expected = PaymentFlowNavigationEvents.ManagePaymentMethods("customerId")
        // act
        val viewModel = PaymentFlowViewModel(
            paymentId,
            customerSecret,
            fetchPaymentIntentUseCase,
            observePaymentIntent,
            fetchPaymentMethodsUseCase,
            updatePaymentStateUseCase
        )
        viewModel.navigateToManagePaymentMethods()
        val actual = viewModel.navigationEvent.value
        // assert
        Assert.assertEquals(expected, actual)
    }
}
