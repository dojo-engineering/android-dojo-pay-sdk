package tech.dojo.pay.uisdk.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoFetchPaymentMethodsResult
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.core.SingleLiveData
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.FetchPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.FetchPaymentMethodsUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowNavigationEvents

internal class PaymentFlowViewModel(
    paymentId: String,
    customerSecret: String,
    private val fetchPaymentIntentUseCase: FetchPaymentIntentUseCase,
    private val observePaymentIntent: ObservePaymentIntent,
    private val fetchPaymentMethodsUseCase: FetchPaymentMethodsUseCase,
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
) : ViewModel() {

    val navigationEvent = SingleLiveData<PaymentFlowNavigationEvents>()

    init {
        viewModelScope.launch {
            try {
                fetchPaymentIntentUseCase.fetchPaymentIntent(paymentId)
                observePaymentIntent.observePaymentIntent().collect {
                    it?.let { it ->
                        if (it is PaymentIntentResult.Success) {
                            it.result.customerId?.let { customerId ->
                                fetchPaymentMethodsUseCase.fetchPaymentMethods(
                                    customerId,
                                    customerSecret
                                )
                            }
                        }
                        if (it is PaymentIntentResult.FetchFailure) {
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

    private fun closeFLowWithInternalError() {
        navigationEvent.value = PaymentFlowNavigationEvents.CLoseFlowWithInternalError
    }

    fun onBackClicked() {
        navigationEvent.value = PaymentFlowNavigationEvents.OnBack
    }

    fun onCloseFlowClicked() {
        navigationEvent.value = PaymentFlowNavigationEvents.OnCloseFlow
    }

    fun navigateToPaymentResult(dojoPaymentResult: DojoPaymentResult) {
        var popBackStack = false
        if (dojoPaymentResult == DojoPaymentResult.SUCCESSFUL || dojoPaymentResult == DojoPaymentResult.SDK_INTERNAL_ERROR) {
            popBackStack = true
        }
        navigationEvent.value =
            PaymentFlowNavigationEvents.PaymentResult(dojoPaymentResult, popBackStack)
    }

    fun navigateToManagePaymentMethods() {
        navigationEvent.value = PaymentFlowNavigationEvents.ManagePaymentMethods
    }

    fun navigateToCardDetailsCheckoutScreen() {
        navigationEvent.value = PaymentFlowNavigationEvents.CardDetailsCheckout
    }
}
