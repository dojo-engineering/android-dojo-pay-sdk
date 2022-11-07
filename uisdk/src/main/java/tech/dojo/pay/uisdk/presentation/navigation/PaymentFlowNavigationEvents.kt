package tech.dojo.pay.uisdk.presentation.navigation

import tech.dojo.pay.sdk.DojoPaymentResult

sealed class PaymentFlowNavigationEvents {
    object OnBack : PaymentFlowNavigationEvents()
    object OnCloseFlow : PaymentFlowNavigationEvents()
    object CLoseFlowWithInternalError : PaymentFlowNavigationEvents()
    data class PaymentResult(val dojoPaymentResult: DojoPaymentResult, val popBackStack: Boolean) :
        PaymentFlowNavigationEvents()

    object CardDetailsCheckout : PaymentFlowNavigationEvents()
    data class ManagePaymentMethods(val customerId: String?) : PaymentFlowNavigationEvents()
}
