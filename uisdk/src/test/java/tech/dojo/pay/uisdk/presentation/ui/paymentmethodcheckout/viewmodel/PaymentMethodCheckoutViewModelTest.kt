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
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.domain.IsWalletAvailableFromDeviceAndIntentUseCase
import tech.dojo.pay.uisdk.domain.MakeGpayPaymentUseCase
import tech.dojo.pay.uisdk.domain.MakeSavedCardPaymentUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.mapper.PaymentMethodCheckoutViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayAmountButtonVState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayWithCardButtonState
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
    private val observePaymentMethods: ObservePaymentMethods = mock()
    private val savedCardPaymentHandler: DojoSavedCardPaymentHandler = mock()
    private val observePaymentStatus: ObservePaymentStatus = mock()
    private val gPayConfig: DojoGPayConfig = mock()
    private val viewEntityMapper: PaymentMethodCheckoutViewEntityMapper = mock()
    private val makeGpayPaymentUseCase: MakeGpayPaymentUseCase = mock()
    private val makeSavedCardPaymentUseCase: MakeSavedCardPaymentUseCase = mock()
    private val isWalletAvailableFromDeviceAndIntentUseCase: IsWalletAvailableFromDeviceAndIntentUseCase =
        mock()

    @Test
    fun `when init viewModel then it should emits loading in the first place`() = runTest {
        // arrange
        val expected = PaymentMethodCheckoutState(
            isGooglePayButtonVisible = false,
            isBottomSheetVisible = true,
            isBottomSheetLoading = true,
            paymentMethodItem = null,
            amountBreakDownList = listOf(),
            totalAmount = "",
            cvvFieldState = InputFieldState(value = ""),
            payWithCardButtonState = PayWithCardButtonState(
                isVisible = false,
                isPrimary = false,
                navigateToCardCheckout = false,
            ),
            payAmountButtonState = null,
        )
        // arrange
        val viewModel = PaymentMethodCheckoutViewModel(
            savedCardPaymentHandler,
            observePaymentIntent,
            observePaymentMethods,
            gpayPaymentHandler,
            gPayConfig,
            observePaymentStatus,
            viewEntityMapper,
            makeGpayPaymentUseCase,
            makeSavedCardPaymentUseCase,
            isWalletAvailableFromDeviceAndIntentUseCase,
            {},
        )
        val actual = viewModel.state.value
        // act
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when init viewModel with emission from PaymentIntent and PaymentMethods flows then viewModel should emit the state from the mapper`() =
        runTest {
            // arrange
            val expected = PaymentMethodCheckoutState(
                isGooglePayButtonVisible = true,
                isBottomSheetVisible = true,
                isBottomSheetLoading = true,
                paymentMethodItem = null,
                amountBreakDownList = listOf(),
                totalAmount = "100",
                cvvFieldState = InputFieldState(value = ""),
                payWithCardButtonState = PayWithCardButtonState(
                    isVisible = true,
                    isPrimary = true,
                    navigateToCardCheckout = true,
                ),
                payAmountButtonState = null,
            )
            val intentResult: PaymentIntentDomainEntity = mock()
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> = MutableStateFlow(
                PaymentIntentResult.Success(result = intentResult),
            )
            val paymentMethodsResul: PaymentMethodsDomainEntity = mock()
            val paymentMethodFakeFlow: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(
                    FetchPaymentMethodsResult.Success(paymentMethodsResul),
                )
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(observePaymentMethods.observe()).willReturn(paymentMethodFakeFlow)
            val paymentStatesFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(
                false,
            )
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStatesFakeFlow)
            given(observePaymentStatus.observeGpayPaymentStates()).willReturn(paymentStatesFakeFlow)
            given(viewEntityMapper.mapToViewState(any(), any(), any())).willReturn(expected)
            given(isWalletAvailableFromDeviceAndIntentUseCase.isAvailable()).willReturn(false)
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                gPayConfig,
                observePaymentStatus,
                viewEntityMapper,
                makeGpayPaymentUseCase,
                makeSavedCardPaymentUseCase,
                isWalletAvailableFromDeviceAndIntentUseCase,
                {},
            )
            val actual = viewModel.state.value
            // act
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling OnSavedPaymentMethodChanged from viewModel then it should update the selected payment method and emits updated state to view `() =
        runTest {
            // arrange
            val expected = PaymentMethodCheckoutState(
                isGooglePayButtonVisible = true,
                isBottomSheetVisible = true,
                isBottomSheetLoading = true,
                paymentMethodItem = PaymentMethodItemViewEntityItem.WalletItemItem,
                amountBreakDownList = listOf(),
                totalAmount = "",
                cvvFieldState = InputFieldState(value = ""),
                payWithCardButtonState = PayWithCardButtonState(
                    isVisible = false,
                    isPrimary = false,
                    navigateToCardCheckout = false,
                ),
                payAmountButtonState = null,
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                gPayConfig,
                observePaymentStatus,
                viewEntityMapper,
                makeGpayPaymentUseCase,
                makeSavedCardPaymentUseCase,
                isWalletAvailableFromDeviceAndIntentUseCase,
                {},
            )
            // arrange
            viewModel.onSavedPaymentMethodChanged(PaymentMethodItemViewEntityItem.WalletItemItem)
            val actual = viewModel.state.value
            // act
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling OnCvvValueChanged from viewModel then it should update the cvv value along with the pay amount button and emits updated state to view `() =
        runTest {
            // arrange
            val expected = PaymentMethodCheckoutState(
                isGooglePayButtonVisible = false,
                isBottomSheetVisible = true,
                isBottomSheetLoading = true,
                paymentMethodItem = null,
                amountBreakDownList = listOf(),
                totalAmount = "",
                cvvFieldState = InputFieldState(value = "123"),
                payWithCardButtonState = PayWithCardButtonState(
                    isVisible = false,
                    isPrimary = false,
                    navigateToCardCheckout = false,
                ),
                payAmountButtonState = PayAmountButtonVState(isEnabled = true, isLoading = false),
            )
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                gPayConfig,
                observePaymentStatus,
                viewEntityMapper,
                makeGpayPaymentUseCase,
                makeSavedCardPaymentUseCase,
                isWalletAvailableFromDeviceAndIntentUseCase,
                {},
            )
            // arrange
            viewModel.onCvvValueChanged("123")
            val actual = viewModel.state.value
            // act
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling onGpayCLicked then makePaymentWithUpdatedToken from makeGpayPaymentUseCase`() =
        runTest {
            // arrange
            val currentState = PaymentMethodCheckoutState(
                isGooglePayButtonVisible = true,
                isBottomSheetVisible = true,
                isBottomSheetLoading = true,
                paymentMethodItem = null,
                amountBreakDownList = listOf(),
                totalAmount = "100",
                cvvFieldState = InputFieldState(value = ""),
                payWithCardButtonState = PayWithCardButtonState(
                    isVisible = true,
                    isPrimary = true,
                    navigateToCardCheckout = true,
                ),
                payAmountButtonState = null,
            )
            val actualGPayConfig = DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
                allowedCardNetworks = listOf(CardsSchemes.MASTERCARD),
            )
            val intentResult = PaymentIntentDomainEntity(
                "id",
                "token",
                AmountDomainEntity(
                    10L,
                    "100",
                    "GBP",
                ),
                supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                supportedWalletSchemes = listOf(),
            )
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> = MutableStateFlow(
                PaymentIntentResult.Success(result = intentResult),
            )
            val paymentMethodsResul: PaymentMethodsDomainEntity = mock()
            val paymentMethodFakeFlow: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(
                    FetchPaymentMethodsResult.Success(paymentMethodsResul),
                )
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(observePaymentMethods.observe()).willReturn(paymentMethodFakeFlow)
            val paymentStatesFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(
                false,
            )
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStatesFakeFlow)
            given(observePaymentStatus.observeGpayPaymentStates()).willReturn(paymentStatesFakeFlow)
            given(viewEntityMapper.mapToViewState(any(), any(), any())).willReturn(currentState)
            given(isWalletAvailableFromDeviceAndIntentUseCase.isAvailable()).willReturn(false)
            val callback: (dojoPaymentResult: DojoPaymentResult) -> Unit = {}
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                actualGPayConfig,
                observePaymentStatus,
                viewEntityMapper,
                makeGpayPaymentUseCase,
                makeSavedCardPaymentUseCase,
                isWalletAvailableFromDeviceAndIntentUseCase,
                callback,
            )
            // act
            viewModel.onGpayCLicked()
            // act
            verify(makeGpayPaymentUseCase).makePaymentWithUpdatedToken(any(), any())
        }

    @Test
    fun `when calling onPayAmountClicked the makePaymentWithUpdatedToken from makeSavedCardPaymentUseCase`() =
        runTest {
            // arrange
            val currentState = PaymentMethodCheckoutState(
                isGooglePayButtonVisible = true,
                isBottomSheetVisible = true,
                isBottomSheetLoading = true,
                paymentMethodItem = null,
                amountBreakDownList = listOf(),
                totalAmount = "100",
                cvvFieldState = InputFieldState(value = ""),
                payWithCardButtonState = PayWithCardButtonState(
                    isVisible = true,
                    isPrimary = true,
                    navigateToCardCheckout = true,
                ),
                payAmountButtonState = null,
            )
            val actualGPayConfig = DojoGPayConfig(
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = "",
                allowedCardNetworks = listOf(CardsSchemes.MASTERCARD),
            )
            val intentResult = PaymentIntentDomainEntity(
                "id",
                "token",
                AmountDomainEntity(
                    10L,
                    "100",
                    "GBP",
                ),
                supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
                supportedWalletSchemes = listOf(),
            )
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> = MutableStateFlow(
                PaymentIntentResult.Success(result = intentResult),
            )
            val paymentMethodsResul: PaymentMethodsDomainEntity = mock()
            val paymentMethodFakeFlow: MutableStateFlow<FetchPaymentMethodsResult?> =
                MutableStateFlow(
                    FetchPaymentMethodsResult.Success(paymentMethodsResul),
                )
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(observePaymentMethods.observe()).willReturn(paymentMethodFakeFlow)
            val paymentStatesFakeFlow: MutableStateFlow<Boolean> = MutableStateFlow(
                false,
            )
            given(observePaymentStatus.observePaymentStates()).willReturn(paymentStatesFakeFlow)
            given(observePaymentStatus.observeGpayPaymentStates()).willReturn(paymentStatesFakeFlow)
            given(viewEntityMapper.mapToViewState(any(), any(), any())).willReturn(currentState)
            given(isWalletAvailableFromDeviceAndIntentUseCase.isAvailable()).willReturn(false)
            val callback: (dojoPaymentResult: DojoPaymentResult) -> Unit = {}
            val viewModel = PaymentMethodCheckoutViewModel(
                savedCardPaymentHandler,
                observePaymentIntent,
                observePaymentMethods,
                gpayPaymentHandler,
                actualGPayConfig,
                observePaymentStatus,
                viewEntityMapper,
                makeGpayPaymentUseCase,
                makeSavedCardPaymentUseCase,
                isWalletAvailableFromDeviceAndIntentUseCase,
                callback,
            )
            // act
            viewModel.onPayAmountClicked()
            // act
            verify(makeSavedCardPaymentUseCase).makePaymentWithUpdatedToken(any(), any())
        }
}
