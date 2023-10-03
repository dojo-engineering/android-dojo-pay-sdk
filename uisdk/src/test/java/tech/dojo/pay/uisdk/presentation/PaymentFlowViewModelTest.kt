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
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.FetchPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.FetchPaymentMethodsUseCase
import tech.dojo.pay.uisdk.domain.IsSDKInitializedCorrectlyUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.entities.DarkColorPalette
import tech.dojo.pay.uisdk.entities.DojoPaymentType
import tech.dojo.pay.uisdk.entities.LightColorPalette
import tech.dojo.pay.uisdk.presentation.components.theme.darkColorPalette
import tech.dojo.pay.uisdk.presentation.components.theme.lightColorPalette
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowNavigationEvents
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowScreens

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
    private var paymentType: DojoPaymentType = DojoPaymentType.PAYMENT_CARD
    private val fetchPaymentIntentUseCase: FetchPaymentIntentUseCase = mock()
    private val observePaymentIntent: ObservePaymentIntent = mock()
    private val fetchPaymentMethodsUseCase: FetchPaymentMethodsUseCase = mock()
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase = mock()
    private val isSDKInitializedCorrectlyUseCase: IsSDKInitializedCorrectlyUseCase = mock()

    @Test
    fun `when initialize view model with Success state from payment intent and isPaymentAlreadyCollected is false and  isSDKInitializedCorrectlyUseCase return true  then should call fetch payment methods `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(successPaymentIntent())
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(
                isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
                    any(),
                    any(),
                ),
            ).willReturn(true)
            // act
            PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            // assert
            verify(fetchPaymentMethodsUseCase).fetchPaymentMethodsWithPaymentType(
                DojoPaymentType.PAYMENT_CARD,
                "customerId",
                customerSecret,
            )
        }

    @Test
    fun `when initialize view model with Success state from payment intent with isPaymentAlreadyCollected as true then navigation should emits PaymentResult`() =
        runTest {
            // arrange
            val expected =
                PaymentFlowNavigationEvents.PaymentResult(DojoPaymentResult.SUCCESSFUL, true)
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(
                    successPaymentIntent().copy(
                        result = successPaymentIntent().result.copy(
                            isPaymentAlreadyCollected = true,
                        ),
                    ),
                )
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(
                isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
                    any(),
                    any(),
                ),
            ).willReturn(true)
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            val actual = viewModel.navigationEvent.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when initialize view model with FetchFailure state from payment intent then should close the flow with Internal error `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(PaymentIntentResult.None)
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            paymentIntentFakeFlow.tryEmit(PaymentIntentResult.FetchFailure)
            val expected = PaymentFlowNavigationEvents.CLoseFlowWithInternalError
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            val actual = viewModel.navigationEvent.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when initialize view model with Success state from payment intent and false from isSDKInitializedCorrectlyUseCase should close the flow with Internal error `() =
        runTest {
            // arrange
            val expected = PaymentFlowNavigationEvents.CLoseFlowWithInternalError

            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(successPaymentIntent())
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(
                isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
                    any(),
                    any(),
                ),
            ).willReturn(false)
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            val actual = viewModel.navigationEvent.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling updatePaymentState should call updatePaymentSate from updatePaymentStateUseCase`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(successPaymentIntent())
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(
                isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
                    any(),
                    any(),
                ),
            ).willReturn(true)
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            viewModel.updatePaymentState(isActivity = false)
            // assert
            verify(updatePaymentStateUseCase).updatePaymentSate(false)
        }

    @Test
    fun `when calling updateGpayPaymentState should call updateGpayPaymentSate from updatePaymentStateUseCase`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(successPaymentIntent())
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(
                isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
                    any(),
                    any(),
                ),
            ).willReturn(true)
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            viewModel.updateGpayPaymentState(isActivity = false)
            // assert
            verify(updatePaymentStateUseCase).updateGpayPaymentSate(false)
        }

    @Test
    fun `when calling onBackClicked should emits OnBack`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
            MutableStateFlow(successPaymentIntent())
        given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
        given(isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(any(), any())).willReturn(
            true,
        )
        val expected = PaymentFlowNavigationEvents.OnBack
        // act
        val viewModel = PaymentFlowViewModel(
            paymentId,
            customerSecret,
            paymentType,
            fetchPaymentIntentUseCase,
            observePaymentIntent,
            fetchPaymentMethodsUseCase,
            updatePaymentStateUseCase,
            isSDKInitializedCorrectlyUseCase,
        )
        viewModel.onBackClicked()
        val actual = viewModel.navigationEvent.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when calling onBackClickedWithSavedPaymentMethod should emits PaymentMethodsCheckOutWithSelectedPaymentMethod`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(successPaymentIntent())
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(
                isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
                    any(),
                    any(),
                ),
            ).willReturn(true)
            val expected =
                PaymentFlowNavigationEvents.PaymentMethodsCheckOutWithSelectedPaymentMethod()
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            viewModel.onBackClickedWithSavedPaymentMethod()
            val actual = viewModel.navigationEvent.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling onCloseFlowClicked should emits OnCloseFlow`() = runTest {
        // arrange
        val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
            MutableStateFlow(successPaymentIntent())
        given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
        given(isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(any(), any())).willReturn(
            true,
        )
        val expected = PaymentFlowNavigationEvents.OnCloseFlow
        // act
        val viewModel = PaymentFlowViewModel(
            paymentId,
            customerSecret,
            paymentType,
            fetchPaymentIntentUseCase,
            observePaymentIntent,
            fetchPaymentMethodsUseCase,
            updatePaymentStateUseCase,
            isSDKInitializedCorrectlyUseCase,
        )
        viewModel.onCloseFlowClicked()
        val actual = viewModel.navigationEvent.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when calling navigateToPaymentResult with SUCCESSFUL should emits PaymentResult with popBackStack as true `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(successPaymentIntent())
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(
                isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
                    any(),
                    any(),
                ),
            ).willReturn(true)
            val expected =
                PaymentFlowNavigationEvents.PaymentResult(DojoPaymentResult.SUCCESSFUL, true)
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            viewModel.navigateToPaymentResult(DojoPaymentResult.SUCCESSFUL)
            val actual = viewModel.navigationEvent.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling navigateToPaymentResult with SDK_INTERNAL_ERROR should emits PaymentResult with popBackStack as false `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(successPaymentIntent())
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(
                isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
                    any(),
                    any(),
                ),
            ).willReturn(true)
            val expected = PaymentFlowNavigationEvents.PaymentResult(
                DojoPaymentResult.SDK_INTERNAL_ERROR,
                false,
            )
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            viewModel.navigateToPaymentResult(DojoPaymentResult.SDK_INTERNAL_ERROR)
            val actual = viewModel.navigationEvent.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling navigateToPaymentResult with FAILED should emits PaymentResult with popBackStack as false `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(successPaymentIntent())
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(
                isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
                    any(),
                    any(),
                ),
            ).willReturn(true)
            val expected =
                PaymentFlowNavigationEvents.PaymentResult(DojoPaymentResult.FAILED, false)
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            viewModel.navigateToPaymentResult(DojoPaymentResult.FAILED)
            val actual = viewModel.navigationEvent.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling navigateToCardDetailsCheckoutScreen should emits CardDetailsCheckout`() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(successPaymentIntent())
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(
                isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
                    any(),
                    any(),
                ),
            ).willReturn(true)
            val expected = PaymentFlowNavigationEvents.CardDetailsCheckout
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )

            viewModel.navigateToCardDetailsCheckoutScreen()
            val actual = viewModel.navigationEvent.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling navigateToManagePaymentMethods should emits ManagePaymentMethods with customer id `() =
        runTest {
            // arrange
            val paymentIntentFakeFlow: MutableStateFlow<PaymentIntentResult> =
                MutableStateFlow(successPaymentIntent())
            given(observePaymentIntent.observePaymentIntent()).willReturn(paymentIntentFakeFlow)
            given(
                isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
                    any(),
                    any(),
                ),
            ).willReturn(true)
            val expected = PaymentFlowNavigationEvents.ManagePaymentMethods("customerId")
            // act
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            viewModel.navigateToManagePaymentMethods()
            val actual = viewModel.navigationEvent.value
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when call isPaymentInSandBoxEnvironment with prod paymentID should return false`() =
        runTest {
            // arrange
            val viewModel = PaymentFlowViewModel(
                paymentId,
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            // act
            val actual = viewModel.isPaymentInSandBoxEnvironment()
            // assert
            Assert.assertFalse(actual)
        }

    @Test
    fun `when call isPaymentInSandBoxEnvironment with sankBox paymentID should return true `() =
        runTest {
            // arrange
            val viewModel = PaymentFlowViewModel(
                "Sandbox_paymentId",
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            // act
            val actual = viewModel.isPaymentInSandBoxEnvironment()
            // assert
            Assert.assertTrue(actual)
        }

    @Test
    fun `when call getCustomColorPalette with isDarkModeEnabled as true  should return darkColorPalette `() =
        runTest {
            // arrange
            val expected = darkColorPalette(DarkColorPalette())
            val viewModel = PaymentFlowViewModel(
                "Sandbox_paymentId",
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            // act
            val actual = viewModel.getCustomColorPalette(isDarkModeEnabled = true)
            // assert
            Assert.assertEquals(
                expected.primarySurfaceBackgroundColor,
                actual.primarySurfaceBackgroundColor,
            )
        }

    @Test
    fun `when call getCustomColorPalette with isDarkModeEnabled as false should return LightColorPalette `() =
        runTest {
            // arrange
            val expected = lightColorPalette(LightColorPalette())
            val viewModel = PaymentFlowViewModel(
                "Sandbox_paymentId",
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )
            // act
            val actual = viewModel.getCustomColorPalette(isDarkModeEnabled = false)
            // assert
            Assert.assertEquals(
                expected.primarySurfaceBackgroundColor,
                actual.primarySurfaceBackgroundColor,
            )
        }

    @Test
    fun `when  calling getFlowStartDestination with PAYMENT_CARD type the start destination should be PaymentMethodCheckout`() =
        runTest {
            // arrange
            val expected = PaymentFlowScreens.PaymentMethodCheckout
            paymentType = DojoPaymentType.PAYMENT_CARD
            val viewModel = PaymentFlowViewModel(
                "Sandbox_paymentId",
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )

            // act
            val actual = viewModel.getFlowStartDestination()
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling getFlowStartDestination with CARD_ON_FILE type the start destination should be CardDetailsCheckout`() =
        runTest {
            // arrange
            val expected = PaymentFlowScreens.CardDetailsCheckout
            paymentType = DojoPaymentType.SETUP_INTENT
            val viewModel = PaymentFlowViewModel(
                "Sandbox_paymentId",
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )

            // act
            val actual = viewModel.getFlowStartDestination()
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling getFlowStartDestination with VIRTUAL_TERMINAL type the start destination should be VirtualTerminalCheckOutScreen`() =
        runTest {
            // arrange
            val expected = PaymentFlowScreens.VirtualTerminalCheckOutScreen
            paymentType = DojoPaymentType.VIRTUAL_TERMINAL
            val viewModel = PaymentFlowViewModel(
                "Sandbox_paymentId",
                customerSecret,
                paymentType,
                fetchPaymentIntentUseCase,
                observePaymentIntent,
                fetchPaymentMethodsUseCase,
                updatePaymentStateUseCase,
                isSDKInitializedCorrectlyUseCase,
            )

            // act
            val actual = viewModel.getFlowStartDestination()
            // assert
            Assert.assertEquals(expected, actual)
        }

    private fun successPaymentIntent() = PaymentIntentResult.Success(
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
            customerId = "customerId",
        ),
    )
}
