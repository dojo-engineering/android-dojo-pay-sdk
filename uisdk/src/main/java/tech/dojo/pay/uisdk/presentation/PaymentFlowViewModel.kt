package tech.dojo.pay.uisdk.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.core.SingleLiveData
import tech.dojo.pay.uisdk.domain.FetchPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowNavigationEvents

class PaymentFlowViewModel(
    private val fetchPaymentIntentUseCase: FetchPaymentIntentUseCase,
    private val observePaymentIntent: ObservePaymentIntent
) : ViewModel() {

    val navigationEvent = SingleLiveData<PaymentFlowNavigationEvents>()
    init {
        viewModelScope.launch {
            fetchPaymentIntentUseCase.fetchPaymentIntent("pi_sandbox_OAzUNy08c0WdnpMHuKYQBg")
            observePaymentIntent.observePaymentIntent().collect {
                println("==========================$it")
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
        val popBackStack = dojoPaymentResult == DojoPaymentResult.SUCCESSFUL
        navigationEvent.value = PaymentFlowNavigationEvents.PaymentResult(dojoPaymentResult, popBackStack)
    }
    fun navigateToManagePaymentMethods() {
        navigationEvent.value = PaymentFlowNavigationEvents.ManagePaymentMethods
    }
    fun navigateToCardDetailsCheckoutScreen() {
        navigationEvent.value = PaymentFlowNavigationEvents.CardDetailsCheckout
    }
}
