package tech.dojo.pay.sdk.card

import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload

interface DojoCardPaymentHandler {

    fun executeCardPayment(token: String, payload: DojoCardPaymentPayload)
}