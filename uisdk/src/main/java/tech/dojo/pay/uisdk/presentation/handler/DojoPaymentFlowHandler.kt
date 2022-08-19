package tech.dojo.pay.uisdk.presentation.handler

import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams

interface DojoPaymentFlowHandler {
    fun startPaymentFlow(dojoPaymentFlowParams: DojoPaymentFlowParams)
}
