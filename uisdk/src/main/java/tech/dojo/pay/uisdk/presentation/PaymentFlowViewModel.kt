package tech.dojo.pay.uisdk.presentation

import androidx.lifecycle.ViewModel
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.core.SingleLiveData
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowNavigationEvents

class PaymentFlowViewModel() : ViewModel() {

    val navigationEvent = SingleLiveData<PaymentFlowNavigationEvents>()

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
