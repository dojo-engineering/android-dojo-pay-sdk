package tech.dojo.pay.sdk.card.presentation.card.handler

import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload

interface DojoCardPaymentHandler {

    fun executeCardPayment(token: String, payload: DojoCardPaymentPayload)
}
