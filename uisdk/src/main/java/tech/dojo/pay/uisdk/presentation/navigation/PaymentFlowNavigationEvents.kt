package tech.dojo.pay.uisdk.presentation.navigation

import tech.dojo.pay.sdk.DojoPaymentResult

sealed class PaymentFlowNavigationEvents {
    object OnBack : PaymentFlowNavigationEvents()
    object OnCloseFlow : PaymentFlowNavigationEvents()
    object PaymentMethodCheckout : PaymentFlowNavigationEvents()
    data class PaymentResult(val dojoPaymentResult: DojoPaymentResult) :
        PaymentFlowNavigationEvents()

    object CardDetailsCheckout : PaymentFlowNavigationEvents()
    object ManagePaymentMethods : PaymentFlowNavigationEvents()
}
