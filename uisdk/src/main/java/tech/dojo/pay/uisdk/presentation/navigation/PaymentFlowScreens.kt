package tech.dojo.pay.uisdk.presentation.navigation

import tech.dojo.pay.sdk.DojoPaymentResult

sealed class PaymentFlowScreens(val rout: String) {
    object PaymentMethodCheckout : PaymentFlowScreens("PaymentMethodCheckout")

    object PaymentResult : PaymentFlowScreens("PaymentResult/{dojoPaymentResult}") {
        fun createRoute(dojoPaymentResult: DojoPaymentResult) = "PaymentResult/$dojoPaymentResult"
    }

    object CardDetailsCheckout : PaymentFlowScreens("CardDetailsCheckout")

    object VirtualTerminalCheckOutScreen : PaymentFlowScreens("VirtualTerminalCheckOutScreen")

    object ManagePaymentMethods : PaymentFlowScreens("ManagePaymentMethods/{customerId}") {
        fun createRoute(customerId: String?) = "ManagePaymentMethods/$customerId"
    }
}
