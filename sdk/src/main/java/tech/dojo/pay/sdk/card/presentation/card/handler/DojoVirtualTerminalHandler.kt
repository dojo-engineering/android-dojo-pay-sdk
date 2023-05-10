package tech.dojo.pay.sdk.card.presentation.card.handler

import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad

interface DojoVirtualTerminalHandler {
    fun executeVirtualTerminalPayment(token: String, payload: DojoCardPaymentPayLoad.FullCardPaymentPayload)
}
