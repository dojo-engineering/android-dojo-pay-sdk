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
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
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
            cardHolderInputField = InputFieldState(value = ""),
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
            updatePaymentStateUseCase
        )
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when paymentIntent emits`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
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
                    )
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            cardHolderInputField = InputFieldState(value = ""),
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
            updatePaymentStateUseCase
        )
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when paymentState emits`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
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
                    )
                )
            )
        )
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            cardHolderInputField = InputFieldState(value = ""),
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
            updatePaymentStateUseCase
        )
        viewModel.onPayWithCardClicked()
        paymentStateFakeFlow.tryEmit(false)
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user clicks on pay button`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
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
                    )
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            cardHolderInputField = InputFieldState(value = ""),
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "",
                expireDateValueValue = "",
            ),
            isLoading = true,
            isEnabled = false

        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase
        )
        viewModel.onPayWithCardClicked()
        // assert
        verify(updatePaymentStateUseCase).updatePaymentSate(any())
        verify(dojoCardPaymentHandler).executeCardPayment(any(), any())
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user update card holder field `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
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
                    )
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            cardHolderInputField = InputFieldState(value = "new"),
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
            updatePaymentStateUseCase
        )
        viewModel.onCardHolderValueChanged("new")
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user update card number field `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
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
                    )
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            cardHolderInputField = InputFieldState(value = ""),
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "new",
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
            updatePaymentStateUseCase
        )
        viewModel.onCardNumberValueChanged("new")
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user update cvv field `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
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
                    )
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            cardHolderInputField = InputFieldState(value = ""),
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "new",
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
            updatePaymentStateUseCase
        )
        viewModel.onCvvValueChanged("new")
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when user update expire date field `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
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
                    )
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            cardHolderInputField = InputFieldState(value = ""),
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "",
                expireDateValueValue = "new",
            ),
            isLoading = false,
            isEnabled = false
        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase
        )
        viewModel.onExpireDareValueChanged("new")
        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `test state when all field are not empty `() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        whenever(observePaymentIntent.observePaymentIntent()).thenReturn(paymentIntentFakeFlow)
        val paymentStateFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
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
                    )
                )
            )
        )
        paymentStateFakeFlow.tryEmit(true)
        val expected = CardDetailsCheckoutState(
            totalAmount = "100",
            amountCurrency = "£",
            cardHolderInputField = InputFieldState(value = "new"),
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "new",
                cvvValue = "new",
                expireDateValueValue = "new",
            ),
            isLoading = false,
            isEnabled = true
        )
        // act
        val viewModel = CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase
        )
        viewModel.onExpireDareValueChanged("new")
        viewModel.onCardHolderValueChanged("new")
        viewModel.onCvvValueChanged("new")
        viewModel.onCardNumberValueChanged("new")

        // assert
        Assert.assertEquals(expected, viewModel.state.value)
    }
}
