package tech.dojo.pay.uisdk.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.core.SingleLiveData
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.FetchPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.FetchPaymentMethodsUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowNavigationEvents
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class PaymentFlowViewModel(
    private val paymentId: String,
    customerSecret: String,
    private val fetchPaymentIntentUseCase: FetchPaymentIntentUseCase,
    private val observePaymentIntent: ObservePaymentIntent,
    private val fetchPaymentMethodsUseCase: FetchPaymentMethodsUseCase,
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
) : ViewModel() {

    val navigationEvent = SingleLiveData<PaymentFlowNavigationEvents>()
    private var currentCustomerId: String? = null

    init {
        viewModelScope.launch {
            try {
                fetchPaymentIntentUseCase.fetchPaymentIntent(paymentId)
                observePaymentIntent.observePaymentIntent().collect {
                    it?.let { paymentIntentResult ->
                        if (paymentIntentResult is PaymentIntentResult.Success) {
                            currentCustomerId = paymentIntentResult.result.customerId
                            fetchPaymentMethodsUseCase.fetchPaymentMethods(
                                paymentIntentResult.result.customerId ?: "",
                                customerSecret
                            )
                        }
                        if (paymentIntentResult is PaymentIntentResult.FetchFailure) {
                            closeFLowWithInternalError()
                        }
                    }
                }
            } catch (error: Throwable) {
                closeFLowWithInternalError()
            }
        }
    }

    fun updatePaymentState(isActivity: Boolean) {
        updatePaymentStateUseCase.updatePaymentSate(isActivity)
    }
    fun updateGpayPaymentState(isActivity: Boolean) {
        updatePaymentStateUseCase.updateGpayPaymentSate(isActivity)
    }
    private fun closeFLowWithInternalError() {
        navigationEvent.value = PaymentFlowNavigationEvents.CLoseFlowWithInternalError
    }

    fun onBackClicked() {
        navigationEvent.value = PaymentFlowNavigationEvents.OnBack
    }

    fun onBackClickedWithSavedPaymentMethod(currentSelectedMethod: PaymentMethodItemViewEntityItem? = null) {
        navigationEvent.value =
            PaymentFlowNavigationEvents.PaymentMethodsCheckOutWithSelectedPaymentMethod(
                currentSelectedMethod
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
        navigationEvent.value =
            PaymentFlowNavigationEvents.PaymentResult(dojoPaymentResult, popBackStack)
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

    fun isPaymentInSandBoxEnvironment(): Boolean = paymentId.lowercase().contains("sandbox")
}
