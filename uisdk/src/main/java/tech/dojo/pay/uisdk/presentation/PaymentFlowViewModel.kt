package tech.dojo.pay.uisdk.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.DojoSDKDebugConfig
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.core.SingleLiveData
import tech.dojo.pay.uisdk.domain.FetchPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.FetchPaymentMethodsUseCase
import tech.dojo.pay.uisdk.domain.IsSDKInitializedCorrectlyUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.UpdateDeviceWalletState
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.entities.DarkColorPalette
import tech.dojo.pay.uisdk.entities.DojoPaymentType
import tech.dojo.pay.uisdk.entities.LightColorPalette
import tech.dojo.pay.uisdk.presentation.components.theme.darkColorPalette
import tech.dojo.pay.uisdk.presentation.components.theme.lightColorPalette
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowNavigationEvents
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowScreens
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class PaymentFlowViewModel(
    private val paymentId: String,
    customerSecret: String,
    private val paymentType: DojoPaymentType,
    private val fetchPaymentIntentUseCase: FetchPaymentIntentUseCase,
    private val observePaymentIntent: ObservePaymentIntent,
    private val fetchPaymentMethodsUseCase: FetchPaymentMethodsUseCase,
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val isSDKInitializedCorrectlyUseCase: IsSDKInitializedCorrectlyUseCase,
    private val updateDeviceWalletState: UpdateDeviceWalletState,
) : ViewModel() {

    val allowedCardsSchemes = SingleLiveData<List<CardsSchemes>>()
    val navigationEvent = SingleLiveData<PaymentFlowNavigationEvents>()
    private var currentCustomerId: String? = null

    init {
        configureDojoSDKDebugConfig()
        viewModelScope.launch {
            try {
                fetchPaymentIntentUseCase.fetchPaymentIntentWithPaymentType(paymentType, paymentId)
                observePaymentIntent.observePaymentIntent().collect {
                    handlePaymentIntentResult(
                        it,
                        customerSecret,
                    )
                }
            } catch (error: Throwable) {
                closeFlowWithInternalError()
            }
        }
    }

    private fun configureDojoSDKDebugConfig() {
        if (DojoSDKDropInUI.dojoSDKDebugConfig != null) {
            DojoSDKDropInUI.dojoSDKDebugConfig?.let { DojoSdk.dojoSDKDebugConfig = it }
        } else {
            val dojoSDKDebugConfig = DojoSDKDebugConfig(
                isSandboxWallet = isPaymentInSandBoxEnvironment(),
                isSandboxIntent = isPaymentInSandBoxEnvironment(),
            )
            DojoSdk.dojoSDKDebugConfig = dojoSDKDebugConfig
        }
    }

    private fun handlePaymentIntentResult(
        paymentIntentResult: PaymentIntentResult,
        customerSecret: String,
    ) {
        if (paymentIntentResult is PaymentIntentResult.Success) {
            handlePaymentIntentSuccess(paymentIntentResult, customerSecret)
        }
        if (paymentIntentResult is PaymentIntentResult.FetchFailure) {
            closeFlowWithInternalError()
        }
    }

    private fun handlePaymentIntentSuccess(
        paymentIntentResult: PaymentIntentResult.Success,
        customerSecret: String,
    ) {
        val isInitCorrectly = isSDKInitializedCorrectlyUseCase.isSDKInitiatedCorrectly(
            paymentIntentResult.result,
            paymentType,
        )
        if (isInitCorrectly) {
            if (paymentIntentResult.result.isPaymentAlreadyCollected) {
                navigateToPaymentResult(DojoPaymentResult.SUCCESSFUL)
            } else {
                currentCustomerId = paymentIntentResult.result.customerId
                if (paymentType == DojoPaymentType.PAYMENT_CARD) {
                    allowedCardsSchemes.postValue(paymentIntentResult.result.supportedCardsSchemes)
                    fetchPaymentMethodsUseCase.fetchPaymentMethodsWithPaymentType(
                        paymentType,
                        paymentIntentResult.result.customerId ?: "",
                        customerSecret,
                    )
                }
            }
        } else {
            closeFlowWithInternalError()
        }
    }

    fun updatePaymentState(isActivity: Boolean) {
        updatePaymentStateUseCase.updatePaymentSate(isActivity)
    }

    fun updateGpayPaymentState(isActivity: Boolean) {
        updatePaymentStateUseCase.updateGpayPaymentSate(isActivity)
    }

    fun updateDeviceWalletState(isAvailable: Boolean) {
        updateDeviceWalletState.updateDeviceWalletState(isAvailable)
    }
    private fun closeFlowWithInternalError() {
        navigationEvent.value = PaymentFlowNavigationEvents.CLoseFlowWithInternalError
    }

    fun onBackClicked() {
        navigationEvent.value = PaymentFlowNavigationEvents.OnBack
    }

    fun onBackClickedWithSavedPaymentMethod(currentSelectedMethod: PaymentMethodItemViewEntityItem? = null) {
        navigationEvent.value =
            PaymentFlowNavigationEvents.PaymentMethodsCheckOutWithSelectedPaymentMethod(
                currentSelectedMethod,
            )
    }

    fun onCloseFlowClicked() {
        navigationEvent.value = PaymentFlowNavigationEvents.OnCloseFlow
    }

    fun navigateToPaymentResult(dojoPaymentResult: DojoPaymentResult) {
        var popBackStack = false
        if (dojoPaymentResult == DojoPaymentResult.SUCCESSFUL) {
            popBackStack = true
        }
        navigationEvent.postValue(PaymentFlowNavigationEvents.PaymentResult(dojoPaymentResult, popBackStack))
    }

    fun navigateToManagePaymentMethods() {
        val customerId =
            if (currentCustomerId?.isEmpty() != false || currentCustomerId?.isBlank() != false) {
                null
            } else {
                currentCustomerId
            }
        navigationEvent.value = PaymentFlowNavigationEvents.ManagePaymentMethods(customerId)
    }

    fun navigateToCardDetailsCheckoutScreen() {
        navigationEvent.value = PaymentFlowNavigationEvents.CardDetailsCheckout
    }

    fun getFlowStartDestination(): PaymentFlowScreens {
        return when (paymentType) {
            DojoPaymentType.PAYMENT_CARD -> PaymentFlowScreens.PaymentMethodCheckout
            DojoPaymentType.SETUP_INTENT -> PaymentFlowScreens.CardDetailsCheckout
            DojoPaymentType.VIRTUAL_TERMINAL -> PaymentFlowScreens.VirtualTerminalCheckOutScreen
        }
    }

    fun isPaymentInSandBoxEnvironment(): Boolean = paymentId.lowercase().contains("sandbox")

    fun getCustomColorPalette(isDarkModeEnabled: Boolean) = if (isDarkModeEnabled) {
        darkColorPalette(
            DojoSDKDropInUI.dojoThemeSettings?.darkColorPalette
                ?: DarkColorPalette(),
        )
    } else {
        lightColorPalette(
            DojoSDKDropInUI.dojoThemeSettings?.lightColorPalette
                ?: LightColorPalette(),
        )
    }
}
