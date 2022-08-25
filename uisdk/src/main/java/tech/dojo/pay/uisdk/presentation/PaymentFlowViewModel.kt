package tech.dojo.pay.uisdk.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.core.SingleLiveData
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.FetchPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowNavigationEvents

class PaymentFlowViewModel(
    paymentId: String,
    private val fetchPaymentIntentUseCase: FetchPaymentIntentUseCase,
    private val observePaymentIntent: ObservePaymentIntent
) : ViewModel() {

    val navigationEvent = SingleLiveData<PaymentFlowNavigationEvents>()

    init {
        viewModelScope.launch {
            fetchPaymentIntentUseCase.fetchPaymentIntent(paymentId)
            observePaymentIntent.observePaymentIntent().collect {
                it?.let {
                    when (it) {
                        is PaymentIntentResult.Failure -> navigateToPaymentResult(DojoPaymentResult.SDK_INTERNAL_ERROR)
                    }
                }
            }
        }
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
