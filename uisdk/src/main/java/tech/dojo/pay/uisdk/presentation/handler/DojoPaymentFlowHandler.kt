package tech.dojo.pay.uisdk.presentation.handler

interface DojoPaymentFlowHandler {
    fun startPaymentFlow(paymentToken: String)
}
