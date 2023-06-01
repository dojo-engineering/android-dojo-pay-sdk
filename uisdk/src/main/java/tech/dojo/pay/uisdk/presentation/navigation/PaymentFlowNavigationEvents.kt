package tech.dojo.pay.uisdk.presentation.navigation

import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem

internal sealed class PaymentFlowNavigationEvents {
    object OnBack : PaymentFlowNavigationEvents()
    data class PaymentMethodsCheckOutWithSelectedPaymentMethod(val currentSelectedMethod: PaymentMethodItemViewEntityItem? = null) :
        PaymentFlowNavigationEvents()

    object OnCloseFlow : PaymentFlowNavigationEvents()
    object CLoseFlowWithInternalError : PaymentFlowNavigationEvents()
    data class PaymentResult(val dojoPaymentResult: DojoPaymentResult, val popBackStack: Boolean) :
        PaymentFlowNavigationEvents()

    object CardDetailsCheckout : PaymentFlowNavigationEvents()
    data class ManagePaymentMethods(val customerId: String?) : PaymentFlowNavigationEvents()

    object CardDetailsCheckoutAsFirstScreen : PaymentFlowNavigationEvents()
}
